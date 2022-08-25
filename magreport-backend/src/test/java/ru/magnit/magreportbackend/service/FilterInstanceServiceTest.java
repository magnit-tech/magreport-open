package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFieldResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceValuesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterInstanceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceResponseMapper;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRepository;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.FilterTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import javax.lang.model.element.Name;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInstanceServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterInstanceService service;

    @Mock
    private FilterInstanceDomainService domainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private FilterTemplateDomainService filterTemplateDomainService;

    @Mock
    private FilterReportDomainService filterReportDomainService;

    @Mock
    private SecurityFilterDomainService securityFilterDomainService;

    @Mock
    private FolderEntitySearchDomainService folderEntitySearchDomainService;

    @Mock
    private FilterInstanceFolderRepository repository;

    @Mock
    private FilterInstanceResponseMapper mapper;

    @Mock
    private PermissionCheckerSystem permissionCheckerSystem;

    @Mock
    private ReportDomainService reportDomainService;


    @Test
    void getFolder() {
        when(domainService.getFolder(any())).thenReturn(getFolderResponse());
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));
        when(folderPermissionsDomainService.getFilterInstanceFolderPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.emptyList());

        FilterInstanceFolderResponse response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        Mockito.reset(domainService);

        when(domainService.getFolder(any())).thenReturn(getFolderResponse().setAuthority(FolderAuthorityEnum.NONE).setParentId(ID));

        response = service.getFolder(getFolderRequest());

        assertNull(response);

        Mockito.reset(userDomainService);

        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.singletonList(new RoleView().setId(0L)));
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));

        response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService, times(2)).getFolder(any());
        verifyNoMoreInteractions(domainService);

    }

    @Test
    void addFolder() {
        when(domainService.addFolder(any())).thenReturn(getFolderResponse());
        when(domainService.getFolder(any())).thenReturn(getFolderResponse());
        when(folderPermissionsDomainService.getFilterInstanceFolderPermissions(any())).thenReturn(new FilterInstanceFolderPermissionsResponse(new FilterInstanceFolderResponse().setId(ID), Collections.singletonList(new RolePermissionResponse(new RoleResponse(), Collections.singletonList(FolderAuthorityEnum.WRITE)))));

        var response = service.addFolder(getFolderAddRequest().setParentId(null));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verifyNoMoreInteractions(domainService);

        response = service.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService, times(2)).addFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getChildFolders() {

        when(domainService.getChildFolders(any())).thenReturn(Collections.singletonList(getFolderResponse()));

        List<FilterInstanceFolderResponse> responses = service.getChildFolders(getFolderRequest());
        assertNotNull(responses);

        FilterInstanceFolderResponse response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).getChildFolders(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void renameFolder() {
        when(domainService.renameFolder(any())).thenReturn(getFolderResponse());

        FilterInstanceFolderResponse response = service.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).renameFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void deleteFolder() {

        service.deleteFolder(getFolderRequest());

        verify(domainService).deleteFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addFilterInstance() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        when(filterTemplateDomainService.getFilterTemplate(any())).thenReturn(new FilterTemplateResponse());
        when(domainService.addFilterInstance(any(), any(), any())).thenReturn(ID);
        when(domainService.getFilterInstance(any())).thenReturn(getFilterInstanceResponse());

        var response = service.addFilterInstance(getFilterInstanceAddRequest());

        assertNotNull(response);

        verify(userDomainService).getCurrentUser();
        verify(filterTemplateDomainService).getFilterTemplate(any());
        verify(domainService).addFilterInstance(any(), any(), any());
        verify(domainService).getFilterInstance(any());

        verifyNoMoreInteractions(userDomainService, filterTemplateDomainService, domainService);
    }

    @Test
    void deleteFilterInstance() {

        when(filterReportDomainService.checkFilterReportForInstance(any())).thenReturn(Collections.emptyList());

        service.deleteFilterInstance(getFilterInstanceRequest());

        verify(filterReportDomainService).checkFilterReportForInstance(any());
        verify(domainService).deleteFilterInstance(any());

        verifyNoMoreInteractions(filterReportDomainService, domainService);

        Mockito.reset(filterReportDomainService);

        when(filterReportDomainService.checkFilterReportForInstance(any())).thenReturn(Collections.singletonList(new FilterReportResponse()));
        var request = getFilterInstanceRequest();
        assertThrows(InvalidParametersException.class, () -> service.deleteFilterInstance(request));

        verify(filterReportDomainService).checkFilterReportForInstance(any());
        verifyNoMoreInteractions(filterReportDomainService, domainService);
    }

    @Test
    void getFilterInstance() {

        when(domainService.getFilterInstance(any())).thenReturn(getFilterInstanceResponse());

        var response = service.getFilterInstance(getFilterInstanceRequest());

        assertNotNull(response);

        verify(domainService).getFilterInstance(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void editFilterInstance() {

        when(filterTemplateDomainService.getFilterTemplate(any())).thenReturn(new FilterTemplateResponse());
        when(domainService.editFilterInstance(any(), any())).thenReturn(ID);
        when(domainService.getFilterInstance(any())).thenReturn(getFilterInstanceResponse());

        var response = service.editFilterInstance(getFilterInstanceAddRequest());
        assertNotNull(response);

        verify(filterTemplateDomainService).getFilterTemplate(any());
        verify(domainService).editFilterInstance(any(), any());

        verifyNoMoreInteractions(filterTemplateDomainService, domainService);
    }

    @Test
    void getFilterInstanceValues() {

        when(domainService.getFilterInstanceValues(any())).thenReturn(new FilterInstanceValuesResponse(getFilterInstanceResponse(), Collections.emptyList()));

        var response = service.getFilterInstanceValues(new ListValuesRequest());
        assertNotNull(response);

        verify(domainService).getFilterInstanceValues(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getChildNodes() {

        when(userDomainService.getCurrentUserRoles(any())).thenReturn(Collections.emptyList());
        when(domainService.getDataSetId(any())).thenReturn(ID);
        when(securityFilterDomainService.getEffectiveSettings(any(), any())).thenReturn(Collections.emptyList());
        when(domainService.getChildNodes(any(), anyList())).thenReturn(getFilterInstanceChildNodesResponse());

        var response = service.getChildNodes(new ChildNodesRequest());
        assertNotNull(response);

        verify(userDomainService).getCurrentUserRoles(any());
        verify(domainService).getDataSetId(any());
        verify(securityFilterDomainService).getEffectiveSettings(any(), any());

        verifyNoMoreInteractions(userDomainService, domainService, securityFilterDomainService);

    }

    @Test
    void changeParentFolder() {

        when(domainService.changeParentFolder(any())).thenReturn(getFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));

        var response = service.changeParentFolder(new FolderChangeParentRequest());

        assertNotNull(response);

        verify(domainService).changeParentFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void searchFilterInstance() {
        when(folderEntitySearchDomainService.search(any(), any(), any(), any())).thenReturn(new FolderSearchResponse(Collections.emptyList(), Collections.emptyList()));

        var response = service.searchFilterInstance(new FolderSearchRequest());
        assertNotNull(response);

        verify(folderEntitySearchDomainService).search(any(), any(), any(), any());
        verifyNoMoreInteractions(folderEntitySearchDomainService);

    }

    @Test
    void changeExcelTemplateParentFolder(){

        service.changeFilterInstanceParentFolder(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(),any(),any());
        verify(domainService).changeFilterInstanceParentFolder(any());
        verifyNoMoreInteractions(permissionCheckerSystem,domainService);

    }

    @Test
    void copyFilterInstance(){

        service.copyFilterInstance(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(),any(),any());
        verify(domainService).copyFilterInstance(any(),any());
        verifyNoMoreInteractions(permissionCheckerSystem,domainService);

    }

    @Test
    void getFilterInstanceDependants(){

        when(domainService.getFilterInstanceDependants(any())).thenReturn(getFilterInstanceDependenciesResponse());
        when(reportDomainService.getPathReport(any())).thenReturn(Collections.emptyList());
        when(securityFilterDomainService.getPathSecurityFilter(any())).thenReturn(Collections.emptyList());
        when(domainService.getPathFilter(any())).thenReturn(Collections.emptyList());

        var response = service.getFilterInstanceDependants(getFilterInstanceRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getCreator());
        assertEquals(FilterTypeEnum.DATE_RANGE, response.getType());
        assertEquals(1, response.getSupportedOperations().size());
        assertNotNull(response.getTemplate());
        assertEquals(1, response.getFields().size());
        assertEquals(1, response.getReportFilters().size());
        assertTrue(response.getValid());
        assertTrue(response.getPath().isEmpty());
        assertEquals(1,response.getSecurityFilters().size());


        verify(domainService).getFilterInstanceDependants(any());
        verify(domainService).getPathFilter(any());
        verify(reportDomainService).getPathReport(any());
        verify(securityFilterDomainService).getPathSecurityFilter(any());
        verifyNoMoreInteractions(domainService,reportDomainService,securityFilterDomainService);

    }

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FilterInstanceFolderResponse getFolderResponse() {
        return new FilterInstanceFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(List.of(new FilterInstanceFolderResponse())))
                .setFilterInstances(Collections.singletonList(new FilterInstanceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FilterInstanceResponse getFilterInstanceResponse() {
        return new FilterInstanceResponse()
                .setId(ID);
    }

    private FilterInstanceRequest getFilterInstanceRequest() {
        return new FilterInstanceRequest().setId(ID);
    }

    private FilterInstanceAddRequest getFilterInstanceAddRequest() {
        return new FilterInstanceAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFolderId(2L)
                .setDataSetId(1L)
                .setTemplateId(3L);
    }

    private FilterInstanceChildNodesResponse getFilterInstanceChildNodesResponse() {
        return new FilterInstanceChildNodesResponse(
                getFilterInstanceResponse(),
                ID,
                new FilterNodeResponse(null, null, null, null, null)
        );
    }


    private ChangeParentFolderRequest getChangeParentFolderRequest() {
        return new ChangeParentFolderRequest()
                .setDestFolderId(ID)
                .setObjIds(Collections.emptyList());
    }

    private FilterInstanceDependenciesResponse getFilterInstanceDependenciesResponse(){
        return new FilterInstanceDependenciesResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreator(new UserResponse())
                .setType(FilterTypeEnum.DATE_RANGE)
                .setSupportedOperations(Collections.singletonList(FilterOperationTypeEnum.IS_EQUAL))
                .setTemplate(new FilterTemplateResponse())
                .setFields(Collections.singletonList(new FilterInstanceFieldResponse()))
                .setReportFilters(Collections.singletonList(new FilterReportResponse()))
                .setValid(true)
                .setPath(Collections.singletonList(new FolderNodeResponse(null,null,null,null, null, null)))
                .setReports(Collections.singletonList(new ReportResponse()))
                .setSecurityFilters(Collections.singletonList(new SecurityFilterResponse(null,null,null,null,null,null,null,null,null,null,new ArrayList<>())));

    }
}