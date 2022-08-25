package ru.magnit.magreportbackend.service.jobengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Slf4j
@Service
@Profile("!prod")
public class RmsStub {

    @Value("${magreport.reports.rms-in-folder}")
    private String rmsInFolder;

    @Value("${magreport.reports.rms-out-folder}")
    private String rmsOutFolder;


    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
    private void processFiles() {

        final var inputFolder = Paths.get(FileUtils.replaceHomeShortcut(rmsInFolder));
        final var outputFolder = Paths.get(FileUtils.replaceHomeShortcut(rmsOutFolder));

        try (var paths = Files.newDirectoryStream(inputFolder)) {
            paths.forEach(path -> {
                final var outPath = Paths.get(outputFolder.toString(), path.getFileName().toString());
                if (!Files.isReadable(outPath)) {
                    try {
                        log.debug("RMS Moving file: " + path);
                        Files.move(path, outPath);
                        log.debug("RMS Moved file to: " + outPath);
                    } catch (IOException ex) {
                        log.error("Error copying files in rms stub", ex);
                    }
                }
            });
        } catch (IOException ex) {
            log.error("Error copying files in rms stub", ex);
        }
    }

}
