package com.ripple.BE.global.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig { // Spring WebFlux에서 HTTP 요청을 비동기로 처리하기 위한 WebClient를 설정

    // Netty HTTP 클라이언트 설정
    @Bean
    public ReactorResourceFactory resourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(false);
        return factory;
    }

    @Bean
    public WebClient webClient() {
        // HTTP 클라이언트 설정
        Function<HttpClient, HttpClient> mapper =
                client ->
                        HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000) // 연결 시간 초과 1초로 설정
                                .doOnConnected(
                                        connection ->
                                                connection
                                                        .addHandlerLast(new ReadTimeoutHandler(10))
                                                        .addHandlerLast(new WriteTimeoutHandler(10))) // 읽기 및 쓰기 시간 초과 10초로 설정
                                .responseTimeout(Duration.ofSeconds(1)); // 응답 시간 초과 1초로 설정

        // HTTP 클라이언트와 연결
        ClientHttpConnector connector = new ReactorClientHttpConnector(resourceFactory(), mapper);

        // WebClient 생성
        return WebClient.builder().clientConnector(connector).build();
    }
}
