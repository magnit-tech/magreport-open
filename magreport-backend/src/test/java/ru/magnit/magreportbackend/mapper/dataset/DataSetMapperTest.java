package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataSetMapperTest {

    private final long DATASOURCE_ID = 1L;
    private final long FOLDER_ID = 2L;
    private final long TYPE_ID = 1L;
    private final long ID = 1L;
    private final String NAME = "Test folder";
    private final String RENAME = "rename";
    private final String DESCRIPTION = "Folder description";
    private final String CATALOG_NAME = "catalog";
    private final String SCHEMA_NAME = "schema";
    private final String OBJECT_NAME = "object";


    @InjectMocks
    private DataSetMapper mapper;

    @Mock
    private DataSetFieldMapper dataSetFieldMapper;

    @Test
    void from() {
        when(dataSetFieldMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSetField()));

        DataSet dataSet = mapper.from(getRequest());

        assertEquals(NAME, dataSet.getName());
        assertEquals(DESCRIPTION, dataSet.getDescription());
        assertEquals(CATALOG_NAME, dataSet.getCatalogName());
        assertEquals(SCHEMA_NAME, dataSet.getSchemaName());
        assertEquals(OBJECT_NAME, dataSet.getObjectName());
        assertNotNull(dataSet.getDataSource());
        assertEquals(DATASOURCE_ID, dataSet.getDataSource().getId());
        assertNotNull(dataSet.getType());
        assertEquals(TYPE_ID, dataSet.getType().getId());
        assertNotNull(dataSet.getFolder());
        assertEquals(FOLDER_ID, dataSet.getFolder().getId());
        assertNotNull(dataSet.getFields());

        List<DataSet> dataSets = mapper.from(Collections.singletonList(getRequest()));
        assertNotEquals(0, dataSets.size());
        dataSet = dataSets.get(0);

        assertEquals(NAME, dataSet.getName());
        assertEquals(DESCRIPTION, dataSet.getDescription());
        assertEquals(CATALOG_NAME, dataSet.getCatalogName());
        assertEquals(SCHEMA_NAME, dataSet.getSchemaName());
        assertEquals(OBJECT_NAME, dataSet.getObjectName());
        assertNotNull(dataSet.getDataSource());
        assertEquals(DATASOURCE_ID, dataSet.getDataSource().getId());
        assertNotNull(dataSet.getType());
        assertEquals(TYPE_ID, dataSet.getType().getId());
        assertNotNull(dataSet.getFolder());
        assertEquals(FOLDER_ID, dataSet.getFolder().getId());
        assertNotNull(dataSet.getFields());
    }

    private DataSetAddRequest getRequest() {
        return new DataSetAddRequest()
                .setId(ID)
                .setDataSourceId(DATASOURCE_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFolderId(FOLDER_ID)
                .setTypeId(TYPE_ID)
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(OBJECT_NAME);
    }
}