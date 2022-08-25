package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceObjectFieldsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemaObjectsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemasRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceObjectResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSourceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceResponseMapper;
import ru.magnit.magreportbackend.repository.DataSourceFolderRepository;
import ru.magnit.magreportbackend.service.dao.ConnectionPoolManager;
import ru.magnit.magreportbackend.service.domain.DataSourceDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceServiceTest {

    public static final short POOL_SIZE = (short) 5;
    private final Long ID = 1L;
    private final String NAME = "name";
    private final String RENAME = "rename";
    private final String DESCRIPTION = "description";
    private final String URL = "URL";
    private final String USER_NAME = "user";
    private final String PASSWORD = "pass";
    private final String SCHEMA_NAME = "schema";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private DataSourceService service;

    @Mock
    private DataSourceDomainService dataSourceDomainService;

    @Mock
    private MetaDataService metaDataService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private ConnectionPoolManager connectionPoolManager;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private FolderEntitySearchDomainService folderEntitySearchDomainService;

    @Mock
    private DataSourceFolderRepository repository;

    @Mock
    private DataSourceResponseMapper mapper;

    @Mock
    private PermissionCheckerSystem permissionCheckerSystem;

    @Test
    void getFolder() {
        when(dataSourceDomainService.getFolder(any())).thenReturn(getFolderResponse());
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));
        when(folderPermissionsDomainService.getDataSourceFolderPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.emptyList());

        DataSourceFolderResponse response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceDomainService).getFolder(any());
        verifyNoMoreInteractions(dataSourceDomainService);

        Mockito.reset(dataSourceDomainService);
        when(dataSourceDomainService.getFolder(any())).thenReturn(getFolderResponse().setAuthority(FolderAuthorityEnum.NONE).setParentId(ID));

        response = service.getFolder(getFolderRequest());
        assertNull(response);

        verify(dataSourceDomainService).getFolder(any());
        verifyNoMoreInteractions(dataSourceDomainService);

        Mockito.reset(userDomainService, dataSourceDomainService);

        when(dataSourceDomainService.getFolder(any())).thenReturn(getFolderResponse());
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.singletonList(new RoleView().setId(0L)));
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));

        response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceDomainService).getFolder(any());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void addFolder() {
        when(dataSourceDomainService.addFolder(any())).thenReturn(getFolderResponse());
        when(folderPermissionsDomainService.getDataSourceFolderPermissions(any())).thenReturn(new DataSourceFolderPermissionsResponse(new DataSourceFolderResponse().setId(ID), Collections.singletonList(new RolePermissionResponse(new RoleResponse(), Collections.singletonList(FolderAuthorityEnum.WRITE)))));
        when(dataSourceDomainService.getFolder(any())).thenReturn(getFolderResponse());

        DataSourceFolderResponse response = service.addFolder(getAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceDomainService).addFolder(any());
        verifyNoMoreInteractions(dataSourceDomainService);

        Mockito.reset(dataSourceDomainService);
        when(dataSourceDomainService.addFolder(any())).thenReturn(getFolderResponse());
        when(dataSourceDomainService.getFolder(any())).thenReturn(getFolderResponse());


        response = service.addFolder(getAddRequest().setParentId(null));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceDomainService).addFolder(any());
        verifyNoMoreInteractions(dataSourceDomainService);

    }

    @Test
    void getChildFolders() {
        when(dataSourceDomainService.getChildFolders(any())).thenReturn(Collections.singletonList(getFolderResponse()));

        List<DataSourceFolderResponse> responseList = service.getChildFolders(getFolderRequest());

        assertNotNull(responseList);

        DataSourceFolderResponse response = responseList.get(0);
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceDomainService).getChildFolders(any());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void renameFolder() {
        when(dataSourceDomainService.renameFolder(any())).thenReturn(getRenameFolderResponse());

        DataSourceFolderResponse response = service.renameFolder(getRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(RENAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceDomainService).renameFolder(any());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void addDataSource() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));
        when(dataSourceDomainService.addDataSource(any(), any())).thenReturn(ID);
        when(dataSourceDomainService.getDataSource(anyLong())).thenReturn(getDataSourceResponse());

        DataSourceResponse response = service.addDataSource(getDataSourceAddRequest());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(URL, response.url());
        assertEquals(USER_NAME, response.userName());
        assertEquals(CREATED_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());

        verify(dataSourceDomainService).addDataSource(any(), any());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void editDataSource() {
        when(dataSourceDomainService.editDataSource(any())).thenReturn(getRenameDataSourceResponse());
        when(dataSourceDomainService.getDataSourceView(any())).thenReturn(new DataSourceData(null, null, null, null, null, null));

        DataSourceResponse response = service.editDataSource(getRenameDataSourceAddRequest());

        assertEquals(ID, response.id());
        assertEquals(RENAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(URL, response.url());
        assertEquals(USER_NAME, response.userName());
        assertEquals(CREATED_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());

        verify(dataSourceDomainService).editDataSource(any());
        verify(dataSourceDomainService).getDataSourceView(any());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void getDataSource() {
        when(dataSourceDomainService.getDataSource(any())).thenReturn(getDataSourceResponse());

        DataSourceResponse response = service.getDataSource(getDataSourceRequest());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(URL, response.url());
        assertEquals(USER_NAME, response.userName());
        assertEquals(CREATED_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());

        verify(dataSourceDomainService).getDataSource(any());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void getCatalogs() {
        when(metaDataService.getCatalogs(any())).thenReturn(Collections.singletonList(NAME));

        List<String> catalogs = service.getCatalogs(getDataSourceRequest());

        assertNotNull(catalogs);
        assertEquals(NAME, catalogs.get(0));

        verify(metaDataService).getCatalogs(any());
        verifyNoMoreInteractions(metaDataService);
    }

    @Test
    void getSchemas() {
        when(metaDataService.getSchemas(any(), any())).thenReturn(Collections.singletonList(NAME));

        List<String> catalogs = service.getSchemas(getSchemasRequest());

        assertNotNull(catalogs);
        assertEquals(NAME, catalogs.get(0));

        verify(metaDataService).getSchemas(any(), any());
        verifyNoMoreInteractions(metaDataService);
    }

    @Test
    void getSchemaObjects() {
        when(metaDataService.getSchemaObjects(any(), any(), any(), any())).thenReturn(Collections.singletonList(new DataSourceObjectResponse()));

        List<DataSourceObjectResponse> objects = service.getSchemaObjects(getSchemaObjectsRequest());
        assertNotNull(objects);

        verify(metaDataService).getSchemaObjects(any(), any(), any(), any());
        verifyNoMoreInteractions(metaDataService);
    }

    @Test
    void getObjectFields() {
        when(metaDataService.getObjectFields(any(), any(), any(), any())).thenReturn(Collections.singletonList(new ObjectFieldResponse()));

        List<ObjectFieldResponse> objects = service.getObjectFields(getSchemaObjectFieldsRequest());
        assertNotNull(objects);

        verify(metaDataService).getObjectFields(any(), any(), any(), any());
        verifyNoMoreInteractions(metaDataService);
    }

    @Test
    void deleteFolder() {

        service.deleteFolder(getFolderRequest());

        verify(dataSourceDomainService).deleteFolder(anyLong());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void deleteDataSource() {

        service.deleteDataSource(getDataSourceRequest());

        verify(dataSourceDomainService).deleteDataSource(anyLong());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void getSchemaProcedures() {

        when(metaDataService.getProcedures(any(), any(), any())).thenReturn(Collections.emptyList());

        var response = service.getSchemaProcedures(getSchemaObjectsRequest());

        assertNotNull(response);

        verify(metaDataService).getProcedures(any(), anyString(), any());
        verifyNoMoreInteractions(metaDataService);

    }

    @Test
    void getSchemaProcedureFields() {
        when(metaDataService.getProcedureFields(any(), anyString(), any(), anyString())).thenReturn(Collections.emptyList());

        var response = service.getSchemaProcedureFields(getSchemaObjectFieldsRequest());
        assertNotNull(response);

        verify(metaDataService).getProcedureFields(any(), anyString(), any(), anyString());
        verifyNoMoreInteractions(metaDataService);
    }

    @Test
    void getDataSourceTypes() {

        when(dataSourceDomainService.getDataSourceTypes()).thenReturn(Collections.emptyList());

        var response = service.getDataSourceTypes();
        assertNotNull(response);

        verify(dataSourceDomainService).getDataSourceTypes();
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void getDataSourceDependencies() {
        when(dataSourceDomainService.getDataSourceDependencies(anyLong())).thenReturn(new DataSourceDependenciesResponse());

        var response = service.getDataSourceDependencies(getDataSourceRequest());
        assertNotNull(response);

        verify(dataSourceDomainService).getDataSourceDependencies(anyLong());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void changeParentFolder() {
        when(dataSourceDomainService.changeParentFolder(any())).thenReturn(new DataSourceFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));

        var response = service.changeParentFolder(new FolderChangeParentRequest());
        assertNotNull(response);

        verify(dataSourceDomainService).changeParentFolder(any());
        verifyNoMoreInteractions(dataSourceDomainService);
    }

    @Test
    void searchDataSet() {
        when(folderEntitySearchDomainService.search(any(), any(), any(), any())).thenReturn(new FolderSearchResponse(Collections.emptyList(), Collections.emptyList()));

        var response = service.searchDataSource(new FolderSearchRequest());
        assertNotNull(response);

        verify(folderEntitySearchDomainService).search(any(), any(), any(), any());
        verifyNoMoreInteractions(folderEntitySearchDomainService);

    }

    @Test
    void changeDataSourceParentFolder() {

        service.changeDataSourceParentFolder(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(), any(), any());
        verify(dataSourceDomainService).changeDataSourceParentFolder(any());
        verifyNoMoreInteractions(permissionCheckerSystem, dataSourceDomainService);
    }

    @Test
    void checkDataSource() {
        service.checkDataSource(getDataSourceRequest());

        verify(metaDataService).checkDataSource(any());
        verify(dataSourceDomainService).getDataSourceView(anyLong());
        verifyNoMoreInteractions(metaDataService, dataSourceDomainService);
    }

    @Test
    void copyDataSource() {
        service.copyDataSource(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(), any(), any());
        verify(dataSourceDomainService).copyDataSource(any(), any());
        verifyNoMoreInteractions(permissionCheckerSystem, dataSourceDomainService);


    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private DataSourceFolderResponse getFolderResponse() {
        return new DataSourceFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(List.of(new DataSourceFolderResponse())))
                .setDataSources(Collections.singletonList(new DataSourceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private DataSourceObjectFieldsRequest getSchemaObjectFieldsRequest() {
        return new DataSourceObjectFieldsRequest()
                .setId(ID)
                .setCatalogName(NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(SCHEMA_NAME);
    }

    private FolderAddRequest getAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private DataSourceFolderResponse getRenameFolderResponse() {
        return new DataSourceFolderResponse()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSourceFolderResponse()))
                .setDataSources(Collections.singletonList(new DataSourceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderRenameRequest getRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION);
    }

    private DataSourceResponse getDataSourceResponse() {

        return new DataSourceResponse(
                ID,
                NAME,
                DESCRIPTION,
                URL,
                USER_NAME,
                new DataSourceTypeResponse(ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME),
                POOL_SIZE,
                "Creator",
                CREATED_TIME,
                MODIFIED_TIME
        );
    }

    private DataSourceAddRequest getDataSourceAddRequest() {
        return new DataSourceAddRequest()
                .setId(ID)
                .setFolderId(ID)
                .setTypeId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setUrl(URL)
                .setUserName(USER_NAME)
                .setPassword(PASSWORD);
    }

    private DataSourceAddRequest getRenameDataSourceAddRequest() {
        return new DataSourceAddRequest()
                .setId(ID)
                .setFolderId(ID)
                .setTypeId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION)
                .setUrl(URL)
                .setUserName(USER_NAME)
                .setPassword(PASSWORD);
    }

    private DataSourceResponse getRenameDataSourceResponse() {

        return new DataSourceResponse(
                ID,
                RENAME,
                DESCRIPTION,
                URL,
                USER_NAME,
                new DataSourceTypeResponse(ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME),
                POOL_SIZE,
                "Creator",
                CREATED_TIME,
                MODIFIED_TIME
        );
    }

    private DataSourceRequest getDataSourceRequest() {
        return new DataSourceRequest()
                .setId(ID);
    }

    private DataSourceSchemasRequest getSchemasRequest() {
        return new DataSourceSchemasRequest()
                .setId(ID)
                .setCatalogName(NAME);
    }

    private DataSourceSchemaObjectsRequest getSchemaObjectsRequest() {
        return new DataSourceSchemaObjectsRequest()
                .setId(ID)
                .setCatalogName(NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectType(SCHEMA_NAME);
    }

    private ChangeParentFolderRequest getChangeParentFolderRequest() {
        return new ChangeParentFolderRequest()
                .setDestFolderId(ID)
                .setObjIds(Collections.emptyList());
    }

}