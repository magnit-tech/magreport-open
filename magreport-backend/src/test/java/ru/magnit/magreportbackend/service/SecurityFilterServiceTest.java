package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.LikenessType;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.RoleSettingsRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterDataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.FieldMappingResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.RoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterDataSetResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterRoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRepository;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityFilterServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "Name";
    private static final String DESCRIPTION = "Desc";
    private static final Long FOLDER_ID = 5L;
    private static final Long FILTER_INSTANCE_ID = 10L;
    private static final Long PARENT_ID = 20L;
    private static final Long SECURITY_FILTER_ID = 50L;
    private static final Long ROLE_ID = 99L;
    private static final FilterOperationTypeEnum OPERATION_TYPE = FilterOperationTypeEnum.IS_BETWEEN;
    private static final List<SecurityFilterDataSetAddRequest> DATASETS = Collections.singletonList(new SecurityFilterDataSetAddRequest());
    private static final String USERNAME = "User";
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();
    private static final FolderAuthorityEnum AUTHORITY = FolderAuthorityEnum.READ;
    private static final List<RoleSettingsRequest> ROLE_SETTINGS = Collections.singletonList(new RoleSettingsRequest().setRoleId(ROLE_ID));
    @Mock
    private SecurityFilterDomainService domainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private SecurityFilterFolderRepository securityFilterFolderRepository;

    @Mock
    private SecurityFilterResponseMapper securityFilterResponseMapper;

    @Mock
    private FolderEntitySearchDomainService folderEntitySearchDomainService;

    @Mock
    private PermissionCheckerSystem permissionCheckerSystem;

    @InjectMocks
    private SecurityFilterService service;

    @Test
    void addSecurityFilter() {

        SecurityFilterAddRequest request = spy(getSecurityFilterAddRequest());

        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        when(domainService.addSecurityFilter(any(), any())).thenReturn(ID);
        when(domainService.getSecurityFilter(anyLong())).thenReturn(getSecurityFilterResponse());

        final var result = service.addSecurityFilter(request);

        assertNotNull(result);

        verify(userDomainService).getCurrentUser();
        verify(domainService).addSecurityFilter(any(), any());
        verify(domainService).getSecurityFilter(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);
    }

    @Test
    void getSecurityFilter() {

        SecurityFilterRequest request = spy(getSecurityFilterRequest());

        when(domainService.getSecurityFilter(anyLong())).thenReturn(getSecurityFilterResponse());

        final var result = service.getSecurityFilter(request);

        assertNotNull(result);

        verify(request).getId();
        verify(domainService).getSecurityFilter(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void deleteSecurityFilter() {

        SecurityFilterRequest request = spy(getSecurityFilterRequest());

        service.deleteSecurityFilter(request);

        verify(request).getId();
        verify(domainService).deleteSecurityFilter(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void getFolder() {

        FolderRequest request = spy(getFolderRequest());
        SecurityFilterFolderResponse response = getSecurityFilterFolderResponse();

        when(domainService.getFolder(anyLong())).thenReturn(response);
        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        when(userDomainService.getUserRoles(any(), any())).thenReturn(Collections.singletonList(new RoleView()));
        when(folderPermissionsDomainService.getFoldersReportPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.singletonList(getFolderRoleResponse()));

        final var result = service.getFolder(request);

        assertNotNull(result);
        assertEquals(FOLDER_ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(AUTHORITY, result.getAuthority());
        assertEquals(1, result.getChildFolders().size());
        assertEquals(AUTHORITY, result.getChildFolders().get(0).getAuthority());


        verify(request).getId();
        verify(domainService).getFolder(anyLong());
        verify(userDomainService).getUserRoles(any(), any());
        verify(userDomainService).getCurrentUser();
        verify(folderPermissionsDomainService).getFoldersReportPermissionsForRoles(anyList(), anyList());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void addFolder() {

        FolderAddRequest request = spy(getFolderAddRequest());

        when(domainService.addFolder(any())).thenReturn(FOLDER_ID);
        when(domainService.getFolder(anyLong())).thenReturn(new SecurityFilterFolderResponse());

        final var result = service.addFolder(request);

        assertNotNull(result);

        verify(domainService).addFolder(any());
        verify(domainService).getFolder(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);
    }

    @Test
    void renameFolder() {

        FolderRenameRequest request = spy(getFolderRenameRequest());

        when(domainService.renameFolder(any())).thenReturn(FOLDER_ID);
        when(domainService.getFolder(anyLong())).thenReturn(new SecurityFilterFolderResponse());

        final var result = service.renameFolder(request);

        assertNotNull(result);

        verify(domainService).renameFolder(any());
        verify(domainService).getFolder(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);
    }

    @Test
    void deleteFolder() {

        FolderRequest request = spy(getFolderRequest());

        service.deleteFolder(request);

        verify(request).getId();
        verify(domainService).deleteFolder(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void setRoleSettings() {

        SecurityFilterSetRoleRequest request = spy(getSecurityFilterSetRoleRequest());

        when(domainService.getFilterRoleSettings(anyLong())).thenReturn(new SecurityFilterRoleSettingsResponse(1L, Collections.emptyList()));

        final var result = service.setRoleSettings(request);

        assertNotNull(result);

        verify(request, times(2)).getSecurityFilterId();
        verify(domainService).deleteRoles(anyLong());
        verify(domainService).setRoleSettings(any());
        verify(domainService).getFilterRoleSettings(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void getRoleSettings() {

        SecurityFilterRequest request = spy(getSecurityFilterRequest());

        when(domainService.getFilterRoleSettings(ID)).thenReturn(new SecurityFilterRoleSettingsResponse(1L, Collections.emptyList()));

        final var result = service.getRoleSettings(request);

        assertNotNull(result);

        verify(request).getId();
        verify(domainService).getFilterRoleSettings(ID);

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void editSecurityFilter() {

        // ID is null

        var filterAddRequest = new SecurityFilterAddRequest().setId(null);
        assertThrows(InvalidParametersException.class, () -> service.editSecurityFilter(filterAddRequest));


        // ID not null
         var request = spy(getSecurityFilterAddRequest());

        when(domainService.getSecurityFilter(anyLong())).thenReturn(getSecurityFilterResponse());

        final var result = service.editSecurityFilter(request);

        assertNotNull(result);

        verify(request, times(3)).getId();
        verify(domainService).deleteDataSetMappings(anyLong());
        verify(domainService).editSecurityFilter(any());
        verify(domainService).getSecurityFilter(anyLong());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void updateRoleSettings() {

        SecurityFilterSetRoleRequest request = spy(getSecurityFilterSetRoleRequest());

        service.updateRoleSettings(request);

        verify(request).getSecurityFilterId();
        verify(request).getRoleSettings();
        verify(domainService).deleteRole(anyLong(), anyLong());
        verify(domainService).setRoleSettings(any());

        verifyNoMoreInteractions(request, domainService, userDomainService, folderPermissionsDomainService);

    }

    @Test
    void changeParentFolder() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));

        service.changeParentFolder(new FolderChangeParentRequest());

        verify(domainService).changeParentFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void searchSecurityFilter() {
     when(folderEntitySearchDomainService.search(any(),any(),any(),any())).thenReturn(new FolderSearchResponse(Collections.emptyList(),Collections.emptyList()));

        assertNotNull(service.searchSecurityFilter(new FolderSearchRequest(ID, LikenessType.CONTAINS,"",true)));

        verify(folderEntitySearchDomainService).search(any(),any(), any(), any());
        verifyNoMoreInteractions(folderEntitySearchDomainService);
    }

    @Test
    void changeSecurityFilterParentFolder(){


        service.changeSecurityFilterParentFolder(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(),any(),any());
        verify(domainService).changeFilterInstanceParentFolder(any());
        verifyNoMoreInteractions(permissionCheckerSystem,domainService);

    }

    private SecurityFilterSetRoleRequest getSecurityFilterSetRoleRequest() {
        return new SecurityFilterSetRoleRequest()
                .setSecurityFilterId(SECURITY_FILTER_ID)
                .setRoleSettings(ROLE_SETTINGS);
    }

    private SecurityFilterAddRequest getSecurityFilterAddRequest() {
        return new SecurityFilterAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFolderId(FOLDER_ID)
                .setFilterInstanceId(FILTER_INSTANCE_ID)
                .setOperationType(OPERATION_TYPE)
                .setDataSets(DATASETS);
    }

    private SecurityFilterResponse getSecurityFilterResponse() {
        return new SecurityFilterResponse(ID,
                new FilterInstanceResponse(),
                OPERATION_TYPE,
                NAME,
                DESCRIPTION,
                Collections.singletonList(new SecurityFilterDataSetResponse(new DataSetResponse(), Collections.singletonList(new FieldMappingResponse(1L, 2L)))),
                Collections.singletonList(new RoleSettingsResponse(new RoleResponse(), Collections.singletonList(new Tuple()))),
                USERNAME,
                CREATED_DATE,
                MODIFIED_DATE,
                Collections.emptyList());
    }

    private SecurityFilterRequest getSecurityFilterRequest() {
        return new SecurityFilterRequest()
                .setId(ID);
    }

    private FolderRoleResponse getFolderRoleResponse() {
        return new FolderRoleResponse()
                .setFolderId(FOLDER_ID)
                .setAuthority(AUTHORITY);
    }

    private SecurityFilterFolderResponse getSecurityFilterFolderResponse() {
        return new SecurityFilterFolderResponse()
                .setId(FOLDER_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new SecurityFilterFolderResponse()))
                ;
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setParentId(PARENT_ID);
    }

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(FOLDER_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private ChangeParentFolderRequest getChangeParentFolderRequest() {
        return new ChangeParentFolderRequest()
                .setDestFolderId(ID)
                .setObjIds(Collections.emptyList());
    }
}