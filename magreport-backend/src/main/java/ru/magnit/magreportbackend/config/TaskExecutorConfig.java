package ru.magnit.magreportbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {

    @Value("${magreport.jobengine.thread-pool-size}")
    private int poolSize;

    @Value("${magreport.jobengine.thread-name-prefix}")
    private String threadNamePrefix;

    @Primary
    @Bean(name = "JobEngineTaskExecutor")
    ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(poolSize);
        taskExecutor.setMaxPoolSize(poolSize);
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.initialize();

        return taskExecutor;
    }

    @Bean(name = "JobEngineTaskExport")
    ThreadPoolTaskExecutor threadPoolTaskExport() {
        ThreadPoolTaskExecutor taskExport = new ThreadPoolTaskExecutor();
        taskExport.setCorePoolSize(poolSize);
        taskExport.setMaxPoolSize(poolSize);
        taskExport.setThreadNamePrefix(threadNamePrefix);
        taskExport.initialize();

        return  taskExport;
    }
}
