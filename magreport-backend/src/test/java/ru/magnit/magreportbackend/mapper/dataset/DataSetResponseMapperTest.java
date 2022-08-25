package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceResponseMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataSetResponseMapperTest {

    private final long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final String CATALOG_NAME = "catalog";
    private final String SCHEMA_NAME = "schema";
    private final String OBJECT_NAME = "object";
    private final long TYPE_ID = 1L;

    @InjectMocks
    private DataSetResponseMapper mapper;

    @Mock
    private DataSetFieldResponseMapper dataSetFieldResponseMapper;

    @Mock
    private DataSourceResponseMapper dataSourceResponseMapper;

    @Test
    void from() {
        when(dataSetFieldResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSetFieldResponse().setIsValid(true)));
        when(dataSourceResponseMapper.from(any(DataSource.class))).thenReturn(new DataSourceResponse());

        DataSetResponse response = mapper.from(getDataSet());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CATALOG_NAME, response.getCatalogName());
        assertEquals(SCHEMA_NAME, response.getSchemaName());
        assertEquals(OBJECT_NAME, response.getObjectName());
        assertEquals(TYPE_ID, response.getTypeId());
        assertNotNull(response.getFields());

        List<DataSetResponse> responses = mapper.from(Collections.singletonList(getDataSet()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CATALOG_NAME, response.getCatalogName());
        assertEquals(SCHEMA_NAME, response.getSchemaName());
        assertEquals(OBJECT_NAME, response.getObjectName());
        assertEquals(TYPE_ID, response.getTypeId());
        assertNotNull(response.getFields());
    }

    private DataSet getDataSet() {
        return new DataSet()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(OBJECT_NAME)
                .setDataSource(new DataSource(ID))
                .setType(new DataSetType(TYPE_ID))
                .setUser(new User())
                .setFields(Collections.singletonList(new DataSetField().setIsSync(true)));
    }
}