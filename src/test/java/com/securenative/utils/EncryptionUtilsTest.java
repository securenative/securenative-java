package com.securenative.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

public class EncryptionUtilsTest {
    private final String SECRET_KEY = "B00C42DAD33EAC6F6572DA756EA4915349C0A4F6";
    private final String PAYLOAD = "{\"cid\":\"198a41ff-a10f-4cda-a2f3-a9ca80c0703b\",\"vi\":\"148a42ff-b40f-4cda-a2f3-a8ca80c0703b\",\"fp\":\"6d8cabd95987f8318b1fe01593d5c2a5.24700f9f1986800ab4fcc880530dd0ed\"}";

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should encrypt message correctly")
    public void encryptTest() {
        String result = EncryptionUtils.encrypt(PAYLOAD, SECRET_KEY);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSizeGreaterThan(PAYLOAD.length());
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should decrypt message correctly")
    public void decryptTest(){
        String encryptedPayload  = "5208ae703cc2fa0851347f55d3b76d3fd6035ee081d71a401e8bc92ebdc25d42440f62310bda60628537744ac03f200d78da9e61f1019ce02087b7ce6c976e7b2d8ad6aa978c532cea8f3e744cc6a5cafedc4ae6cd1b08a4ef75d6e37aa3c0c76954d16d57750be2980c2c91ac7ef0bbd0722abd59bf6be22493ea9b9759c3ff4d17f17ab670b0b6fc320e6de982313f1c4e74c0897f9f5a32d58e3e53050ae8fdbebba9009d0d1250fe34dcde1ebb42acbc22834a02f53889076140f0eb8db1";
        String result = EncryptionUtils.decrypt(encryptedPayload, SECRET_KEY);
        Assertions.assertThat(result).isEqualTo(PAYLOAD);
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should encrypt and decrypt message correctly")
    public void encryptDecryptTest(){
        String encRes = EncryptionUtils.encrypt(PAYLOAD, SECRET_KEY);
        String decRes = EncryptionUtils.decrypt(encRes, SECRET_KEY);
        Assertions.assertThat(decRes).isEqualTo(PAYLOAD);
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should handle encryption with short key")
    public void encryptWithInvalidKeyLenTest(){
        String secretKey = "BAD_KEY";
        String result = EncryptionUtils.encrypt(PAYLOAD, secretKey);
        Assertions.assertThat(result).hasSize(0);
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should handle decryption with short key")
    public void decryptWithInvalidKeyLenTest(){
        String secretKey = "BAD_KEY";
        String encryptedPayload  = "5208ae703cc2fa0851347f55d3b76d3fd6035ee081d71a401e8bc92ebdc25d42440f62310bda60628537744ac03f200d78da9e61f1019ce02087b7ce6c976e7b2d8ad6aa978c532cea8f3e744cc6a5cafedc4ae6cd1b08a4ef75d6e37aa3c0c76954d16d57750be2980c2c91ac7ef0bbd0722abd59bf6be22493ea9b9759c3ff4d17f17ab670b0b6fc320e6de982313f1c4e74c0897f9f5a32d58e3e53050ae8fdbebba9009d0d1250fe34dcde1ebb42acbc22834a02f53889076140f0eb8db1";

        String result = EncryptionUtils.decrypt(encryptedPayload, secretKey);

        Assertions.assertThat(result).hasSize(0);
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Should decrypt with key which is too long")
    public void encryptDecryptWithKeyWhichTooLongTest(){
        String secretKey = "B00C42DAD33EAC6F6572DA756EA4915349C0A4F6B00C42DAD33EAC6F6572DA756EA4915349C0A4F6";

        String encRes = EncryptionUtils.encrypt(PAYLOAD, secretKey);
        String decRes = EncryptionUtils.decrypt(encRes, secretKey);

        Assertions.assertThat(decRes).isEqualTo(PAYLOAD);
    }

}
