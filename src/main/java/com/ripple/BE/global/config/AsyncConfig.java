package com.ripple.BE.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 스레드 풀의 기본 크기
        executor.setMaxPoolSize(100); // 스레드 풀의 최대 크기
        executor.setQueueCapacity(50); // 대기열 크기
        executor.initialize();
        return executor;
    }
}
