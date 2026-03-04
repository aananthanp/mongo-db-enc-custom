package com.tech.java.mongo.demo.enc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FailedMessage API")
                        .version("1.0")
                        .description("API for managing encrypted FailedMessageDoc payloads"));
    }
}