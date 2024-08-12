package org.simple.bankingsystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

public class FileParserConfiguration {

    @Bean(name = "fileProcessor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);  // Adjust based on your system's capabilities
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("FileProcessor-");
        executor.initialize();
        return executor;
    }
}
