package com.ripple.BE.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components())
			.info(new Info()
				.title("Ripple REST API") // API의 제목
				.description("Ripple Swagger") // API에 대한 설명
				.contact(new Contact()
					.name("Ripple")
					.url("https://github.com/IT-Cotato/10th-Economic-Learning-BE")) // BE 레포지토리 주소
				.version("1.0.0")); // API의 버전
	}
}
