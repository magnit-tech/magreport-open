package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";

    @InjectMocks
    private ExcelTemplateMapper mapper;

    @Test
    void from() {
        ExcelTemplate template = mapper.from(getExcelTemplateAddRequest());

        assertEquals(NAME, template.getName());
        assertEquals(DESCRIPTION, template.getDescription());
        assertNotNull(template.getFolder());
        assertEquals(ID, template.getFolder().getId());

        List<ExcelTemplate> templates = mapper.from(Collections.singletonList(getExcelTemplateAddRequest()));
        assertNotEquals(0, templates.size());
        template = templates.get(0);

        assertEquals(NAME, template.getName());
        assertEquals(DESCRIPTION, template.getDescription());
        assertNotNull(template.getFolder());
        assertEquals(ID, template.getFolder().getId());
    }

    private ExcelTemplateAddRequest getExcelTemplateAddRequest() {
        return new ExcelTemplateAddRequest()
            .setFolderId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}