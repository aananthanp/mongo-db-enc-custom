package com.tech.java.mongo.demo.enc.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class GenericCryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String PREFIX = "ENC::";

    public static String encrypt(String value, String secret) {
        try {
            if (value.startsWith(PREFIX)) {
                return value;
            }

            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            String encrypted = Base64.getEncoder()
                    .encodeToString(cipher.doFinal(value.getBytes()));

            return PREFIX + encrypted;

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public static String decrypt(String value, String secret) {
        try {
            if (!value.startsWith(PREFIX)) {
                return value;
            }

            String encrypted = value.substring(PREFIX.length());

            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
