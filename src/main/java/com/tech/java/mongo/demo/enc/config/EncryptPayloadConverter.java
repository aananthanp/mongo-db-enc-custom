package com.tech.java.mongo.demo.enc.config;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@WritingConverter
public class EncryptPayloadConverter implements Converter<String, String> {
    private final TextEncryptor encryptor = Encryptors.text("secret-key", "123456");

    @Override
    public String convert(String source) {
        return encryptor.encrypt(source);
    }
}

