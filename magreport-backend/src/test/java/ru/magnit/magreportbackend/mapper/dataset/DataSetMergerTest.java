package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSetMergerTest {
    @Mock
    private DataSetFieldMerger dataSetFieldMerger;

    @InjectMocks
    private DataSetMerger merger;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String SCHEMA = "Schema";
    private final static String OBJECT = "Object";
    private final static String CATALOG = "CATALOG";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void merge() {
        when(dataSetFieldMerger.merge(anyList(), anyList())).thenReturn(Collections.singletonList(new DataSetField()));

        var response = merger.merge(getDataset(), getDataSetAddRequest());

        assertEquals(CATALOG, response.getCatalogName());
        assertEquals(SCHEMA, response.getSchemaName());
        assertEquals(OBJECT, response.getObjectName());
        assertEquals(new User(), response.getUser());
        assertEquals(NAME, response.getName());
        assertEquals(ID, response.getId());
        assertEquals(CREATE_TIME, response.getCreatedDateTime());
        assertEquals(MODIFIED_TIME, response.getModifiedDateTime());
        assertEquals(1, response.getFields().size());
        assertEquals(0, response.getExternalAuthSources().size());
        assertEquals(0, response.getFilterInstances().size());
        assertEquals(0, response.getReports().size());
        assertEquals(2L, response.getType().getId());
    }

    private DataSet getDataset() {
        return new DataSet()
                .setId(ID)
                .setName("")
                .setDescription("")
                .setSchemaName("")
                .setObjectName("")
                .setCatalogName("")
                .setType(new DataSetType().setId(ID))
                .setFolder(new DataSetFolder().setParentFolder(new DataSetFolder()))
                .setUser(new User())
                .setFields(Collections.emptyList())
                .setFilterInstances(Collections.emptyList())
                .setReports(Collections.emptyList())
                .setSecurityFilterDataSets(Collections.emptyList())
                .setExternalAuthSources(Collections.emptyList())
                .setDataSource(new DataSource())
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }


    private DataSetAddRequest getDataSetAddRequest() {
        return new DataSetAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setTypeId(2L)
                .setDataSourceId(ID)
                .setFolderId(ID)
                .setCatalogName(CATALOG)
                .setObjectName(OBJECT)
                .setSchemaName(SCHEMA)
                .setFields(Collections.singletonList(new DataSetFieldAddRequest()));
    }

}
