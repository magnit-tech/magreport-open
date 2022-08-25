package ru.magnit.magreportbackend.service.domain;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.util.Pair;
import ru.magnit.magreportbackend.util.Triple;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TokenService {

    private final Map<String, Triple<Long, Long, LocalDateTime>> tokenCache = new ConcurrentHashMap<>();

    private final byte[] tokenSymbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes(StandardCharsets.UTF_8);
    private static final int TOKEN_LENGTH = 8;
    private static final int TOKEN_LIFE_TIME = 300_000;

    private String generateToken() {
        final var result = new StringBuilder(TOKEN_LENGTH);

        ThreadLocalRandom
                .current()
                .ints(TOKEN_LENGTH, 0, tokenSymbols.length)
                .map(index -> tokenSymbols[index])
                .forEach(letter -> result.append((char) letter));

        return result.toString();
    }

    public String getToken(Long associatedId, Long additionalId) {
        String token;

        do {
            token = generateToken();
        } while (tokenCache.containsKey(token));

        tokenCache.put(token, new Triple<>(associatedId, additionalId, LocalDateTime.now()));
        return token;
    }

    public Pair<Long,Long> getAssociatedValue(String token) {
        if (!tokenCache.containsKey(token))
            throw new InvalidParametersException("Token: '" + token + "' does not exists in cache.");

        final var result = new Pair<>(tokenCache.get(token).getA(),tokenCache.get(token).getB());
        tokenCache.remove(token);

        return result;
    }

    @Scheduled(fixedDelay = 300_000)
    public void clearOutdatedTokens() {
        final var now = LocalDateTime.now();

        tokenCache
                .entrySet()
                .removeIf(entry ->
                        Math.abs(Duration.between(
                                entry.getValue().getC(),
                                now)
                                .getSeconds()) > TOKEN_LIFE_TIME
                );
    }
}
