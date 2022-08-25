package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataSetFieldMapperTest {

    private final Long DATA_TYPE = 1L;
    private final String NAME = "name";
    private final String DESCRIPTION = "description";
    private final boolean IS_SYNC = true;

    @InjectMocks
    private DataSetFieldMapper mapper;

    @Test
    void from() {
        DataSetField field = mapper.from(getRequest());

        assertEquals(NAME, field.getName());
        assertEquals(DESCRIPTION, field.getDescription());
        assertEquals(IS_SYNC, field.getIsSync());
        assertNotNull(field.getType());
        assertEquals(DATA_TYPE, field.getType().getId());

        List<DataSetField> fields = mapper.from(Collections.singletonList(getRequest()));
        assertNotEquals(0, fields.size());
        field = fields.get(0);

        assertEquals(NAME, field.getName());
        assertEquals(DESCRIPTION, field.getDescription());
        assertEquals(IS_SYNC, field.getIsSync());
        assertNotNull(field.getType());
        assertEquals(DATA_TYPE, field.getType().getId());
    }

    private DataSetFieldAddRequest getRequest() {
        return new DataSetFieldAddRequest()
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setTypeId(DATA_TYPE)
            .setIsValid(IS_SYNC);
    }
}