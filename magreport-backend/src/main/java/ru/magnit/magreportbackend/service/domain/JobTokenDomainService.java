package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.magnit.magreportbackend.domain.reportjob.JobToken;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.repository.JobTokenRepository;
import ru.magnit.magreportbackend.util.Pair;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobTokenDomainService {

    private final byte[] tokenSymbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes(StandardCharsets.UTF_8);
    private static final int TOKEN_LENGTH = 8;

    private final JobTokenRepository repository;

    @Value("${magreport.jobengine.job-retention-time}")
    private Long jobRetentionTime;

    @Transactional
    public String createJobToken(Long jobId, Long templateId, String email) {

        String token;
        do {
            token = generateToken();
        } while (repository.findById(token).isPresent());

        var jobToken = new JobToken(token, jobId, templateId, email);
        repository.save(jobToken);
        return token;
    }

    @Transactional
    public Pair<Long, Long> getAssociatedValue(String token) {
        var jobToken = repository.findById(token);

        if (jobToken.isPresent()) {
            var result = jobToken.get();
            log.debug("User with email: " + result.getEmail() + " get file report with jobId: " + result.getReportJobId());
            return new Pair<Long, Long>().setL(result.getReportJobId()).setR(result.getExcelTemplateId());
        } else
            throw new InvalidParametersException("Token: '" + token + "' does not exists");
    }

    @Transactional
    public void clearOldToken() {
        var oldTokens = repository.findAllByCreatedBefore(LocalDateTime.now().minusHours(jobRetentionTime));
        repository.deleteAll(oldTokens);
    }

    private String generateToken() {
        final var result = new StringBuilder(TOKEN_LENGTH);

        ThreadLocalRandom
                .current()
                .ints(TOKEN_LENGTH, 0, tokenSymbols.length)
                .map(index -> tokenSymbols[index])
                .forEach(letter -> result.append((char) letter));

        return result.toString();
    }
}
