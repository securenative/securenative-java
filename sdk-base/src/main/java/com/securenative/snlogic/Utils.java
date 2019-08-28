package com.securenative.snlogic;

import com.securenative.exceptions.SecureNativeSDKException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    private static String[] ipHeaders = {"x-forwarded-for", "x-client-ip", "x-real-ip", "x-forwarded", "x-cluster-client-ip", "forwarded-for", "forwarded", "via"};
    public String COOKIE_NAME = "_sn";
    public final String SN_HEADER = "x-securenative";
    public final String USERAGENT_HEADER = "user-agent";
    private final String HMAC_SHA1_ALGORITHM = "HmacSHA512";
    private Pattern VALID_IPV6_PATTERN = Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}", Pattern.CASE_INSENSITIVE);
    private final int AES_KEY_SIZE = 32;


    public Utils() {
    }


    public String remoteIpFromRequest(Function<String, String> headerExtractor) {
        Optional<String> bestCandidate = Optional.empty();
        String header = "";
        for (int i = 0; i < ipHeaders.length; i++) {
            List<String> candidates = Arrays.asList();
            header = headerExtractor.apply(ipHeaders[i]);
            if (!this.isNullOrEmpty(header)) {
                candidates = Arrays.stream(header.split(",")).map(s -> s.trim()).filter(s -> !this.isNullOrEmpty(s) &&
                        (isValidInet4Address(s) || this.isIpV6Address(s)) &&
                        !isPrivateIPAddress(s)).collect(Collectors.toList());
                if (candidates.size() > 0) {
                    return candidates.get(0);
                }
            }
            if (!bestCandidate.isPresent()) {
                bestCandidate = candidates.stream().filter(x -> isLoopBack(x)).findFirst();
            }
        }
        return "127.0.0.1";
    }


    private boolean isLoopBack(String ip) {
        try {
            return InetAddress.getByName(ip).isLoopbackAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isPrivateIPAddress(String ipAddress) {
        InetAddress ia = null;
        try {
            InetAddress ad = InetAddress.getByName(ipAddress);
            byte[] ip = ad.getAddress();
            ia = InetAddress.getByAddress(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ia.isSiteLocalAddress();
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    public String calculateRFC2104HMAC(String data, String key) throws SecureNativeSDKException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return toHexString(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SecureNativeSDKException("failed calculating hmac");
        }
    }

    private String calculateSignature(String payload, String apikey) {
        if (this.isNullOrEmpty(payload)) {
            return null;
        }
        try {
            return this.calculateRFC2104HMAC(payload, apikey);
        } catch (SecureNativeSDKException e) {
            return null;
        }
    }

    public boolean isVerifiedSnRequest(String payload, String hedaerSignature, String apiKey) {
        String signed = calculateSignature(payload, apiKey);
        if (this.isNullOrEmpty(signed) || this.isNullOrEmpty(hedaerSignature)) {
            return false;
        }
        return hedaerSignature.equals(signed);
    }

    public boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }

    public boolean isValidInet4Address(String ip) {
        String[] groups = ip.split("\\.");
        if (groups.length != 4)
            return false;
        try {
            return Arrays.stream(groups)
                    .filter(s -> s.length() > 1 && s.startsWith("0"))
                    .map(Integer::parseInt)
                    .filter(i -> (i >= 0 && i <= 255))
                    .count() == 4;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isIpV6Address(String ipAddress) {
        return this.VALID_IPV6_PATTERN.matcher(ipAddress).matches();
    }

    public String decryptAES(String s, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        if (s == null || s.length() == 0) {
            return s;
        }
        byte[] cipherText = hexToByteArray(s);
        SecretKeySpec skeySpec = new SecretKeySpec(key.substring(0, 32).getBytes("UTF-8"), "AES");
        byte[] ivBytes = Arrays.copyOfRange(cipherText, 0, AES_KEY_SIZE / 2);
        cipherText = Arrays.copyOfRange(cipherText, AES_KEY_SIZE / 2, cipherText.length);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec IVspec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, IVspec);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    private byte[] hexToByteArray(String s) {
        byte[] retValue = null;
        if (s != null && s.length() != 0) {
            retValue = new byte[s.length() / 2];
            for (int i = 0; i < retValue.length; i++) {
                retValue[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
            }
        }
        return retValue;
    }
}
