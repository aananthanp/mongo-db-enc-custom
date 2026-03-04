package com.tech.java.mongo.demo.enc.config;

import org.springframework.context.annotation.Bean;

import java.util.Arrays;

public class MongoCustomConversions {
    @Bean
    public org.springframework.data.mongodb.core.convert.MongoCustomConversions mongoCustomConversions() {
        return new org.springframework.data.mongodb.core.convert.MongoCustomConversions(Arrays.asList(
                new EncryptPayloadConverter(),
                new DecryptPayloadConverter()
        ));
    }
}
