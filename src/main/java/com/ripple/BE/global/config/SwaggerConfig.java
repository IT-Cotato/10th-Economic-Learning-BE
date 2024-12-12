package com.ripple.BE.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Security Scheme 정의
        SecurityScheme securityScheme =
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization");

        // Security Requirement 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .components(new Components())
                .info(
                        new Info()
                                .title("Ripple REST API") // API의 제목
                                .description("Ripple Swagger") // API에 대한 설명
                                .contact(
                                        new Contact()
                                                .name("Ripple BE Github")
                                                .url("https://github.com/IT-Cotato/10th-Economic-Learning-BE")) // BE 레포지토리
                                // 주소
                                .version("1.0.0"))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("BearerAuth", securityScheme); // API의 버전
    }
}
