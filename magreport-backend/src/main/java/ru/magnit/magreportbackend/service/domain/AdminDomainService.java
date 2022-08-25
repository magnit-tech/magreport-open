package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.exception.FileSystemException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDomainService {

    @Value("${logging.magreport.file.name}")
    private String mainLogPath;

    @Value("${logging.olap.file.name}")
    private String olapLogPath;

    public byte[] getMainLog() {
        return getActiveLog(mainLogPath);
    }

    public byte[] getOlapLog() {
        return getActiveLog(olapLogPath);
    }


    private byte[] getActiveLog(String logPath) {

        try (
                InputStream in = Files.newInputStream(Paths.get(logPath))
        ) {
            log.debug("Log path: " + logPath);
            return IOUtils.toByteArray(in);
        } catch (IOException ex) {
            log.error("Error trying to get log file", ex);
            throw new FileSystemException("Error trying to get log file", ex);
        }
    }
}
