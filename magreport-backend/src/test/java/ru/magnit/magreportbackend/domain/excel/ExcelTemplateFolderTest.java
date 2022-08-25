package ru.magnit.magreportbackend.domain.excel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExcelTemplateFolderTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test folder";
    private static final String DESCRIPTION = "Folder description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(9);
    }

    @Test
    void testNoArgsConstructor() {
        var folder = getExcelTemplateFolder();

        assertEquals(ID, folder.getId());
        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());
        assertEquals(NOW, folder.getCreatedDateTime());

        assertEquals(NOW.plusMinutes(2), folder.getModifiedDateTime());
        assertNotNull(folder.getParentFolder());
        assertNotNull(folder.getChildFolders());
        assertNotNull(folder.getExcelTemplates());
        assertNotNull(folder.getFolderRoles());

        var testFolder = new ExcelTemplateFolder(-ID);
        assertEquals(-ID, testFolder.getId());
    }

    private static ExcelTemplateFolder getExcelTemplateFolder() {
        return new ExcelTemplateFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setParentFolder(new ExcelTemplateFolder())
                .setChildFolders(Collections.singletonList(new ExcelTemplateFolder()))
                .setExcelTemplates(Collections.singletonList(new ExcelTemplate()))
                .setFolderRoles(Collections.singletonList(new ExcelTemplateFolderRole()));
    }
}