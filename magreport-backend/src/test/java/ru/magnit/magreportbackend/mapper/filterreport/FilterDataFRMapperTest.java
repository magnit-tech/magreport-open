package ru.magnit.magreportbackend.mapper.filterreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterDataFRMapperTest {
    @Mock
    private DataSourceViewMapper dataSourceViewMapper;

    @Mock
    private FilterFieldDataFRMapper filterFieldDataFRMapper;

    @InjectMocks
    private FilterDataFRMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String CODE = "Code";
    private final static Boolean HIDDEN = false;
    private final static Boolean MANDATORY = false;
    private final static Boolean ROOT = false;
    private final static Long ORDINAL = 1L;
    private final static String SCHEMA = "Schema";
    private final static String OBJECT = "Object";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void from() {

        when(dataSourceViewMapper.from(any(DataSource.class))).thenReturn(getDataSourceData());

        var response = mapper.from(getFilterReport());

        assertEquals(getDataSourceData(), response.dataSource());
        assertEquals(ID, response.filterId());
        assertEquals(FilterTypeEnum.RANGE, response.filterType());
        assertEquals(SCHEMA, response.schemaName());
        assertEquals(OBJECT, response.tableName());
        assertEquals(NAME, response.name());
        assertEquals(CODE, response.code());
        assertEquals(DESCRIPTION, response.description());


        verify(dataSourceViewMapper).from(any(DataSource.class));
        verify(filterFieldDataFRMapper).from(anyList());
        ;
    }

    private FilterReport getFilterReport() {
        return new FilterReport()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCode(CODE)
                .setHidden(HIDDEN)
                .setMandatory(MANDATORY)
                .setRootSelectable(ROOT)
                .setOrdinal(ORDINAL)
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME)
                .setFilterInstance(
                        new FilterInstance()
                                .setDataSet(
                                        new DataSet()
                                                .setSchemaName(SCHEMA)
                                                .setObjectName(OBJECT)
                                                .setDataSource(new DataSource())
                                )
                                .setFilterTemplate(
                                        new FilterTemplate()
                                                .setType(new FilterType().setId(ID)))
                )
                .setUser(new User())
                .setGroup(new FilterReportGroup())
                .setReportJobFilters(Collections.emptyList())
                .setFields(Collections.emptyList());
    }

    private DataSourceData getDataSourceData() {
        return new DataSourceData(ID, DataSourceTypeEnum.IMPALA, "url", "username", "******", (short) 5);
    }
}
