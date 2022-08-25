package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateFolderResponseMapperTest {

    private static final long PARENT_ID = 2L;
    private static final Long ID = 1L;
    private static final String NAME = "Test folder";
    private static final String DESCRIPTION = "Folder description";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private ExcelTemplateFolderResponseMapper mapper;

    @Mock
    private ExcelTemplateResponseMapper excelTemplateResponseMapper;

    @Test
    void from() {
        when(excelTemplateResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new ExcelTemplateResponse()));

        ExcelTemplateFolderResponse response = mapper.from(getExcelTemplateFolder());

        assertEquals(ID, response.getId());
        assertEquals(PARENT_ID, response.getParentId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<ExcelTemplateFolderResponse> responses = mapper.from(Collections.singletonList(getExcelTemplateFolder()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    @Test
    void shallowMap() {

        var response = mapper.shallowMap(getExcelTemplateFolder());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

    }

    private ExcelTemplateFolder getExcelTemplateFolder() {

        return new ExcelTemplateFolder()
                .setId(ID)
                .setParentFolder(new ExcelTemplateFolder(PARENT_ID))
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.emptyList())
                .setExcelTemplates(Collections.emptyList())
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}