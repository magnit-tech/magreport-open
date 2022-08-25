package ru.magnit.magreportbackend.config;

import ch.qos.logback.classic.LoggerContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.magnit.magreportbackend.util.LogAppender;

@RequiredArgsConstructor
@Configuration
public class LogAppenderConfig {

    @Value("$spring.profiles.active")
    private String profile;

    private final ConfigurableApplicationContext context;

    @Bean
    void initLogAppender() {
        if (profile.equals("prod"))
            addLogAppender(context, (LoggerContext) LoggerFactory.getILoggerFactory());
    }

    private void addLogAppender(ConfigurableApplicationContext context, LoggerContext loggerContext) {
        var customAppender = context.getBean(LogAppender.class);
        var rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(customAppender);
    }
}
