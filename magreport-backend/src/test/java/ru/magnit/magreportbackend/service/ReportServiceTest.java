package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterFieldData;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddFavoritesRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.permission.ReportFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFieldResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportJobFilterResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.repository.ReportFolderRepository;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.JobDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.ScheduleTaskDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private ReportService service;

    @Mock
    private ReportDomainService domainService;

    @Mock
    private DataSetDomainService dataSetDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private JobDomainService jobDomainService;

    @Mock
    private FilterReportDomainService filterReportDomainService;

    @Mock
    private FilterReportService filterReportService;

    @Mock
    private FolderEntitySearchDomainService folderEntitySearchDomainService;

    @Mock
    private ReportFolderRepository repository;

    @Mock
    private ReportResponseMapper mapper;

    @Mock
    private FilterQueryExecutor filterQueryExecutor;

    @Mock
    private ExcelTemplateDomainService excelTemplateDomainService;

    @Mock
    private ScheduleTaskDomainService scheduleTaskDomainService;


    @Test
    void getFolder() {
        when(domainService.getFolder(any(), any())).thenReturn(getFolderResponse());
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.singletonList(new RoleView().setId(0L)));
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));
        when(folderPermissionsDomainService.getReportFolderPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.emptyList());

        ReportFolderResponse response = service.getFolder(new FolderRequest(ID));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        Mockito.reset(userDomainService);

        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));

        response = service.getFolder(new FolderRequest(ID));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        Mockito.reset(domainService);
        when(domainService.getFolder(any(), any())).thenReturn(getFolderResponse().setParentId(ID).setAuthority(FolderAuthorityEnum.NONE));

        response = service.getFolder(new FolderRequest(ID));

        assertNull(response);

        verify(domainService).getFolder(any(), any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addFolder() {
        when(domainService.addFolder(any())).thenReturn(getFolderResponse());
        when(folderPermissionsDomainService.getReportFolderPermissions(any())).thenReturn(new ReportFolderPermissionsResponse(new ReportFolderResponse().setId(ID), Collections.singletonList(new RolePermissionResponse(new RoleResponse(), Collections.singletonList(FolderAuthorityEnum.WRITE)))));
        when(domainService.getFolder(any(), any())).thenReturn(getFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));

        ReportFolderResponse response = service.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        response = service.addFolder(getFolderAddRequest().setParentId(null));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService, times(2)).addFolder(any());
        verify(userDomainService, times(2)).getCurrentUser();
        verifyNoMoreInteractions(domainService);
        verifyNoMoreInteractions(userDomainService);
    }

    @Test
    void getChildFolders() {
        when(domainService.getChildFolders(any())).thenReturn(Collections.singletonList(getFolderResponse()));

        List<ReportFolderResponse> responses = service.getChildFolders(new FolderRequest().setId(ID));
        assertNotNull(responses);

        ReportFolderResponse response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).getChildFolders(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void renameFolder() {
        when(domainService.renameFolder(any())).thenReturn(getFolderResponse());

        ReportFolderResponse response = service.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).renameFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void deleteFolder() {
        service.deleteFolder(new FolderRequest().setId(ID));

        verify(domainService).deleteFolder(anyLong());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addReport() {
        when(dataSetDomainService.getDataSet(anyLong())).thenReturn(new DataSetResponse().setIsValid(true));
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));
        when(domainService.addReport(any(), any())).thenReturn(ID);
        when(domainService.getReport(ID)).thenReturn(getReportResponse());

        var response = service.addReport(getReportAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFields());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        Mockito.reset(dataSetDomainService);
        when(dataSetDomainService.getDataSet(anyLong())).thenReturn(new DataSetResponse().setIsValid(false));
        var request = getReportAddRequest();
        assertThrows(InvalidParametersException.class, () -> service.addReport(request));

        verify(domainService).addReport(any(), any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void deleteReport() {
        when(scheduleTaskDomainService.findScheduleTaskByReport(anyLong())).thenReturn(Collections.emptyList());
        service.deleteReport(new ReportRequest().setId(ID));

        verify(domainService).deleteReport(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addReportToFavorites() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView());

        service.addReportToFavorites(new ReportAddFavoritesRequest());

        verify(userDomainService).getCurrentUser();
        verify(domainService).addReportToFavorites(any(), any());

        verifyNoMoreInteractions(userDomainService, domainService);
    }

    @Test
    void getReport() {

        when(domainService.getReport(anyLong())).thenReturn(getReportResponse());
        when(jobDomainService.getLastJobParameters(anyLong(), anyLong())).thenReturn(getReportJobFilterResponse());
        when(jobDomainService.getJobParameters(anyLong())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));
        when(filterReportDomainService.getFilterReportData(anyLong())).thenReturn(getFilterData());
        when(filterQueryExecutor.getFieldsValues(any())).thenReturn(new ArrayList<>());

        var response = service.getReport(new ReportRequest().setId(ID));
        assertNotNull(response);
        verify(jobDomainService).getLastJobParameters(anyLong(), anyLong());

        response = service.getReport(new ReportRequest().setId(ID).setJobId(ID));
        assertNotNull(response);
        verify(jobDomainService).getJobParameters(anyLong());

        Mockito.reset(jobDomainService);
        when(jobDomainService.getLastJobParameters(anyLong(), anyLong())).thenThrow(NullPointerException.class);

        response = service.getReport(new ReportRequest().setId(ID));
        assertNotNull(response);

        verify(userDomainService, times(2)).getCurrentUser();
        verify(domainService, times(3)).getReport(anyLong());
        verify(jobDomainService).getLastJobParameters(anyLong(), anyLong());
        verifyNoMoreInteractions(domainService, jobDomainService, userDomainService);
    }

    @Test
    void editReport() {
        var request = getReportEditRequestLondDescription();
        assertThrows(InvalidParametersException.class, () ->
                service.editReport((request)));

        when(domainService.getDeletedFields(any())).thenReturn(Collections.emptyList());
        when(domainService.getReport(anyLong())).thenReturn(getReportResponse());

        var response = service.editReport(getReportEditRequest());
        assertNotNull(response);

        response = service.editReport(getReportEditRequest().setFilterGroup(null));
        assertNotNull(response);

        verify(filterReportDomainService, times(2)).removeFilters(anyLong());
        verify(domainService, times(2)).getDeletedFields(any());
        verify(domainService, times(2)).deleteFields(anyList());
        verify(domainService, times(2)).editReport(any());
        verify(filterReportService).addFilters(any());
        verify(domainService, times(2)).getReport(anyLong());

        verifyNoMoreInteractions(domainService, filterReportDomainService, filterReportService);

    }

    @Test
    void getPivotFieldTypes() {

        when(domainService.getPivotFieldTypes()).thenReturn(Collections.emptyList());

        var response = service.getPivotFieldTypes();
        assertNotNull(response);

        verify(domainService).getPivotFieldTypes();
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void searchFolder() {
        when(folderEntitySearchDomainService.search(any(), any(), any(), any())).thenReturn(new FolderSearchResponse(Collections.emptyList(), Collections.emptyList()));

        var response = service.searchFolder(new FolderSearchRequest());
        assertNotNull(response);

        verify(folderEntitySearchDomainService).search(any(), any(), any(), any());
        verifyNoMoreInteractions(folderEntitySearchDomainService);
    }

    @Test
    void changeParentFolder() {

        when(domainService.changeParentFolder(any())).thenReturn(new ReportFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));

        var response = service.changeParentFolder(new FolderChangeParentRequest());
        assertNotNull(response);

        verify(domainService).changeParentFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getFavReports() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        when(domainService.getFavReports(any())).thenReturn(new FolderResponse().setReports(Collections.emptyList()));

        var response = service.getFavReports();
        assertNotNull(response);

        verify(userDomainService).getCurrentUser();
        verify(domainService).getFavReports(any());
        verifyNoMoreInteractions(userDomainService, domainService);
    }

    @Test
    void deleteReportToFavorites() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        service.deleteReportToFavorites(new ReportIdRequest());

        verify(userDomainService).getCurrentUser();
        verify(domainService).deleteReportToFavorites(any(), any());
        verifyNoMoreInteractions(userDomainService, domainService);
    }

    private ReportFolderResponse getFolderResponse() {
        return new ReportFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(List.of(new ReportFolderResponse())))
                .setReports(Collections.singletonList(new ReportResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private ReportResponse getReportResponse() {
        return new ReportResponse()
                .setId(ID)
                .setDataSetId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFields(Collections.singletonList(new ReportFieldResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private ReportAddRequest getReportAddRequest() {
        return new ReportAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setDataSetId(ID)
                .setFolderId(ID);
    }

    private ReportEditRequest getReportEditRequest() {
        return new ReportEditRequest()
                .setId(ID)
                .setName("name")
                .setDescription("description")
                .setRequirementsLink("url")
                .setFilterGroup(new FilterGroupAddRequest());
    }

    private ReportEditRequest getReportEditRequestLondDescription() {
        return new ReportEditRequest()
                .setId(ID)
                .setName("name")
                .setDescription("**************************************************" +
                                "**************************************************" +
                                "**************************************************" +
                                "**************************************************" +
                                "**************************************************" +
                                "**************************************************")
                .setRequirementsLink("url")
                .setFilterGroup(new FilterGroupAddRequest());
    }

    private List<ReportJobFilterResponse> getReportJobFilterResponse() {
        return Arrays.asList(
                new ReportJobFilterResponse()
                        .setFilterId(0L)
                        .setFilterType(FilterTypeEnum.TOKEN_INPUT)
                        .setParameters(Collections.emptyList()),
                new ReportJobFilterResponse()
                        .setFilterId(1L)
                        .setFilterType(FilterTypeEnum.VALUE_LIST)
                        .setParameters(Collections.emptyList()),
                new ReportJobFilterResponse()
                        .setFilterId(2L)
                        .setFilterType(FilterTypeEnum.HIERARCHY)
                        .setParameters(Collections.emptyList())
        );
    }

    private FilterData getFilterData() {
        return new FilterData(
                null,
                0L,
                FilterTypeEnum.VALUE_LIST,
                null,
                null,
                null,
                null,
                null,
                Collections.singletonList(
                        new FilterFieldData(
                                0L,
                                1,
                                "Name",
                                "Description",
                                "FieldName",
                                null,
                                FilterFieldTypeEnum.CODE_FIELD
                        )));
    }
}