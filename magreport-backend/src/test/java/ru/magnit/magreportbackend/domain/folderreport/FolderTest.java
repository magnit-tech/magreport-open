package ru.magnit.magreportbackend.domain.folderreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FolderTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "getFolder";
    private static final String DESCRIPTION = "getFolder description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(9);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getFolder();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertEquals(NAME, testObject.getName());
        assertEquals(DESCRIPTION, testObject.getDescription());
        assertEquals(NOW, testObject.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), testObject.getModifiedDateTime());

        assertNotNull(testObject.getParentFolder());
        assertNotNull(testObject.getChildFolders());
        assertNotNull(testObject.getFolderReports());
        assertNotNull(testObject.getFolderRoles());

        assertEquals(-ID, testObject2.getId());
    }

    private static Folder getFolder() {
        return new Folder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setParentFolder(new Folder())
                .setChildFolders(Collections.singletonList(new Folder()))

                .setFolderReports(Collections.singletonList(new FolderReport()))
                .setFolderRoles(Collections.singletonList(new FolderRole()))
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2));
    }
}