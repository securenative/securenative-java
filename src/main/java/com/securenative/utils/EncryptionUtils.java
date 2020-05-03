package com.securenative.utils;

import com.securenative.Logger;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;


public class EncryptionUtils {
    private static final String EMPTY_STRING = "";
    private static final Logger logger = Logger.getLogger(EncryptionUtils.class);
    private static final int AES_KEY_SIZE = 32;
    private final static char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static byte[] hexToByteArray(String s) {
        byte[] retValue = null;
        if (s != null && s.length() != 0) {
            retValue = new byte[s.length() / 2];
            for (int i = 0; i < retValue.length; i++) {
                retValue[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
            }
        }
        return retValue;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        StringBuffer hexBuffer = new StringBuffer(byteArray.length * 2);
        for (byte b : byteArray)
            for (int j = 1; j >= 0; j--)
                hexBuffer.append(HEX[(b >> (j * 4)) & 0xF]);
        return hexBuffer.toString();
    }

    private byte[] pad (byte[] buf, int size){
        int bufLen = buf.length;
        int padLen = size - bufLen%size;
        byte[] padded = new byte[bufLen+padLen];
        padded = Arrays.copyOf(buf,bufLen+padLen);
        for (int i = 0; i < padLen; i++) {
            padded[bufLen+i] = (byte)padLen;
        }
        return padded;
    }

    private static byte[] addAll(final byte[] array1, byte[] array2) {
        byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static String encrypt(String text, String key) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] ivBytes = new byte[16];
            secureRandom.nextBytes(ivBytes);
            AlgorithmParameterSpec IVspec = new IvParameterSpec(ivBytes);
            SecretKeySpec skeySpec = new SecretKeySpec(key.substring(0, AES_KEY_SIZE).getBytes(StandardCharsets.UTF_8), "AES");
            byte[] source = text.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IVspec);
            int mod = source.length % 16;
            if (mod != 0) {
                text = String.format(text + "%" + (16 - mod) + "s", " ");
            }
            return byteArrayToHex(cipher.doFinal(addAll(ivBytes,text.getBytes(StandardCharsets.UTF_8)))).trim();
        } catch (Exception ex) {
            logger.error("Unable to encrypt, err:", ex.getMessage());
        }

        return EMPTY_STRING;
    }

    public static String decrypt(String s, String key) {
        logger.info("Starting to decrypt " + s);
        if (s == null || s.length() == 0) {
            return s;
        }
        byte[] cipherText = hexToByteArray(s);
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.substring(0, AES_KEY_SIZE).getBytes("UTF-8"), "AES");
            byte[] ivBytes = Arrays.copyOfRange(cipherText, 0, AES_KEY_SIZE / 2);
            cipherText = Arrays.copyOfRange(cipherText, AES_KEY_SIZE / 2, cipherText.length);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameterSpec IVspec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, IVspec);
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8).trim();
        } catch (Exception ex) {
            logger.error("Unable to decrypt", ex.getMessage());
        }

        return EMPTY_STRING;
    }
}
