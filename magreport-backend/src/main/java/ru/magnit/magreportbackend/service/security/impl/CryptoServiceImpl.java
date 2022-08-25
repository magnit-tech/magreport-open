package ru.magnit.magreportbackend.service.security.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import ru.magnit.magreportbackend.exception.InvalidApplicationSettings;
import ru.magnit.magreportbackend.service.security.CryptoService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {

    private final TextEncryptor encrypt;

    @Autowired
    public CryptoServiceImpl(Environment env) {

        var secretKey = env.getProperty("jwt.properties.secretKey");
        if (secretKey == null)
            throw new InvalidApplicationSettings("Missing key in application.properties: 'jwt.properties.secretKey'.");

        var password = secretKey.chars().mapToObj(ch -> Integer.toString(ch, 16)).collect(Collectors.joining());
        encrypt = Encryptors.text(password, password);
    }

    @Override
    public String encode(String message) {

        return encrypt.encrypt(message);
    }

    @Override
    public String decode(String message) {
        return message.isEmpty()?  message : encrypt.decrypt(message);
    }
}
