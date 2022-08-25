package ru.magnit.magreportbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FileService {

    private static final String ERROR_MESSAGE = "Error trying to save template file.";
    private static final String RETRIEVE_ERROR_MESSAGE = "Error trying to read template file.";
    private static final String TEMPLATE_EXTENSION = ".xlsm";
    private static final String USER_HOME = "user.home";


    @Value(value = "${magreport.excel-template.folder}")
    private String templatesPath;

    public void storeFile(Long fileId, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream())
        {
            Path destination  = Paths.get(getTemplateFolder(), fileId.toString() + TEMPLATE_EXTENSION);
            Files.createDirectories(destination.toAbsolutePath().getParent());
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error(ERROR_MESSAGE);
            throw new InvalidParametersException(ERROR_MESSAGE);
        }

    }

    public byte[] retrieveFile(Long fileId) {
        try {
            Path filePath  = Paths.get(getTemplateFolder(), fileId.toString() + TEMPLATE_EXTENSION);
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            log.error(RETRIEVE_ERROR_MESSAGE);
            throw new InvalidParametersException(RETRIEVE_ERROR_MESSAGE);
        }
    }

    private String getTemplateFolder() {
        return templatesPath.replaceFirst("^~", System.getProperty(USER_HOME).replace("\\", "/"));
    }
}
