package ru.magnit.magreportbackend.domain.folderreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FolderReportTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getFolderReport();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertEquals(NOW, testObject.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), testObject.getModifiedDateTime());
        assertNotNull(testObject.getFolder());
        assertNotNull(testObject.getReport());
        assertNotNull(testObject.getUser());

        assertEquals(-ID, testObject2.getId());
    }

    private static FolderReport getFolderReport() {
        return new FolderReport()
                .setId(ID)
                .setFolder(new Folder())
                .setReport(new Report())
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setUser(new User());
    }
}