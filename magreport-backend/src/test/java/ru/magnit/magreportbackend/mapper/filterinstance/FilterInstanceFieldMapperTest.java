package ru.magnit.magreportbackend.mapper.filterinstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceFieldAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class FilterInstanceFieldMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";

    @InjectMocks
    private FilterInstanceFieldMapper mapper;

    @Test
    void from() {
        FilterInstanceField field = mapper.from(getRequest());

        assertEquals(NAME, field.getName());
        assertEquals(DESCRIPTION, field.getDescription());
        assertNotNull(field.getDataSetField());
        assertEquals(ID, field.getDataSetField().getId());
        assertNotNull(field.getTemplateField());
        assertEquals(ID, field.getTemplateField().getId());

        List<FilterInstanceField> fields = mapper.from(Collections.singletonList(getRequest()));
        assertNotEquals(0, fields.size());
        field = fields.get(0);

        assertEquals(NAME, field.getName());
        assertEquals(DESCRIPTION, field.getDescription());
        assertNotNull(field.getDataSetField());
        assertEquals(ID, field.getDataSetField().getId());
        assertNotNull(field.getTemplateField());
        assertEquals(ID, field.getTemplateField().getId());
    }

    private FilterInstanceFieldAddRequest getRequest() {
        return new FilterInstanceFieldAddRequest()
            .setDataSetFieldId(ID)
            .setTemplateFieldId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}