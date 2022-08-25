package ru.magnit.magreportbackend.domain.filtertemplate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterTemplateFolderTest extends BaseEntityTest {

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
        var folder = getFolder();

        assertEquals(ID, folder.getId());
        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());
        assertEquals(NOW, folder.getCreatedDateTime());

        assertEquals(NOW.plusMinutes(2), folder.getModifiedDateTime());
        assertNotNull(folder.getParentFolder());
        assertNotNull(folder.getChildFolders());
        assertNotNull(folder.getFilterTemplates());
        assertNotNull(folder.getFolderRoles());

        var testFolder = new FilterTemplateFolder(-ID);
        assertEquals(-ID, testFolder.getId());
    }

    private static FilterTemplateFolder getFolder() {
        return new FilterTemplateFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setParentFolder(new FilterTemplateFolder())
                .setChildFolders(Collections.singletonList(new FilterTemplateFolder()))
                .setFilterTemplates(Collections.singletonList(new FilterTemplate()))
                .setFolderRoles(Collections.singletonList(new FilterTemplateFolderRole()));
    }
}