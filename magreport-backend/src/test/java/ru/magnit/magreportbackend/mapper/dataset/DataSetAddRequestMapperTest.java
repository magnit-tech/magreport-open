package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class DataSetAddRequestMapperTest {

    private final Long DATA_SOURCE_ID = 1L;
    private final Long FOLDER_ID = 1L;
    private final Long TYPE_ID = 1L;
    private final String NAME = "name";
    private final String DESCRIPTION = "description";
    private final String CATALOG_NAME = "catalogName";
    private final String SCHEMA_NAME = "schemaName";
    private final String OBJECT_NAME = "objectName";

    @InjectMocks
    DataSetAddRequestMapper mapper;

    @Test
    void from() {
        DataSetAddRequest request = mapper.from(getCreateRequest());

        assertEquals(DATA_SOURCE_ID, request.getId());
        assertEquals(FOLDER_ID, request.getFolderId());
        assertEquals(TYPE_ID, request.getTypeId());
        assertEquals(NAME, request.getName());
        assertEquals(DESCRIPTION, request.getDescription());
        assertEquals(CATALOG_NAME, request.getCatalogName());
        assertEquals(SCHEMA_NAME, request.getSchemaName());
        assertEquals(OBJECT_NAME, request.getObjectName());

        List<DataSetAddRequest> requests = mapper.from(Collections.singletonList(getCreateRequest()));

        assertNotEquals(0, requests.size());
        request = requests.get(0);

        assertEquals(DATA_SOURCE_ID, request.getId());
        assertEquals(FOLDER_ID, request.getFolderId());
        assertEquals(TYPE_ID, request.getTypeId());
        assertEquals(NAME, request.getName());
        assertEquals(DESCRIPTION, request.getDescription());
        assertEquals(CATALOG_NAME, request.getCatalogName());
        assertEquals(SCHEMA_NAME, request.getSchemaName());
        assertEquals(OBJECT_NAME, request.getObjectName());
    }

    private DataSetCreateFromMetaDataRequest getCreateRequest() {
        return new DataSetCreateFromMetaDataRequest()
            .setDataSourceId(DATA_SOURCE_ID)
            .setFolderId(FOLDER_ID)
            .setTypeId(TYPE_ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCatalogName(CATALOG_NAME)
            .setSchemaName(SCHEMA_NAME)
            .setObjectName(OBJECT_NAME);
    }
}