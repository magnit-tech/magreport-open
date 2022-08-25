package ru.magnit.magreportbackend.domain.report;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportFolderTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "getReportFolder";
    private static final String DESCRIPTION = "getReportFolder description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(9);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getReportFolder();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertEquals(NAME, testObject.getName());
        assertEquals(DESCRIPTION, testObject.getDescription());
        assertEquals(NOW, testObject.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), testObject.getModifiedDateTime());

        assertNotNull(testObject.getParentFolder());
        assertNotNull(testObject.getChildFolders());
        assertNotNull(testObject.getReports());
        assertNotNull(testObject.getFolderRoles());

        assertEquals(-ID, testObject2.getId());
    }

    private static ReportFolder getReportFolder() {
        return new ReportFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setParentFolder(new ReportFolder())
                .setChildFolders(Collections.singletonList(new ReportFolder()))

                .setReports(Collections.singletonList(new Report()))
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setFolderRoles(Collections.singletonList(new ReportFolderRole()));
    }
}