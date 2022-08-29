package ru.magnit.magreportbackend.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ru.magnit.magreportbackend.util.LogAppender;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class LogAppenderConfig {

    @Value("${spring.profiles.active}")
    private String profile;

    private final Path logbackConfigPath = Paths.get("logback.xml");

    private final ConfigurableApplicationContext context;

    @Bean
    void initLogAppender() {
        final var loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        final var configurator = new JoranConfigurator();

        if (Files.exists(logbackConfigPath)) {
            try (final var configStream = Files.newInputStream(logbackConfigPath)) {
                configurator.setContext(loggerContext);
                configurator.doConfigure(configStream);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try (final var configStream = new ClassPathResource(logbackConfigPath.getFileName().toString()).getInputStream()) {
                configurator.setContext(loggerContext);
                configurator.doConfigure(configStream);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (profile.equals("prod"))
            addLogAppender(context, (LoggerContext) LoggerFactory.getILoggerFactory());
    }

    private void addLogAppender(ConfigurableApplicationContext context, LoggerContext loggerContext) {
        var customAppender = context.getBean(LogAppender.class);
        var rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(customAppender);
    }
}
