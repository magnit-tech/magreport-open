package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.dataset.DataSetAddRequestMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetDataTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetDependenciesResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldAddRequestMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetMerger;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.FolderNodeResponseDataSetFolderMapper;
import ru.magnit.magreportbackend.repository.DataSetDataTypeRepository;
import ru.magnit.magreportbackend.repository.DataSetFieldRepository;
import ru.magnit.magreportbackend.repository.DataSetFolderRepository;
import ru.magnit.magreportbackend.repository.DataSetRepository;
import ru.magnit.magreportbackend.repository.DataSetTypeRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSetDomainServiceTest {

    private final Long ID = 1L;
    private final Long DATASOURCE_ID = 1L;
    private final Long FOLDER_ID = 2L;
    private final Long TYPE_ID = 1L;
    private final String NAME = "Test folder";
    private final String RENAME = "rename";
    private final String DESCRIPTION = "Folder description";
    private final String CATALOG_NAME = "catalog";
    private final String SCHEMA_NAME = "schema";
    private final String OBJECT_NAME = "object";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private DataSetDomainService service;

    @Mock
    private DataSetFolderRepository folderRepository;

    @Mock
    private DataSetRepository dataSetRepository;

    @Mock
    private DataSetDataTypeRepository dataSetDataTypeRepository;

    @Mock
    private DataSetFolderResponseMapper dataSetFolderResponseMapper;

    @Mock
    private DataSetFolderMapper dataSetFolderMapper;

    @Mock
    private DataSetResponseMapper dataSetResponseMapper;

    @Mock
    private DataSetMapper dataSetMapper;

    @Mock
    private DataSetMerger dataSetMerger;

    @Mock
    private DataSetAddRequestMapper dataSetAddRequestMapper;

    @Mock
    private DataSetFieldAddRequestMapper dataSetFieldAddRequestMapper;

    @Mock
    private DataSetDataTypeResponseMapper dataSetDataTypeResponseMapper;

    @Mock
    private DataSetTypeResponseMapper dataSetTypeResponseMapper;

    @Mock
    private DataSetTypeRepository dataSetTypeRepository;

    @Mock
    private DataSetFieldRepository dataSetFieldRepository;

    @Mock
    private DataSetFieldMapper dataSetFieldMapper;

    @Mock
    private DataSetFieldResponseMapper dataSetFieldResponseMapper;

    @Mock
    private DataSetDependenciesResponseMapper dataSetDependenciesResponseMapper;

    @Mock
    private FolderNodeResponseDataSetFolderMapper folderNodeMapper;

    @Test
    void getFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new DataSetFolder());
        when(dataSetFolderResponseMapper.from((DataSetFolder) any())).thenReturn(getFolderResponse());

        DataSetFolderResponse response = service.getFolder(ID);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetFolderResponseMapper).from((DataSetFolder) any());
        verifyNoMoreInteractions(dataSetFolderResponseMapper);
        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);

        when(folderRepository.getAllByParentFolderIsNull()).thenReturn(Collections.emptyList());

        assertNotNull(service.getFolder(null));

        verify(folderRepository).getAllByParentFolderIsNull();
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void addFolder() {
        when(folderRepository.saveAndFlush(any())).thenReturn(new DataSetFolder().setId(ID));
        when(dataSetFolderMapper.from((FolderAddRequest) any())).thenReturn(new DataSetFolder());
        when(dataSetFolderResponseMapper.from((DataSetFolder) any())).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(service, "maxLevel", 128L);

        DataSetFolderResponse response = service.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetFolderMapper).from((FolderAddRequest) any());
        verify(dataSetFolderResponseMapper).from((DataSetFolder) any());
        verify(folderRepository).saveAndFlush(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(dataSetFolderMapper, folderRepository, dataSetFolderResponseMapper);
    }

    @Test
    void getChildFolders() {
        when(folderRepository.getAllByParentFolderId(ID)).thenReturn(Collections.singletonList(new DataSetFolder()));
        when(dataSetFolderResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getFolderResponse()));

        List<DataSetFolderResponse> responseList = service.getChildFolders(ID);

        assertNotNull(responseList);

        DataSetFolderResponse response = responseList.get(0);
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getDataSets());
        assertNotNull(response.getChildFolders());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
        verify(dataSetFolderResponseMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(dataSetFolderResponseMapper);
    }

    @Test
    void renameFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new DataSetFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new DataSetFolder());
        when(dataSetFolderResponseMapper.from((DataSetFolder) any())).thenReturn(getRenameFolderResponse());

        DataSetFolderResponse response = service.renameFolder(getRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(RENAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSets());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSetFolderResponseMapper).from((DataSetFolder) any());
        verifyNoMoreInteractions(dataSetFolderMapper);
        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).saveAndFlush(any());
        verifyNoMoreInteractions(folderRepository);

    }

    @Test
    void getDataSet() {
        when(dataSetRepository.existsById(anyLong())).thenReturn(true);
        when(dataSetRepository.getReferenceById(anyLong())).thenReturn(new DataSet());
        when(dataSetResponseMapper.from((DataSet) any())).thenReturn(getDataSetResponse());

        DataSetResponse response = service.getDataSet(ID);

        assertEquals(ID, response.getId());
        assertEquals(TYPE_ID, response.getTypeId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CATALOG_NAME, response.getCatalogName());
        assertEquals(SCHEMA_NAME, response.getSchemaName());
        assertEquals(OBJECT_NAME, response.getObjectName());
        assertNotNull(response.getFields());

        verify(dataSetRepository).existsById(anyLong());
        verifyNoMoreInteractions(dataSetRepository);
        verify(dataSetRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(dataSetRepository);
        verify(dataSetResponseMapper).from((DataSet) any());
        verifyNoMoreInteractions(dataSetResponseMapper);
    }

    @Test
    void createDataSetFromMetaData() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(dataSetRepository.save(any())).thenReturn(new DataSet(ID));
        when(dataSetAddRequestMapper.from((DataSetCreateFromMetaDataRequest) any())).thenReturn(new DataSetAddRequest());
        when(dataSetFieldAddRequestMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSetFieldAddRequest()));
        when(dataSetMapper.from((DataSetAddRequest) any())).thenReturn(new DataSet());

        var response = service.createDataSetFromMetaData(new UserView().setId(1L), getDataSetCreateFromMetaDataRequest(), Collections.singletonList(new ObjectFieldResponse()));

        assertEquals(ID, response);

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(dataSetRepository).save(any());
        verifyNoMoreInteractions(dataSetRepository);
        verifyNoMoreInteractions(dataSetResponseMapper);
        verify(dataSetAddRequestMapper).from((DataSetCreateFromMetaDataRequest) any());
        verifyNoMoreInteractions(dataSetAddRequestMapper);
        verify(dataSetFieldAddRequestMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(dataSetFieldAddRequestMapper);
        verify(dataSetMapper).from((DataSetAddRequest) any());
        verifyNoMoreInteractions(dataSetMapper);
    }

    @Test
    void getDataSetDataTypes() {
        when(dataSetDataTypeRepository.findAll()).thenReturn(Collections.singletonList(new DataType()));
        when(dataSetDataTypeResponseMapper.from(anyList())).thenReturn(getDataSetDataTypesResponse());

        List<DataSetDataTypeResponse> response = service.getDataSetDataTypes();
        assertEquals(1, response.size());
        assertEquals(ID, response.get(0).getId());
        assertEquals(NAME, response.get(0).getName());
        assertEquals(DESCRIPTION, response.get(0).getDescription());

        verify(dataSetDataTypeRepository).findAll();
        verifyNoMoreInteractions(dataSetDataTypeRepository);
        verify(dataSetDataTypeResponseMapper).from(anyList());
        verifyNoMoreInteractions(dataSetDataTypeResponseMapper);
    }

    @Test
    void checkFolderExists() {

        when(folderRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(InvalidParametersException.class, () -> service.deleteFolder(ID));

        verify(folderRepository).existsById(any());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkFolderEmpty() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(true);

        assertThrows(InvalidParametersException.class, () -> service.deleteFolder(ID));

        verify(folderRepository).existsById(any());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository);

        Mockito.reset(folderRepository);

        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(dataSetRepository.existsByFolderId(anyLong())).thenReturn(true);

        assertThrows(InvalidParametersException.class, () -> service.deleteFolder(ID));

        verify(folderRepository).existsById(any());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(dataSetRepository).existsByFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void deleteFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(dataSetRepository.existsByFolderId(anyLong())).thenReturn(false);

        service.deleteFolder(ID);

        verify(folderRepository).existsById(any());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(dataSetRepository).existsByFolderId(anyLong());
        verify(folderRepository).deleteById(any());
        verifyNoMoreInteractions(folderRepository);

    }

    @Test
    void editDataSet() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(dataSetRepository.getReferenceById(any())).thenReturn(getDataset(TYPE_ID));
        when(dataSetMerger.merge(any(), any())).thenReturn(getDataset(TYPE_ID));

        service.editDataSet(getDataSetAddRequest());

        verify(folderRepository).existsById(any());
        verify(dataSetRepository).getReferenceById(any());
        verify(dataSetMerger).merge(any(), any());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkChangeDataSetType() {

        var request = new DataSetType().setId(0L);
        var current = new DataSetType().setId(1L);

        assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(service, "checkChangeDataSetType", request, current));
    }

    @Test
    void checkDataSetExists() {

        when(dataSetRepository.existsById(any())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(service, "checkDataSetExists", ID));

        verify(dataSetRepository).existsById(any());
        verifyNoMoreInteractions(dataSetRepository);
    }

    @Test
    void deleteDataSet() {

        when(dataSetRepository.existsById(any())).thenReturn(true);

        service.deleteDataSet(ID);

        verify(dataSetRepository).existsById(any());
        verify(dataSetRepository).deleteById(any());
        verifyNoMoreInteractions(dataSetRepository);
    }

    @Test
    void getDataSetTypes() {
        when(dataSetTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(dataSetTypeResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(service.getDataSetTypes());

        verify(dataSetTypeRepository).findAll();
        verify(dataSetTypeResponseMapper).from(anyList());
        verifyNoMoreInteractions(dataSetTypeRepository, dataSetTypeResponseMapper);
    }

    @Test
    void refreshDataSet() {

        assertNotNull(service.refreshDataSet(getDataSetResponse().setTypeId(0L), Collections.emptyList()));

        verify(dataSetFieldRepository).markFieldSync(anyLong(), any());

        verify(dataSetFieldRepository).saveAll(any());
        verifyNoMoreInteractions(dataSetFieldRepository);
    }

    @Test
    void getUnlinkedInvalidFields() {

        when(dataSetRepository.getReferenceById(any())).thenReturn(getDataset(TYPE_ID));

        assertNotNull(service.getUnlinkedInvalidFields(getDataSetResponse(), Collections.emptyList()));

        verify(dataSetRepository).getReferenceById(any());
        verifyNoMoreInteractions(dataSetRepository);
    }

    @Test
    void deleteFields() {

        service.deleteFields(Collections.singletonList(ID));

        verify(dataSetFieldRepository).deleteAllByIdIn(anyList());
        verifyNoMoreInteractions(dataSetFieldRepository);
    }

    @Test
    void getReportIds() {

        when(dataSetRepository.getReferenceById(any())).thenReturn(getDataset(TYPE_ID));

        assertNotNull(service.getReportIds(ID));

        verify(dataSetRepository).getReferenceById(any());
        verifyNoMoreInteractions(dataSetRepository);

    }

    @Test
    void getDataSetDependants() {

        when(dataSetRepository.getReferenceById(any())).thenReturn(getDataset(TYPE_ID));
        when(dataSetDependenciesResponseMapper.from(any(DataSet.class))).thenReturn(new DataSetDependenciesResponse());

        assertNotNull(service.getDataSetDependants(ID));

        verify(dataSetRepository).getReferenceById(any());
        verify(dataSetDependenciesResponseMapper).from(any(DataSet.class));
        verifyNoMoreInteractions(dataSetRepository, dataSetDependenciesResponseMapper);

    }

    @Test
    void actualizeDataSet() {
        when(dataSetRepository.getReferenceById(any())).thenReturn(getDataset(TYPE_ID));

        service.actualizeDataSet(ID);

        verify(dataSetRepository).getReferenceById(any());
        verify(dataSetRepository).save(any());
        verifyNoMoreInteractions(dataSetRepository);
    }

    @Test
    void addDataSetFromProcedure() {

        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(dataSetAddRequestMapper.from(any(DataSetCreateFromMetaDataRequest.class))).thenReturn(getDataSetAddRequest());
        when(dataSetMapper.from(any(DataSetAddRequest.class))).thenReturn(getDataset(TYPE_ID));
        when(dataSetRepository.save(any())).thenReturn(getDataset(TYPE_ID));

        assertNotNull(service.addDataSetFromProcedure(new UserView().setId(ID), getDataSetCreateFromMetaDataRequest(), Collections.emptyList()));

        verify(folderRepository).existsById(any());
        verify(dataSetAddRequestMapper).from(any(DataSetCreateFromMetaDataRequest.class));
        verify(dataSetMapper).from(any(DataSetAddRequest.class));
        verify(dataSetRepository).save(any());
        verifyNoMoreInteractions(folderRepository, dataSetAddRequestMapper, dataSetMapper, dataSetRepository);
    }

    @Test
    void checkDataSetTypeProcedure() {
        assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(service, "checkDataSetTypeProcedure", 0L));
    }

    @Test
    void changeParentFolder() {

        when(folderRepository.getReferenceById(any())).thenReturn(new DataSetFolder());
        when(dataSetFolderResponseMapper.from(any(DataSetFolder.class))).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(service, "maxLevel", 128L);

        assertNotNull(service.changeParentFolder(new FolderChangeParentRequest().setId(ID)));

        verify(folderRepository).getReferenceById(any());
        verify(dataSetFolderResponseMapper).from(any(DataSetFolder.class));
        verify(folderRepository).save(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderRepository, dataSetFolderResponseMapper);

    }


    private List<DataSetDataTypeResponse> getDataSetDataTypesResponse() {
        return Collections.singletonList(new DataSetDataTypeResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION));
    }

    private DataSetFolderResponse getFolderResponse() {
        return new DataSetFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSetFolderResponse()))
                .setDataSets(Collections.singletonList(new DataSetResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FolderRenameRequest getRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION);
    }

    private DataSetFolderResponse getRenameFolderResponse() {
        return new DataSetFolderResponse()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSetFolderResponse()))
                .setDataSets(Collections.singletonList(new DataSetResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private DataSetResponse getDataSetResponse() {
        return new DataSetResponse()
                .setId(ID)
                .setTypeId(TYPE_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCatalogName(CATALOG_NAME)
                .setObjectName(OBJECT_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setFields(Collections.singletonList(
                        new DataSetFieldResponse()
                                .setId(ID)
                                .setName(NAME)
                                .setTypeName(NAME)));
    }

    private DataSetAddRequest getDataSetAddRequest() {
        return new DataSetAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFolderId(FOLDER_ID)
                .setTypeId(TYPE_ID)
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(OBJECT_NAME)
                .setFields(Collections.singletonList(new DataSetFieldAddRequest()));
    }

    private DataSetCreateFromMetaDataRequest getDataSetCreateFromMetaDataRequest() {
        return new DataSetCreateFromMetaDataRequest()
                .setDataSourceId(DATASOURCE_ID)
                .setTypeId(TYPE_ID)
                .setFolderId(FOLDER_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCatalogName(CATALOG_NAME)
                .setObjectName(OBJECT_NAME)
                .setSchemaName(SCHEMA_NAME);
    }

    private DataSet getDataset(Long idType) {
        return new DataSet()
                .setId(ID)
                .setType(new DataSetType().setId(idType))
                .setFolder(new DataSetFolder());
    }


}
