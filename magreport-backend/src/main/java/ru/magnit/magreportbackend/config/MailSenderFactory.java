package ru.magnit.magreportbackend.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.service.domain.SettingsDomainService;
import ru.magnit.magreportbackend.service.security.CryptoService;

import java.util.Properties;

@Setter
@Getter
@Service
@RequiredArgsConstructor
public class MailSenderFactory {

    private final SettingsDomainService settingsService;
    private final CryptoService cryptoService;

    @Value("${spring.mail.protocol}")
    private String protocolCode;

    @Value("${spring.mail.host}")
    private String hostCode;

    @Value("${mail.port}")
    private String portCode;

    @Value("${spring.mail.username}")
    private String usernameCode;

    @Value("${spring.mail.password}")
    private String passwordCode;

    @Value("${mail.debug}")
    private String debug;


    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String protocol = settingsService.getValueSetting(protocolCode);
        String host = settingsService.getValueSetting(hostCode);
        String port = settingsService.getValueSetting(portCode);
        String username = settingsService.getValueSetting(usernameCode);
        String password = settingsService.getValueSetting(passwordCode);

        mailSender.setProtocol(protocol == null ? "protocol" : protocol);
        mailSender.setHost(host == null ? "host" : host);
        mailSender.setPort(port == null ? 0 : Integer.parseInt(port));
        mailSender.setUsername(username == null ? "login" : username);
        mailSender.setPassword(password == null ? "password" : cryptoService.decode(password));

        Properties properties = mailSender.getJavaMailProperties();

        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", debug);

        return mailSender;
    }

}
