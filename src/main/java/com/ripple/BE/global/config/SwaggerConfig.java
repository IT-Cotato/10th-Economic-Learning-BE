package com.ripple.BE.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme =
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .addServersItem(
                        new io.swagger.v3.oas.models.servers.Server()
                                .url(serverUrl)
                                .description("Auto Configured Server"))
                .components(new Components().addSecuritySchemes("BearerAuth", securityScheme))
                .info(
                        new Info()
                                .title("Ripple REST API")
                                .description("Ripple Swagger")
                                .contact(
                                        new Contact()
                                                .name("Ripple BE Github")
                                                .url("https://github.com/IT-Cotato/10th-Economic-Learning-BE"))
                                .version("1.0.0"))
                .addSecurityItem(securityRequirement);
    }
}
