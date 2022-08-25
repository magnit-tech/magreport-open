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
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetFieldView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSetViewMapperTest {

    @Mock
    private DataSetFieldViewMapper fieldViewMapper;

    @Mock
    private DataSourceViewMapper dataSourceViewMapper;

    @InjectMocks
    private DataSetViewMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String SCHEMA = "Schema";
    private final static String OBJECT = "Object";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Test
    void from() {

        when(fieldViewMapper.from(any(DataSetField.class))).thenReturn(new DataSetFieldView());
        when(dataSourceViewMapper.from(any(DataSource.class))).thenReturn(getDataSourceData());

        var response = mapper.from(getDataset());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(SCHEMA, response.getSchemaName());
        assertEquals(OBJECT, response.getObjectName());
        assertEquals(getDataSourceData(), response.getDataSource());
        assertEquals(1, response.getFields().size());

        verify(fieldViewMapper).from(any(DataSetField.class));
        verify(dataSourceViewMapper).from(any(DataSource.class));
        verifyNoMoreInteractions(fieldViewMapper, dataSourceViewMapper);
    }


    private DataSet getDataset() {
        return new DataSet()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setSchemaName(SCHEMA)
                .setObjectName(OBJECT)
                .setCatalogName("")
                .setType(new DataSetType().setId(ID))
                .setFolder(new DataSetFolder().setParentFolder(new DataSetFolder()))
                .setUser(new User())
                .setFields(Collections.singletonList(new DataSetField()))
                .setFilterInstances(Collections.emptyList())
                .setReports(Collections.emptyList())
                .setSecurityFilterDataSets(Collections.emptyList())
                .setExternalAuthSources(Collections.emptyList())
                .setDataSource(new DataSource())
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private DataSourceData getDataSourceData() {
        return new DataSourceData(ID, DataSourceTypeEnum.IMPALA, "url", "username", "******", (short) 5);
    }
}
