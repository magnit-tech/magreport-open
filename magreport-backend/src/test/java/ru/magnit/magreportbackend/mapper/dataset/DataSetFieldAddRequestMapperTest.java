package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;

import java.sql.JDBCType;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class DataSetFieldAddRequestMapperTest {

    private final Integer DATA_TYPE = 1;
    private final String NAME = "name";
    private final String DESCRIPTION = "description";

    @InjectMocks
    private DataSetFieldAddRequestMapper mapper;

    @Test
    void from() {
        DataSetFieldAddRequest request = mapper.from(getResponse());

        assertEquals(NAME, request.getName());
        assertEquals(DESCRIPTION, request.getDescription());
        assertEquals(DataTypeEnum.valueOf(JDBCType.valueOf(DATA_TYPE)).ordinal(), request.getTypeId());

        List<DataSetFieldAddRequest> requests = mapper.from(Collections.singletonList(getResponse()));
        assertNotEquals(0, requests.size());
        request = requests.get(0);

        assertEquals(NAME, request.getName());
        assertEquals(DESCRIPTION, request.getDescription());
        assertEquals(DataTypeEnum.valueOf(JDBCType.valueOf(DATA_TYPE)).ordinal(), request.getTypeId());
    }

    private ObjectFieldResponse getResponse() {
        return new ObjectFieldResponse()
            .setFieldName(NAME)
            .setRemarks(DESCRIPTION)
            .setDataType(DATA_TYPE);
    }
}