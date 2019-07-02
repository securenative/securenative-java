package com.securenative.akka

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.{BasicDirectives, MarshallingDirectives}
import akka.http.scaladsl.server.{Directive1, ValidationRejection}
import com.securenative.models.SnEvent.EventBuilder
import com.securenative.models.{EventTypes, SnEvent}
import com.securenative.snlogic.{SecureNative, Utils}

object SnDirectives extends BasicDirectives with MarshallingDirectives {
  private val DEFAULT_IP = "127.0.0.1"
  private val utils = new Utils()

  def verifyWebhook(secret: String): Directive1[String] = extractRequest.flatMap { request =>
    entity(as[String]).flatMap { body =>
      val headerSig = request.getHeader(utils.SN_HEADER)

      if (!headerSig.isPresent) {
        cancelRejection(ValidationRejection(s"Failed to extract ${utils.SN_HEADER} header from the request, this request cannot be validated and probably not sent from SecureNative."))
      }

      if (utils.isVerifiedSnRequest(body, headerSig.get().value(), secret)) {
        provide(body)
      } else {
        provide(null)
      }
    }
  }

  def extractSnEvent(eventType: EventTypes)(implicit sdk: SecureNative): Directive1[EventBuilder] = extractRequest.flatMap { request =>
    val builder = fromRequest(eventType, request)
    provide(builder)
  }

  private def fromRequest(eventName: EventTypes, httpRequest: HttpRequest)(implicit snSdk: SecureNative): SnEvent.EventBuilder = {
    val headers = Map(httpRequest.headers.map { h => h.name.toLowerCase -> h.value }: _*)
    val cookies = Map(httpRequest.cookies.map { c => c.name -> c.value }: _*)

    val ua = headers.getOrElse("user-agent", "")

    val utils = new Utils()
    val ip = utils.remoteIpFromRequest(name => headers.getOrElse(name, DEFAULT_IP))
    val remoteIp = headers.getOrElse("remote-address", DEFAULT_IP)
    val snCookie = cookies.getOrElse(snSdk.getDefaultCookieName, headers.getOrElse(utils.SN_HEADER, ""))

    val builder = new SnEvent.EventBuilder(eventName.getType)
      .withCookieValue(snCookie)
      .withIp(ip)
      .withRemoteIP(remoteIp)
      .withUserAgent(ua)

    builder
  }
}
