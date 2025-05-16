package org.example.meetlearning.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {
        return new ThreadPoolExecutor(
                10, // 核心线程数
                20, // 最大线程数
                60L, TimeUnit.SECONDS, // 空闲线程存活时间
                new LinkedBlockingQueue<>(1000), // 任务队列容量
                new CustomThreadFactory("email-pool-"), // 使用自定义线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
    }
}