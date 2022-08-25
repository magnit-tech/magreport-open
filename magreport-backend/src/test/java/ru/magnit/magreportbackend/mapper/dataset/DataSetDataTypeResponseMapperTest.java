package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataSetDataTypeResponseMapperTest {

    private final Long ID = 1L;
    private final String NAME = "INTEGER";
    private final String DESCRIPTION = "Integer data type";

    @InjectMocks
    private DataSetDataTypeResponseMapper mapper;

    @Test
    void from() {
        DataSetDataTypeResponse response = mapper.from(getDataSetDataType());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
    }

    private DataType getDataSetDataType() {
        return new DataType(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }
}