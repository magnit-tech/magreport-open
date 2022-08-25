package ru.magnit.magreportbackend.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileServiceTest {

    @InjectMocks
    private FileService service;

    private static final Long ID = -1L;
    private final String PATH = "~/magreport2.0/excel-templates/test";
    private static final String USER_HOME = "user.home";
    private static final String TEMPLATE_EXTENSION = ".xlsm";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "templatesPath", PATH);
    }


    @Test
    void storeFile() {

        MockMultipartFile file = new MockMultipartFile("data", "filename.xlsm", "text/plain", "1".getBytes());

        service.storeFile(ID, file);
        assertTrue(Files.exists(Path.of(getPath(),ID+TEMPLATE_EXTENSION)));

    }

    @Test
    void retrieveFile() throws IOException {

        byte[] expected = "File content".getBytes();
        Files.createDirectories(Path.of(getPath()));
        Path path = Path.of(getPath(), ID+TEMPLATE_EXTENSION);
        Files.write(path, expected);

        byte[] actual = service.retrieveFile(ID);

        assertArrayEquals(expected, actual);
    }

    @AfterAll
    void clear() {
        try {
            FileUtils.deleteDirectory(new File(getPath()));
        } catch (IOException ex) {
            fail("Ошибка при удалении тестовых файлов");
        }
    }

    private String getPath() {
        return PATH.replaceFirst("^~", System.getProperty(USER_HOME).replace("\\", "/"));

    }
}
