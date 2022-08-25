package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateFolderMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";

    @InjectMocks
    private ExcelTemplateFolderMapper mapper;

    @Test
    void from() {
        ExcelTemplateFolder folder = mapper.from(getFolderAddRequest());

        assertEquals(ID, folder.getParentFolder().getId());
        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());

        List<ExcelTemplateFolder> folders = mapper.from(Collections.singletonList(getFolderAddRequest()));
        assertNotEquals(0, folders.size());

        folder = folders.get(0);

        assertEquals(ID, folder.getParentFolder().getId());
        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
            .setParentId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}