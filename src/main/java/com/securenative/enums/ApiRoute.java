package com.securenative.enums;

public enum ApiRoute {
        TRACK("track"),
        VERIFY("verify");

        private String route;

        public String getRoute() {
            return route;
        }

        ApiRoute(String route) {
            this.route = route;
        }
}