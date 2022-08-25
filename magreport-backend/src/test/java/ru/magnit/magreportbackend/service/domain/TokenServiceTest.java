package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.util.Pair;
import ru.magnit.magreportbackend.util.Triple;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    TokenService service;

    @Test
    void getToken() {
        assertEquals(8, service.getToken(0L, 1L).length());
    }

    @Test
    void getAssociatedValue() {
        assertThrows(InvalidParametersException.class, () -> service.getAssociatedValue(""));

        Map<String, Triple<Long, Long, LocalDateTime>> tokenCache = new ConcurrentHashMap<>();
        tokenCache.put("", new Triple<>(1L, 1L, LocalDateTime.now()));

        ReflectionTestUtils.setField(service, "tokenCache", tokenCache);

        assertEquals(new Pair<>(1L, 1L), service.getAssociatedValue(""));

    }

    @Test
    void clearOutdatedTokens() {
        Map<String, Triple<Long, Long, LocalDateTime>> tokenCache = new ConcurrentHashMap<>();
        tokenCache.put("", new Triple<>(1L, 1L, LocalDateTime.now().minusDays(50)));
        ReflectionTestUtils.setField(service, "tokenCache", tokenCache);

        service.clearOutdatedTokens();
        var result = (ConcurrentHashMap) ReflectionTestUtils.getField(service, "tokenCache");
        assert result != null;
        assertTrue(result.isEmpty());
    }

}
