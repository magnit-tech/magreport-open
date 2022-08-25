package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceDependenciesResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceMerger;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;
import ru.magnit.magreportbackend.repository.DataSourceFolderRepository;
import ru.magnit.magreportbackend.repository.DataSourceRepository;
import ru.magnit.magreportbackend.repository.DataSourceTypeRepository;

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
class DataSourceDomainServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String RENAME = "rename";
    private static final String DESCRIPTION = "description";
    private static final String URL = "URL";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "pass";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);
    public static final short POOL_SIZE = (short) 5;

    @InjectMocks
    private DataSourceDomainService domainService;

    @Mock
    private DataSourceFolderRepository folderRepository;

    @Mock
    private DataSourceRepository dataSourceRepository;

    @Mock
    private DataSourceFolderResponseMapper dataSourceFolderResponseMapper;

    @Mock
    private DataSourceFolderMapper dataSourceFolderMapper;

    @Mock
    private DataSourceMapper dataSourceMapper;

    @Mock
    private DataSourceMerger dataSourceMerger2;

    @Mock
    private DataSourceResponseMapper dataSourceResponseMapper;

    @Mock
    private DataSourceViewMapper dataSourceViewMapper;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private DataSourceTypeRepository dataSourceTypeRepository;

    @Mock
    private DataSourceTypeResponseMapper dataSourceTypeResponseMapper;

    @Mock
    private DataSourceDependenciesResponseMapper dataSourceDependenciesResponseMapper;

    @Test
    void getFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(dataSourceFolderResponseMapper.from(any(DataSourceFolder.class))).thenReturn(getFolderResponse());
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new DataSourceFolder());

        DataSourceFolderResponse response = domainService.getFolder(ID);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());


        verify(dataSourceFolderResponseMapper).from(any(DataSourceFolder.class));
        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository, dataSourceFolderResponseMapper);

        Mockito.reset(folderRepository, dataSourceFolderResponseMapper);

        when(dataSourceFolderResponseMapper.shallowMap(anyList())).thenReturn(Collections.emptyList());
        when(folderRepository.getAllByParentFolderIsNull()).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFolder(null));

        verify(folderRepository).getAllByParentFolderIsNull();
        verify(dataSourceFolderResponseMapper).shallowMap(anyList());
        verifyNoMoreInteractions(folderRepository, dataSourceFolderResponseMapper);
    }

    @Test
    void addFolder() {
        when(dataSourceFolderMapper.from((FolderAddRequest) any())).thenReturn(new DataSourceFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new DataSourceFolder().setId(ID));
        when(dataSourceFolderResponseMapper.from((DataSourceFolder) any())).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        DataSourceFolderResponse response = domainService.addFolder(getAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceFolderMapper).from((FolderAddRequest) any());
        verify(folderRepository).saveAndFlush(any());
        verify(dataSourceFolderResponseMapper).from((DataSourceFolder) any());
        verifyNoMoreInteractions(dataSourceFolderMapper, folderRepository, dataSourceFolderResponseMapper);
    }

    @Test
    void getChildFolders() {
        when(folderRepository.getAllByParentFolderId(ID)).thenReturn(Collections.singletonList(new DataSourceFolder()));
        when(dataSourceFolderResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getFolderResponse()));

        List<DataSourceFolderResponse> responseList = domainService.getChildFolders(ID);

        assertNotNull(responseList);

        DataSourceFolderResponse response = responseList.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
        verify(dataSourceFolderResponseMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(dataSourceFolderResponseMapper);
    }

    @Test
    void renameFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new DataSourceFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new DataSourceFolder());
        when(dataSourceFolderResponseMapper.from((DataSourceFolder) any())).thenReturn(getRenameFolderResponse());

        DataSourceFolderResponse response = domainService.renameFolder(getRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(RENAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getDataSources());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(dataSourceFolderResponseMapper).from((DataSourceFolder) any());
        verifyNoMoreInteractions(dataSourceFolderMapper);
        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).saveAndFlush(any());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void addDataSource() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(dataSourceMapper.from((DataSourceAddRequest) any())).thenReturn(new DataSource());
        when(dataSourceRepository.save(any())).thenReturn(new DataSource().setId(ID));

        Long response = domainService.addDataSource(new UserView().setId(1L), getDataSourceAddRequest());

        assertEquals(ID, response);

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(dataSourceRepository).save(any());
        verifyNoMoreInteractions(dataSourceRepository);
        verify(dataSourceMapper).from((DataSourceAddRequest) any());
        verifyNoMoreInteractions(dataSourceMapper);
    }

    @Test
    void editDataSource() {
        when(dataSourceRepository.existsById(anyLong())).thenReturn(true);
        when(dataSourceRepository.getReferenceById(anyLong())).thenReturn(new DataSource());
        when(dataSourceRepository.saveAndFlush(any())).thenReturn(new DataSource());
        when(dataSourceMerger2.merge(any(), any())).thenReturn(new DataSource());
        when(dataSourceResponseMapper.from((DataSource) any())).thenReturn(getDataSourceResponse());

        DataSourceResponse response = domainService.editDataSource(getDataSourceAddRequest());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(URL, response.url());
        assertEquals(USER_NAME, response.userName());
        assertEquals(CREATED_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());

        verify(dataSourceRepository).existsById(anyLong());
        verify(dataSourceRepository).getReferenceById(any());
        verify(dataSourceRepository).saveAndFlush(any());
        verifyNoMoreInteractions(dataSourceRepository);
        verify(dataSourceMerger2).merge(any(), any());
        verifyNoMoreInteractions(dataSourceMerger2);
        verify(dataSourceResponseMapper).from((DataSource) any());
        verifyNoMoreInteractions(dataSourceResponseMapper);
    }

    @Test
    void getDataSource() {
        when(dataSourceRepository.existsById(anyLong())).thenReturn(true);
        when(dataSourceRepository.getReferenceById(anyLong())).thenReturn(new DataSource());
        when(dataSourceResponseMapper.from((DataSource) any())).thenReturn(getDataSourceResponse());

        DataSourceResponse response = domainService.getDataSource(ID);

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(URL, response.url());
        assertEquals(USER_NAME, response.userName());
        assertEquals(CREATED_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());

        verify(dataSourceRepository).existsById(anyLong());
        verify(dataSourceRepository).getReferenceById(any());
        verifyNoMoreInteractions(dataSourceRepository);
        verify(dataSourceResponseMapper).from((DataSource) any());
        verifyNoMoreInteractions(dataSourceResponseMapper);
    }

    @Test
    void getDataSourceView() {
        when(dataSourceRepository.existsById(anyLong())).thenReturn(true);
        when(dataSourceRepository.getReferenceById(anyLong())).thenReturn(new DataSource());
        when(dataSourceViewMapper.from((DataSource) any())).thenReturn(getView());

        DataSourceData view = domainService.getDataSourceView(ID);

        assertEquals(ID, view.id());
        assertEquals(USER_NAME, view.userName());
        assertEquals(URL, view.url());
        assertEquals(PASSWORD, view.password());

        verify(dataSourceRepository).existsById(anyLong());
        verify(dataSourceRepository).getReferenceById(any());
        verifyNoMoreInteractions(dataSourceRepository);
        verify(dataSourceViewMapper).from((DataSource) any());
        verifyNoMoreInteractions(dataSourceViewMapper);
    }

    @Test
    void deleteFolder() {

        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(dataSourceRepository.existsByFolderId(anyLong())).thenReturn(false);

        domainService.deleteFolder(ID);

        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(dataSourceRepository).existsByFolderId(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).deleteById(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkFolderExists() {

        when(folderRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(domainService, "checkFolderExists", ID));

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkFolderEmpty() {

        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(true);
        assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(domainService, "checkFolderEmpty", ID));

        verify(folderRepository).existsByParentFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository);

        Mockito.reset(folderRepository);

        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(dataSourceRepository.existsByFolderId(anyLong())).thenReturn(true);
        assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(domainService, "checkFolderEmpty", ID));

        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(dataSourceRepository).existsByFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository, dataSourceRepository);

    }

    @Test
    void checkDataSourceExists() {

        when(dataSourceRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "checkDataSourceExists", ID));

        verify(dataSourceRepository).existsById(anyLong());
        verifyNoMoreInteractions(dataSourceRepository);
    }

    @Test
    void deleteDataSource() {

        when(dataSourceRepository.existsById(anyLong())).thenReturn(true);

        domainService.deleteDataSource(ID);

        verify(dataSourceRepository).existsById(anyLong());
        verify(dataSourceRepository).deleteById(anyLong());
        verifyNoMoreInteractions(dataSourceRepository);
    }

    @Test
    void getDataSourceTypes() {

        when(dataSourceTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(dataSourceTypeResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getDataSourceTypes());

        verify(dataSourceTypeRepository).findAll();
        verify(dataSourceTypeResponseMapper).from(anyList());
        verifyNoMoreInteractions(dataSourceTypeRepository, dataSourceTypeResponseMapper);

    }

    @Test
    void getDataSourceDependencies() {

        when(dataSourceRepository.getReferenceById(anyLong())).thenReturn(new DataSource());
        when(dataSourceDependenciesResponseMapper.from(any(DataSource.class))).thenReturn(new DataSourceDependenciesResponse());

        assertNotNull(domainService.getDataSourceDependencies(ID));

        verify(dataSourceRepository).getReferenceById(anyLong());
        verify(dataSourceDependenciesResponseMapper).from(any(DataSource.class));

        verifyNoMoreInteractions(dataSourceRepository, dataSourceDependenciesResponseMapper);
    }

    @Test
    void changeParentFolder() {

        when(folderRepository.getReferenceById(anyLong())).thenReturn(new DataSourceFolder());
        when(dataSourceFolderResponseMapper.from(any(DataSourceFolder.class))).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        assertNotNull(domainService.changeParentFolder(new FolderChangeParentRequest().setId(ID)));

        verify(folderRepository).getReferenceById(anyLong());
        verify(dataSourceFolderResponseMapper).from(any(DataSourceFolder.class));
        verify(folderRepository).save(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderRepository, dataSourceFolderResponseMapper);
    }

    private DataSourceData getView() {
        return new DataSourceData(
                ID,
                DataSourceTypeEnum.H2,
                URL,
                USER_NAME,
                PASSWORD,
                POOL_SIZE);
    }

    private DataSourceFolderResponse getFolderResponse() {
        return new DataSourceFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSourceFolderResponse()))
                .setDataSources(Collections.singletonList(new DataSourceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderAddRequest getAddRequest() {
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

    private DataSourceAddRequest getDataSourceAddRequest() {
        return new DataSourceAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setUrl(URL)
                .setUserName(USER_NAME)
                .setPassword(PASSWORD)
                .setTypeId(ID)
                .setFolderId(ID);
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
}
