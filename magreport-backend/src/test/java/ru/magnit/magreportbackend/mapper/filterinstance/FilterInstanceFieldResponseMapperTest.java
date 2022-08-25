package ru.magnit.magreportbackend.mapper.filterinstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFieldResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class FilterInstanceFieldResponseMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterInstanceFieldResponseMapper mapper;

    @Test
    void from() {
        FilterInstanceFieldResponse response = mapper.from(getFilterInstanceField());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(ID, response.getTemplateFieldId());
        assertNull(response.getDataSetFieldId());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<FilterInstanceFieldResponse> responses = mapper.from(Collections.singletonList(getFilterInstanceField()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(ID, response.getTemplateFieldId());
        assertNull(response.getDataSetFieldId());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    private FilterInstanceField getFilterInstanceField() {
        FilterInstanceField field = new FilterInstanceField();
        field.setId(ID);
        field.setName(NAME);
        field.setDescription(DESCRIPTION);
        field.setTemplateField(new FilterTemplateField(ID).setType(new FilterFieldType(1L)));
        field.setDataSetField(null);
        field.setCreatedDateTime(CREATED_TIME);
        field.setModifiedDateTime(MODIFIED_TIME);
        return field;
    }
}