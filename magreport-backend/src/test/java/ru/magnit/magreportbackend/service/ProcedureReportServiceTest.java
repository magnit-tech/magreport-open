package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceObjectResponse;
import ru.magnit.magreportbackend.service.domain.TemplateParserService;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcedureReportServiceTest {

    @InjectMocks
    private ProcedureReportService service;

    @Mock
    private MetaDataService metaDataService;

    @Mock
    private TemplateParserService templateParserService;

    @Mock
    private FilterQueryExecutor filterQueryExecutor;

    private final String SCHEMA = "SCHEMA";


    @Test
    void checkProcedureReportSchemaMetaData() {
        when(metaDataService.getSchemaObjects(any(), any(), any(), any())).thenReturn(getDataSourceObjectResponses());
        ReflectionTestUtils.setField(service, "tables", new String[]{"1", "2"});

        var result = service.checkProcedureReportSchemaMetaData(getDataSourceData(), SCHEMA);
        assertTrue(result);

        verify(metaDataService).getSchemaObjects(any(), any(), any(), any());

        verifyNoMoreInteractions(metaDataService);
    }

    @Test
    void createProcedureReportMetaData() {

        when(metaDataService.getSchemaObjects(any(), any(), any(), any())).thenReturn(getDataSourceObjectResponses());
        ReflectionTestUtils.setField(service, "tables", new String[]{"1", "2", "5"});

        service.createProcedureReportMetaData(getDataSourceData(), SCHEMA);

        verify(metaDataService).getSchemaObjects(any(), any(), any(), any());
        verify(filterQueryExecutor).executeSql(any(), any());
        verify(templateParserService).parseTemplate(any(), any());

        verifyNoMoreInteractions(metaDataService, filterQueryExecutor, templateParserService);
    }

    private DataSourceData getDataSourceData() {
        return new DataSourceData(
                0L,
                DataSourceTypeEnum.TERADATA,
                "url",
                "user",
                "pwd",
                (short) 5
        );
    }

    private List<DataSourceObjectResponse> getDataSourceObjectResponses() {
        return Arrays.asList(
                new DataSourceObjectResponse()
                        .setName("1"),
                new DataSourceObjectResponse()
                        .setName("2"),
                new DataSourceObjectResponse()
                        .setName("3"),
                new DataSourceObjectResponse()
                        .setName("4"));
    }
}
