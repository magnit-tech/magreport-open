package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class DataSetFieldResponseMapperTest {

    private final Long ID = 1L;
    private final Long DATA_TYPE = 2L;
    private final String NAME = "name";
    private final String DESCRIPTION = "description";

    @InjectMocks
    private DataSetFieldResponseMapper mapper;

    @Test
    void from() {
        DataSetFieldResponse response = mapper.from(getDataSetField());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(DATA_TYPE, response.getTypeId());

        List<DataSetFieldResponse> responses = mapper.from(Collections.singletonList(getDataSetField()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(DATA_TYPE, response.getTypeId());
    }

    private DataSetField getDataSetField() {
        return new DataSetField()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setType(new DataType(DATA_TYPE));
    }
}