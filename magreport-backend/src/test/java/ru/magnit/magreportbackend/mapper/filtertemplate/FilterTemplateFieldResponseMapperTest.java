package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFieldResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum.ID_FIELD;

@ExtendWith(MockitoExtension.class)
class FilterTemplateFieldResponseMapperTest {

    private final long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final FilterFieldTypeEnum TYPE = ID_FIELD;
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterTemplateFieldResponseMapper mapper;

    @Test
    void from() {
        FilterTemplateFieldResponse response = mapper.from(getFilterTemplateField());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(TYPE, response.getType());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<FilterTemplateFieldResponse> responses = mapper.from(Collections.singletonList(getFilterTemplateField()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(TYPE, response.getType());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    private FilterTemplateField getFilterTemplateField() {
        FilterTemplateField field = new FilterTemplateField();
        field.setId(ID);
        field.setName(NAME);
        field.setDescription(DESCRIPTION);
        field.setType(new FilterFieldType((long) TYPE.ordinal()));
        field.setCreatedDateTime(CREATED_TIME);
        field.setModifiedDateTime(MODIFIED_TIME);
        return field;
    }
}