package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceShortResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSetFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;
import ru.magnit.magreportbackend.repository.DataSetFolderRepository;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.DataSourceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSetServiceTest {

    private static final String URL = "url";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "password";
    private static final Long ID = 1L;
    private static final Long FOLDER_ID = 2L;
    private static final Long TYPE_ID_TABLE = 0L;
    private static final Long TYPE_ID_PROCEDURE = 1L;
    private static final String NAME = "Test folder";
    private static final String DESCRIPTION = "Folder description";
    private static final String CATALOG_NAME = "catalog";
    private static final String SCHEMA_NAME = "schema";
    private static final String OBJECT_NAME = "object";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);
    public static final short POOL_SIZE = (short) 5;

    @InjectMocks
    private DataSetService service;

    @Mock
    private DataSetDomainService dataSetDomainService;

    @Mock
    private MetaDataService metaDataService;

    @Mock
    private DataSourceDomainService dataSourceDomainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private ProcedureReportService procedureReportService;

    @Mock
    private SecurityFilterDomainService securityFilterDomainService;

    @Mock
    private FilterInstanceDomainService filterInstanceDomainService;

    @Mock
    private ReportDomainService reportDomainService;

    @Mock
    private FolderEntitySearchDomainService folderEntitySearchDomainService;

    @Mock
    private DataSetFolderRepository repository;

    @Mock
    private DataSetResponseMapper mapper;

    @Mock
    private PermissionCheckerSystem permissionCheckerSystem;


    @Test
    void getFolder() {
        when(dataSetDomainService.getFolder(anyLong())).thenReturn(getFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        when(userDomainService.getUserRoles(any(), any())).thenReturn(Collections.singletonList(new RoleView().setId(0L)));
        when(folderPermissionsDomainService.getDataSetFolderPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.emptyList());

        DataSetFolderResponse response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetDomainService).getFolder(anyLong());
        verifyNoMoreInteractions(dataSetDomainService);

        Mockito.reset(userDomainService, dataSetDomainService);
        when(dataSetDomainService.getFolder(anyLong())).thenReturn(getFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        when(userDomainService.getUserRoles(any(), any())).thenReturn(Collections.singletonList(new RoleView().setId(5L)));

        response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetDomainService).getFolder(anyLong());
        verifyNoMoreInteractions(dataSetDomainService);

        Mockito.reset(dataSetDomainService);
        when(dataSetDomainService.getFolder(anyLong())).thenReturn(getFolderResponse().setAuthority(FolderAuthorityEnum.NONE).setParentId(ID));

        response = service.getFolder(getFolderRequest());

        assertNull(response);

        verify(dataSetDomainService).getFolder(anyLong());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void addFolder() {

        when(dataSetDomainService.getFolder(any())).thenReturn(getFolderResponse());
        when(dataSetDomainService.addFolder(any())).thenReturn(getFolderResponse());

        var response = service.addFolder(getFolderAddRequest().setParentId(null));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        Mockito.reset(dataSetDomainService);

        when(dataSetDomainService.addFolder(any())).thenReturn(getFolderResponse());
        when(folderPermissionsDomainService.getDataSetFolderPermissions(any())).thenReturn(new DataSetFolderPermissionsResponse(new DataSetFolderResponse().setId(ID), Collections.singletonList(new RolePermissionResponse(new RoleResponse(), Collections.singletonList(FolderAuthorityEnum.WRITE)))));
        when(dataSetDomainService.getFolder(any())).thenReturn(getFolderResponse());

        response = service.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetDomainService).addFolder(any());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void getChildFolders() {
        when(dataSetDomainService.getChildFolders(anyLong())).thenReturn(Collections.singletonList(getFolderResponse()));

        List<DataSetFolderResponse> responses = service.getChildFolders(getFolderRequest());

        assertNotNull(responses);
        DataSetFolderResponse response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetDomainService).getChildFolders(any());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void renameFolder() {
        when(dataSetDomainService.renameFolder(any())).thenReturn(getFolderResponse());

        DataSetFolderResponse response = service.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetDomainService).renameFolder(any());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void getDataSet() {
        when(dataSetDomainService.getDataSet(any())).thenReturn(getDataSetResponse(TYPE_ID_TABLE));

        DataSetResponse response = service.getDataSet(getDataSetRequest());

        assertEquals(ID, response.getId());
        assertEquals(TYPE_ID_TABLE, response.getTypeId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CATALOG_NAME, response.getCatalogName());
        assertEquals(SCHEMA_NAME, response.getSchemaName());
        assertEquals(OBJECT_NAME, response.getObjectName());
        assertNotNull(response.getFields());

        verify(dataSetDomainService).getDataSet(any());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void createDataSetFromDBMetaData() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));
        when(dataSourceDomainService.getDataSourceView(anyLong())).thenReturn(new DataSourceData(ID, DataSourceTypeEnum.H2, URL, USER_NAME, PASSWORD, POOL_SIZE));
        when(metaDataService.getObjectFields(any(), any(), any(), any())).thenReturn(Collections.singletonList(new ObjectFieldResponse()));
        when(dataSetDomainService.createDataSetFromMetaData(any(), any(), anyList())).thenReturn(ID);
        when(dataSetDomainService.getDataSet(anyLong())).thenReturn(getDataSetResponse(TYPE_ID_TABLE));
        when(metaDataService.checkObjectExists(any(),any(),any())).thenReturn(true);

        DataSetResponse response = service.createDataSetFromDBMetaData(getDataSetCreateFromMetaDataRequest(TYPE_ID_TABLE));

        assertEquals(ID, response.getId());
        assertEquals(TYPE_ID_TABLE, response.getTypeId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CATALOG_NAME, response.getCatalogName());
        assertEquals(SCHEMA_NAME, response.getSchemaName());
        assertEquals(OBJECT_NAME, response.getObjectName());
        assertNotNull(response.getFields());

        verify(dataSetDomainService).createDataSetFromMetaData(any(), any(), anyList());
        verify(dataSetDomainService).getDataSet(anyLong());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void createDataSetFromDBMetaDataProcedure() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));
        when(dataSourceDomainService.getDataSourceView(anyLong())).thenReturn(new DataSourceData(ID, DataSourceTypeEnum.H2, URL, USER_NAME, PASSWORD, POOL_SIZE));
        when(dataSetDomainService.getDataSet(anyLong())).thenReturn(getDataSetResponse(TYPE_ID_PROCEDURE));
        when(procedureReportService.checkProcedureReportSchemaMetaData(any(), any())).thenReturn(false);

        DataSetResponse response = service.createDataSetFromDBMetaData(getDataSetCreateFromMetaDataRequest(TYPE_ID_PROCEDURE));

        assertEquals(ID, response.getId());
        assertEquals(TYPE_ID_PROCEDURE, response.getTypeId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CATALOG_NAME, response.getCatalogName());
        assertEquals(SCHEMA_NAME, response.getSchemaName());
        assertEquals(OBJECT_NAME, response.getObjectName());
        assertNotNull(response.getFields());

        verify(procedureReportService).checkProcedureReportSchemaMetaData(any(), any());
        verify(procedureReportService).createProcedureReportMetaData(any(), any());
        verify(dataSetDomainService).addDataSetFromProcedure(any(), any(), anyList());
        verify(dataSetDomainService).getDataSet(anyLong());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void getDataSetDataTypes() {
        when(dataSetDomainService.getDataSetDataTypes()).thenReturn(getDataSetDataTypesResponse());

        var response = service.getDataSetDataTypes();

        assertEquals(1, response.size());
        assertEquals(ID, response.get(0).getId());
        assertEquals(NAME, response.get(0).getName());
        assertEquals(DESCRIPTION, response.get(0).getDescription());

        verify(dataSetDomainService).getDataSetDataTypes();
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void deleteFolder() {

        service.deleteFolder(getFolderRequest());

        verify(dataSetDomainService).deleteFolder(anyLong());
        verifyNoMoreInteractions(dataSetDomainService);

    }

    @Test
    void editDataSet() {
        when(service.getDataSet(getDataSetRequest())).thenReturn(getDataSetResponse(ID));
        when(metaDataService.checkObjectExists(any(),any(),any())).thenReturn(true);

        var response = service.editDataSet(getDataSetAddRequest());
        Assertions.assertNotNull(response);

        verify(dataSetDomainService).editDataSet(any());
        verify(dataSetDomainService).getDataSet(any());
        verify(metaDataService).checkObjectExists(any(),any(),any());
        verifyNoMoreInteractions(dataSetDomainService,metaDataService);
    }

    @Test
    void deleteDataSet() {
        // check delete
        when(reportDomainService.checkReportExistsForDataset(any())).thenReturn(Collections.emptyList());
        when(filterInstanceDomainService.checkFilterExistsForDataset(any())).thenReturn(Collections.emptyList());
        when(securityFilterDomainService.checkSecurityFilterExistsForDataset(any())).thenReturn(Collections.emptyList());

        service.deleteDataSet(getDataSetRequest());

        verify(reportDomainService).checkReportExistsForDataset(any());
        verify(filterInstanceDomainService).checkFilterExistsForDataset(any());
        verify(securityFilterDomainService).checkSecurityFilterExistsForDataset(any());
        verify(dataSetDomainService).deleteDataSet(any());

        verifyNoMoreInteractions(reportDomainService, filterInstanceDomainService, securityFilterDomainService, dataSetDomainService);

        Mockito.reset(reportDomainService, filterInstanceDomainService, securityFilterDomainService, securityFilterDomainService);

        //check Exception
        when(reportDomainService.checkReportExistsForDataset(any())).thenReturn(Collections.singletonList(new ReportShortResponse(ID, NAME)));
        when(filterInstanceDomainService.checkFilterExistsForDataset(any())).thenReturn(Collections.singletonList(new FilterInstanceShortResponse(ID, NAME)));
        when(securityFilterDomainService.checkSecurityFilterExistsForDataset(any())).thenReturn(Collections.emptyList());

        var request = getDataSetRequest();
        assertThrows(InvalidParametersException.class, () -> service.deleteDataSet(request));

        verify(reportDomainService).checkReportExistsForDataset(any());
        verify(filterInstanceDomainService).checkFilterExistsForDataset(any());
        verify(securityFilterDomainService).checkSecurityFilterExistsForDataset(any());
        verify(dataSetDomainService).deleteDataSet(any());

        verifyNoMoreInteractions(reportDomainService, filterInstanceDomainService, securityFilterDomainService, dataSetDomainService);
    }

    @Test
    void getDataSetTypes() {

        var response = service.getDataSetTypes();
        assertNotNull(response);

        verify(dataSetDomainService).getDataSetTypes();
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void refreshDataSet() {
        //check Procedure type
        when(dataSetDomainService.getDataSet(any())).thenReturn(getDataSetResponse(ID).setTypeId(1L));
        when(dataSourceDomainService.getDataSourceView(any())).thenReturn(new DataSourceData(ID, DataSourceTypeEnum.H2, URL, USER_NAME, PASSWORD, POOL_SIZE));
        when(metaDataService.getProcedureFields2(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(dataSetDomainService.getUnlinkedInvalidFields(any(), any())).thenReturn(Collections.emptyList());
        when(dataSetDomainService.refreshDataSet(any(), anyList())).thenReturn(Collections.emptyList());
        when(dataSetDomainService.getReportIds(any())).thenReturn(Collections.singletonList(ID));

        assertNotNull(service.refreshDataSet(getDataSetRequest()));

        verify(dataSetDomainService, times(2)).getDataSet(any());
        verify(metaDataService).getProcedureFields2(any(), any(), any(), any());
        verify(dataSetDomainService).getUnlinkedInvalidFields(any(), anyList());
        verify(dataSetDomainService).deleteFields(anyList());
        verify(dataSetDomainService).refreshDataSet(any(), anyList());
        verify(reportDomainService).addFields(anyLong(), anyList());
        verify(dataSetDomainService).actualizeDataSet(any());

        verifyNoMoreInteractions(dataSetDomainService);

        Mockito.reset(dataSetDomainService);

        //check refresh
        when(dataSetDomainService.getDataSet(any())).thenReturn(getDataSetResponse(ID).setTypeId(0L));
        when(dataSourceDomainService.getDataSourceView(any())).thenReturn(new DataSourceData(ID, DataSourceTypeEnum.H2, URL, USER_NAME, PASSWORD, POOL_SIZE));
        when(metaDataService.getObjectFields(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(dataSetDomainService.getUnlinkedInvalidFields(any(), any())).thenReturn(Collections.emptyList());
        when(dataSetDomainService.refreshDataSet(any(), anyList())).thenReturn(Collections.emptyList());
        when(dataSetDomainService.getReportIds(any())).thenReturn(Collections.singletonList(ID));

        assertNotNull(service.refreshDataSet(getDataSetRequest()));

        verify(dataSetDomainService, times(2)).getDataSet(any());
        verify(dataSourceDomainService, times(2)).getDataSourceView(any());
        verify(metaDataService).getObjectFields(any(), any(), any(), any());
        verify(dataSetDomainService).getUnlinkedInvalidFields(any(), anyList());
        verify(dataSetDomainService).deleteFields(anyList());
        verify(dataSetDomainService).refreshDataSet(any(), anyList());
        verify(reportDomainService, times(2)).addFields(anyLong(), anyList());
        verify(dataSetDomainService).actualizeDataSet(any());
        verify(reportDomainService).updateOrdinalFields(any(),any());
        verifyNoMoreInteractions(dataSetDomainService, dataSourceDomainService, metaDataService, reportDomainService);

    }

    @Test
    void getDataSetDependants() {

        when(dataSetDomainService.getDataSetDependants(any())).thenReturn(getDataSetDependenciesResponse());
        when(reportDomainService.getPathReport(anyLong())).thenReturn(Collections.emptyList());
        when(securityFilterDomainService.getPathSecurityFilter(anyLong())).thenReturn(Collections.emptyList());
        when(filterInstanceDomainService.getPathFilter(anyLong())).thenReturn(Collections.emptyList());

        var response = service.getDataSetDependants(getDataSetRequest());
        assertNotNull(response);

        verify(dataSetDomainService).getDataSetDependants(any());
        verify(reportDomainService).getPathReport(any());
        verify(securityFilterDomainService).getPathSecurityFilter(anyLong());
        verify(filterInstanceDomainService).getPathFilter(anyLong());
        verifyNoMoreInteractions(dataSetDomainService, reportDomainService, securityFilterDomainService, filterInstanceDomainService);
    }

    @Test
    void addDataSetFromProcedure() {

        when(userDomainService.getCurrentUser()).thenReturn(new UserView());
        when(dataSourceDomainService.getDataSourceView(any())).thenReturn(new DataSourceData(ID, DataSourceTypeEnum.H2, URL, USER_NAME, PASSWORD, POOL_SIZE));
        when(procedureReportService.checkProcedureReportSchemaMetaData(any(), any())).thenReturn(true);
        when(metaDataService.getProcedureFields2(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(dataSetDomainService.addDataSetFromProcedure(any(), any(), anyList())).thenReturn(ID);
        when(dataSetDomainService.getDataSet(any())).thenReturn(getDataSetResponse(ID));

        var response = service.addDataSetFromProcedure(new DataSetCreateFromMetaDataRequest().setDataSourceId(ID).setSchemaName(""));
        assertNotNull(response);

        verify(userDomainService).getCurrentUser();
        verify(dataSourceDomainService).getDataSourceView(any());
        verify(procedureReportService).createProcedureReportMetaData(any(), any());
        verify(procedureReportService).checkProcedureReportSchemaMetaData(any(), any());
        verify(dataSetDomainService).addDataSetFromProcedure(any(), any(), anyList());
        verify(dataSetDomainService).getDataSet(any());
        verifyNoMoreInteractions(userDomainService, dataSetDomainService, dataSourceDomainService, procedureReportService);
    }

    @Test
    void changeParentFolder() {
        when(dataSetDomainService.changeParentFolder(any())).thenReturn(new DataSetFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));

        var response = service.changeParentFolder(new FolderChangeParentRequest().setId(ID));
        assertNotNull(response);

        verify(dataSetDomainService).changeParentFolder(any());
        verifyNoMoreInteractions(dataSetDomainService);
    }

    @Test
    void searchDataSet() {

        when(folderEntitySearchDomainService.search(any(), any(), any(), any())).thenReturn(new FolderSearchResponse(Collections.emptyList(), Collections.emptyList()));

        var response = service.searchDataSet(new FolderSearchRequest());
        assertNotNull(response);

        verify(folderEntitySearchDomainService).search(any(), any(), any(), any());
        verifyNoMoreInteractions(folderEntitySearchDomainService);

    }

    @Test
    void changeDataSetParentFolder() {

        service.changeDataSetParentFolder(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(),any(),any());
        verify(dataSetDomainService).changeDataSetParentFolder(any());
        verifyNoMoreInteractions(permissionCheckerSystem,dataSetDomainService);
    }

    @Test
    void copyDataSets() {

        service.copyDataSets(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(),any(),any());
        verify(dataSetDomainService).copyDataSet(any(),any());
        verifyNoMoreInteractions(permissionCheckerSystem,dataSetDomainService);
    }

    private DataSetFolderResponse getFolderResponse() {
        return new DataSetFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.emptyList())
                .setDataSets(Collections.singletonList(new DataSetResponse()))
                .setAuthority(FolderAuthorityEnum.READ)
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

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private DataSetResponse getDataSetResponse(Long typeID) {
        return new DataSetResponse()
                .setId(ID)
                .setTypeId(typeID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCatalogName(CATALOG_NAME)
                .setObjectName(OBJECT_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setDataSource(new DataSourceResponse(ID, null, null, null, null, null, null, null, null, null))
                .setFields(Collections.singletonList(new DataSetFieldResponse()));
    }

    private DataSetAddRequest getDataSetAddRequest() {
        return new DataSetAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFolderId(FOLDER_ID)
                .setTypeId(TYPE_ID_TABLE)
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(OBJECT_NAME)
                .setFields(Collections.singletonList(new DataSetFieldAddRequest()));
    }

    private DataSetRequest getDataSetRequest() {
        return new DataSetRequest()
                .setId(ID);
    }

    private DataSetCreateFromMetaDataRequest getDataSetCreateFromMetaDataRequest(Long typeID) {
        return new DataSetCreateFromMetaDataRequest()
                .setDataSourceId(ID)
                .setTypeId(typeID)
                .setFolderId(FOLDER_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCatalogName(CATALOG_NAME)
                .setObjectName(OBJECT_NAME)
                .setSchemaName(SCHEMA_NAME);
    }

    private List<DataSetDataTypeResponse> getDataSetDataTypesResponse() {
        return Collections.singletonList(new DataSetDataTypeResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION));
    }

    private DataSetDependenciesResponse getDataSetDependenciesResponse() {
        return new DataSetDependenciesResponse(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Collections.singletonList(new ReportResponse().setId(ID)),
                Collections.singletonList(new FilterInstanceDependenciesResponse().setId(ID)),
                Collections.singletonList(new SecurityFilterResponse(ID, null, null, null, null, null, null, null, null, null, new ArrayList<>())),
                null,
                null,
                null, null);

    }

    private ChangeParentFolderRequest getChangeParentFolderRequest() {
        return new ChangeParentFolderRequest()
                .setDestFolderId(FOLDER_ID)
                .setObjIds(Collections.emptyList());
    }
}