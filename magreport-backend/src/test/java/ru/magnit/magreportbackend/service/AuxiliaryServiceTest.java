package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.folder.FolderPathRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.DataSourceDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuxiliaryServiceTest {

    private final static Long ID = 0L;

    @Mock
    private ReportDomainService reportDomainService;
    @Mock
    private FolderDomainService folderDomainService;
    @Mock
    private DataSourceDomainService dataSourceDomainService;
    @Mock
    private DataSetDomainService dataSetDomainService;
    @Mock
    private ExcelTemplateDomainService excelTemplateDomainService;
    @Mock
    private FilterInstanceDomainService filterInstanceDomainService;
    @Mock
    private FilterTemplateDomainService filterTemplateDomainService;
    @Mock
    private SecurityFilterDomainService securityFilterDomainService;

    @InjectMocks
    private AuxiliaryService service;


    @Test
    void getFolderPath() {

        when(folderDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.PUBLISHED_REPORT));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(folderDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(folderDomainService);
        verifyNoInteractions(
                reportDomainService, dataSourceDomainService, dataSetDomainService,
                excelTemplateDomainService, filterInstanceDomainService, filterTemplateDomainService,
                securityFilterDomainService);
    }

    @Test
    void getReportFolderPath() {

        when(reportDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.REPORT_FOLDER));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(reportDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(reportDomainService);
        verifyNoInteractions(
                folderDomainService, dataSourceDomainService, dataSetDomainService,
                excelTemplateDomainService, filterInstanceDomainService, filterTemplateDomainService,
                securityFilterDomainService);
    }

    @Test
    void getDataSourceFolderPath() {

        when(dataSourceDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.DATASOURCE));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(dataSourceDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(dataSourceDomainService);
        verifyNoInteractions(
                folderDomainService, reportDomainService, dataSetDomainService,
                excelTemplateDomainService, filterInstanceDomainService, filterTemplateDomainService,
                securityFilterDomainService);
    }

    @Test
    void getDataSetFolderPath() {

        when(dataSetDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.DATASET));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(dataSetDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(dataSetDomainService);
        verifyNoInteractions(
                folderDomainService, reportDomainService, dataSourceDomainService,
                excelTemplateDomainService, filterInstanceDomainService, filterTemplateDomainService,
                securityFilterDomainService);
    }

    @Test
    void getExcelTemplateFolderPath() {

        when(excelTemplateDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.EXCEL_TEMPLATE));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(excelTemplateDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(excelTemplateDomainService);
        verifyNoInteractions(
                folderDomainService, reportDomainService, dataSourceDomainService,
                dataSetDomainService, filterInstanceDomainService, filterTemplateDomainService,
                securityFilterDomainService);
    }

    @Test
    void getFilterInstanceFolderPath() {

        when(filterInstanceDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.FILTER_INSTANCE));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(filterInstanceDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(filterInstanceDomainService);
        verifyNoInteractions(
                folderDomainService, reportDomainService, dataSourceDomainService,
                dataSetDomainService, excelTemplateDomainService, filterTemplateDomainService,
                securityFilterDomainService);
    }

    @Test
    void getFilterTemplateFolderPath() {

        when(filterTemplateDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.FILTER_TEMPLATE));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(filterTemplateDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(filterTemplateDomainService);
        verifyNoInteractions(
                folderDomainService, reportDomainService, dataSourceDomainService,
                dataSetDomainService, excelTemplateDomainService, filterInstanceDomainService,
                securityFilterDomainService);
    }

    @Test
    void getSecurityFilterFolderPath() {

        when(securityFilterDomainService.getPathToFolder(anyLong())).thenReturn(getPathToFolder());

        var response = service.getFolderPath(getFolderPathRequest(FolderTypes.SECURITY_FILTER));

        assertNotNull(response);
        assertEquals(1, response.path().size());

        verify(securityFilterDomainService).getPathToFolder(anyLong());
        verifyNoMoreInteractions(securityFilterDomainService);
        verifyNoInteractions(
                folderDomainService, reportDomainService, dataSourceDomainService,
                dataSetDomainService, excelTemplateDomainService, filterInstanceDomainService,
                filterTemplateDomainService);
    }


    private FolderPathRequest getFolderPathRequest(FolderTypes type) {
        return new FolderPathRequest()
                .setId(ID)
                .setFolderType(type);
    }

    private List<FolderNodeResponse> getPathToFolder() {
        return Collections.singletonList(
                new FolderNodeResponse(
                        0L, 1L, "NAME", "DESCRIPTION", LocalDateTime.now(), LocalDateTime.now()
                ));

    }

}
