package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceFieldAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterAddRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFieldResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceShortResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFieldResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filter.FilterChildNodesRequestDataMerger;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterDataFIMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFieldMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceMerger;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceQueryDataMerger;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceShortResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FolderNodeResponseFilterInstanceFolderMapper;
import ru.magnit.magreportbackend.repository.DataSetRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFieldRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceRepository;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInstanceDomainServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String RENAME = "rename";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterInstanceDomainService domainService;

    @Mock
    private FilterInstanceFolderRepository folderRepository;

    @Mock
    private FilterInstanceRepository filterInstanceRepository;

    @Mock
    private DataSetRepository dataSetRepository;

    @Mock
    private FilterInstanceFieldRepository fieldRepository;

    @Mock
    private FilterQueryExecutor queryExecutor;

    @Mock
    private FilterInstanceFolderResponseMapper filterInstanceFolderResponseMapper;

    @Mock
    private FilterInstanceFolderMapper filterInstanceFolderMapper;

    @Mock
    private FilterInstanceMapper filterInstanceMapper;

    @Mock
    private FilterInstanceResponseMapper filterInstanceResponseMapper;

    @Mock
    private FilterInstanceMerger filterInstanceMerger;

    @Mock
    private FilterInstanceFieldMapper filterInstanceFieldMapper;

    @Mock
    private FilterInstanceQueryDataMerger filterInstanceQueryDataMerger;

    @Mock
    private FilterDataFIMapper filterDataFIMapper;

    @Mock
    private FilterChildNodesRequestDataMerger filterChildNodesRequestDataMerger;

    @Mock
    private FolderNodeResponseFilterInstanceFolderMapper folderNodeResponseFilterInstanceFolderMapper;

    @Mock
    private FilterInstanceShortResponseMapper filterInstanceShortResponseMapper;

    @Test
    void getFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new FilterInstanceFolder());
        when(filterInstanceFolderResponseMapper.from((FilterInstanceFolder) any())).thenReturn(getFolderResponse());

        FilterInstanceFolderResponse response = domainService.getFolder(ID);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(filterInstanceFolderResponseMapper).from((FilterInstanceFolder) any());
        verifyNoMoreInteractions(filterInstanceFolderResponseMapper);


        when(folderRepository.getAllByParentFolderIsNull()).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFolder(null));

        verify(folderRepository).getAllByParentFolderIsNull();
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void addFolder() {
        when(filterInstanceFolderMapper.from((FolderAddRequest) any())).thenReturn(new FilterInstanceFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new FilterInstanceFolder().setId(ID));
        when(filterInstanceFolderResponseMapper.from((FilterInstanceFolder) any())).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        FilterInstanceFolderResponse response = domainService.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(filterInstanceFolderMapper).from((FolderAddRequest) any());
        verify(filterInstanceFolderResponseMapper).from((FilterInstanceFolder) any());
        verify(folderRepository).saveAndFlush(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(filterInstanceFolderMapper, filterInstanceFolderResponseMapper, folderRepository);
    }

    @Test
    void getChildFolders() {
        when(folderRepository.getAllByParentFolderId(ID)).thenReturn(Collections.singletonList(new FilterInstanceFolder()));
        when(filterInstanceFolderResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getFolderResponse()));

        List<FilterInstanceFolderResponse> responseList = domainService.getChildFolders(ID);

        assertNotNull(responseList);

        FilterInstanceFolderResponse response = responseList.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
        verify(filterInstanceFolderResponseMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(filterInstanceFolderResponseMapper);
    }

    @Test
    void renameFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new FilterInstanceFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new FilterInstanceFolder());
        when(filterInstanceFolderResponseMapper.from((FilterInstanceFolder) any())).thenReturn(getRenameFolderResponse());

        FilterInstanceFolderResponse response = domainService.renameFolder(getRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(RENAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).saveAndFlush(any());
        verifyNoMoreInteractions(folderRepository);
        verify(filterInstanceFolderResponseMapper).from((FilterInstanceFolder) any());
        verifyNoMoreInteractions(filterInstanceFolderResponseMapper);
    }

    @Test
    void deleteFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () -> domainService.deleteFolder(ID));

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository, filterInstanceRepository);

        Mockito.reset(folderRepository);

        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(filterInstanceRepository.existsByFolderId(anyLong())).thenReturn(false);

        domainService.deleteFolder(ID);

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(filterInstanceRepository).existsByFolderId(anyLong());
        verify(folderRepository).deleteById(anyLong());
        verifyNoMoreInteractions(folderRepository, filterInstanceRepository);

        Mockito.reset(folderRepository);

        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(true);

        assertThrows(InvalidParametersException.class, () -> domainService.deleteFolder(ID));

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository, filterInstanceRepository);
    }

    @Test
    void addFilterInstance() {

        FilterInstanceAddRequest request = getFilterInstanceAddRequest();
        var userView = new UserView().setId(ID);
        var template = new FilterTemplateResponse().setFields(Arrays.asList(
                new FilterTemplateFieldResponse().setId(ID).setType(FilterFieldTypeEnum.ID_FIELD),
                new FilterTemplateFieldResponse().setId(4L).setType(FilterFieldTypeEnum.CODE_FIELD)));

        assertThrows(InvalidParametersException.class, () -> domainService.addFilterInstance(userView, request, template));

        when(filterInstanceRepository.findAllByCode(any())).thenReturn(Collections.singletonList(new FilterInstance()));

        request.setCode("1234");
        assertThrows(InvalidParametersException.class, () -> domainService.addFilterInstance(userView, request, template));
        verify(filterInstanceRepository).findAllByCode(any());
        verifyNoMoreInteractions(filterInstanceRepository);

        Mockito.reset(filterInstanceRepository);

        when(filterInstanceRepository.findAllByCode(any())).thenReturn(Collections.emptyList());
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(filterInstanceMapper.from(any(FilterInstanceAddRequest.class))).thenReturn(getFilterInstance(null));
        when(dataSetRepository.getReferenceById(anyLong())).thenReturn(null);

        assertNotNull(domainService.addFilterInstance(userView, request, template));

        verify(filterInstanceRepository).findAllByCode(any());
        verify(folderRepository).existsById(anyLong());
        verify(filterInstanceMapper).from(any(FilterInstanceAddRequest.class));
        verify(filterInstanceRepository).save(any());
        verify(dataSetRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(filterInstanceRepository, folderRepository, filterInstanceMapper, dataSetRepository);

        Mockito.reset(filterInstanceRepository, folderRepository, filterInstanceMapper, dataSetRepository);

        when(filterInstanceRepository.findAllByCode(any())).thenReturn(Collections.emptyList());
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(filterInstanceMapper.from(any(FilterInstanceAddRequest.class))).thenReturn(getFilterInstance(getDataset()));

        request.setDataSetId(null);
        assertNotNull(domainService.addFilterInstance(userView, request, template));

        verify(filterInstanceRepository).findAllByCode(any());
        verify(folderRepository).existsById(anyLong());
        verify(filterInstanceMapper).from(any(FilterInstanceAddRequest.class));
        verify(filterInstanceRepository).save(any());
        verifyNoMoreInteractions(filterInstanceRepository, folderRepository, filterInstanceMapper, dataSetRepository);

        Mockito.reset(filterInstanceRepository, folderRepository, filterInstanceMapper, dataSetRepository);

        when(filterInstanceRepository.findAllByCode(any())).thenReturn(Collections.emptyList());
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(filterInstanceMapper.from(any(FilterInstanceAddRequest.class))).thenReturn(getFilterInstance(getProcedureDataset()));

        request.setDataSetId(null);
        assertThrows(InvalidParametersException.class, () -> domainService.addFilterInstance(userView, request, template));

        verify(filterInstanceRepository).findAllByCode(any());
        verify(folderRepository).existsById(anyLong());
        verify(filterInstanceMapper).from(any(FilterInstanceAddRequest.class));

        verifyNoMoreInteractions(filterInstanceRepository, folderRepository, filterInstanceMapper, dataSetRepository);
    }

    @Test
    void deleteFilterInstance() {

        when(filterInstanceRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () -> domainService.deleteFilterInstance(ID));

        Mockito.reset(filterInstanceRepository);

        when(filterInstanceRepository.existsById(anyLong())).thenReturn(true);

        domainService.deleteFilterInstance(ID);

        verify(filterInstanceRepository).existsById(anyLong());
        verify(filterInstanceRepository).deleteById(anyLong());
        verifyNoMoreInteractions(filterInstanceRepository);
    }

    @Test
    void getFilterInstance() {
        when(filterInstanceResponseMapper.from(any(FilterInstance.class))).thenReturn(getFilterInstanceResponse());
        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()));

        assertNotNull(domainService.getFilterInstance(ID));

        verify(filterInstanceResponseMapper).from(any(FilterInstance.class));
        verify(filterInstanceRepository).getReferenceById(any());

        verifyNoMoreInteractions(filterInstanceResponseMapper, filterInstanceRepository);
    }

    @Test
    void editFilterInstance() {

        var template = new FilterTemplateResponse().setFields(Arrays.asList(
                new FilterTemplateFieldResponse()
                        .setId(ID)
                        .setType(FilterFieldTypeEnum.ID_FIELD),
                new FilterTemplateFieldResponse()
                        .setId(ID)
                        .setType(FilterFieldTypeEnum.CODE_FIELD)));

        var request = getFilterInstanceAddRequest();

        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()));

        assertThrows(InvalidParametersException.class, () -> domainService.editFilterInstance(request, template));

        when(filterInstanceFieldMapper.from(any(FilterInstanceFieldAddRequest.class))).thenReturn(new FilterInstanceField());

        request.setCode("1234");

        assertNotNull(domainService.editFilterInstance(request, template));

        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()).setSecurityFilters(Collections.singletonList(new SecurityFilter())));
        assertThrows(InvalidParametersException.class, () -> domainService.editFilterInstance(request, template));
    }

    @Test
    void getFilterInstanceValues() {

        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()));
        when(filterInstanceQueryDataMerger.merge(any(FilterInstance.class), any())).thenReturn(getFilterValueListRequestData());
        when(queryExecutor.getFilterInstanceValuesQuery(any())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFilterInstanceValues(new ListValuesRequest()));

        verify(filterInstanceRepository).getReferenceById(any());
        verify(filterInstanceQueryDataMerger).merge(any(FilterInstance.class), any());
        verify(queryExecutor).getFilterInstanceValuesQuery(any());

        verifyNoMoreInteractions(filterInstanceRepository, queryExecutor, filterInstanceQueryDataMerger);
    }

    @Test
    void getChildNodes() {

        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()));
        when(filterDataFIMapper.from(any(FilterInstance.class))).thenReturn(getFilterData());
        when(filterChildNodesRequestDataMerger.merge(any(), any(), any())).thenReturn(getFilterChildNodesRequestData());
        when(queryExecutor.getFilterInstanceChildNodes(any())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getChildNodes(new ChildNodesRequest().setFilterId(ID), Collections.emptyList()));

        verify(filterInstanceRepository).getReferenceById(any());
        verify(filterDataFIMapper).from(any(FilterInstance.class));
        verify(filterChildNodesRequestDataMerger).merge(any(), any(), any());
        verify(queryExecutor).getFilterInstanceChildNodes(any());
        verifyNoMoreInteractions(queryExecutor, filterInstanceRepository, filterChildNodesRequestDataMerger, filterChildNodesRequestDataMerger, filterDataFIMapper);
    }

    @Test
    void addMissingFields() {

        assertNotNull(domainService.addMissingFields(new FilterGroupAddRequest().setFilters(null)));

        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()));
        assertNotNull(domainService.addMissingFields(getFilterGroupAddRequest()));

        verify(filterInstanceRepository, times(2)).getReferenceById(any());
        verifyNoMoreInteractions(filterInstanceRepository);
    }

    @Test
    void getDataSetId() {
        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()));

        assertNotNull(domainService.getDataSetId(ID));

        verify(filterInstanceRepository).getReferenceById(any());
        verifyNoMoreInteractions(filterInstanceRepository);
    }

    @Test
    void changeParentFolder() {
        when(folderRepository.getReferenceById(any())).thenReturn(new FilterInstanceFolder());
        when(filterInstanceFolderResponseMapper.from(any(FilterInstanceFolder.class))).thenReturn(new FilterInstanceFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        assertNotNull(domainService.changeParentFolder(new FolderChangeParentRequest().setId(ID).setParentId(ID)));

        verify(folderRepository).getReferenceById(any());
        verify(folderRepository).save(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void getPathFilter() {

        when(filterInstanceRepository.getReferenceById(any())).thenReturn(getFilterInstance(getDataset()));
        when(folderNodeResponseFilterInstanceFolderMapper.from(any(FilterInstanceFolder.class))).thenReturn(new FolderNodeResponse(ID, ID, "", "", LocalDateTime.now(), LocalDateTime.now()));

        assertNotNull(domainService.getPathFilter(ID));

        verify(filterInstanceRepository).getReferenceById(any());
        verify(folderNodeResponseFilterInstanceFolderMapper, times(2)).from(any(FilterInstanceFolder.class));
        verifyNoMoreInteractions(folderRepository, folderNodeResponseFilterInstanceFolderMapper);
    }

    @Test
    void checkFilterExistsForDataset() {
        when(filterInstanceShortResponseMapper.from(any(FilterInstance.class))).thenReturn(new FilterInstanceShortResponse());
        when(filterInstanceRepository.findByDataSetId(anyLong())).thenReturn(Collections.singletonList(getFilterInstance(getDataset())));

        assertNotNull(domainService.checkFilterExistsForDataset(ID));

        verify(filterInstanceShortResponseMapper).from(any(FilterInstance.class));
        verify(filterInstanceRepository).findByDataSetId(anyLong());
        verifyNoMoreInteractions(filterInstanceShortResponseMapper, filterInstanceRepository);
    }

    private FolderRenameRequest getRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION);
    }

    private FilterInstanceFolderResponse getRenameFolderResponse() {
        return new FilterInstanceFolderResponse()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new FilterInstanceFolderResponse()))
                .setFilterInstances(Collections.singletonList(new FilterInstanceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FilterInstanceResponse getFilterInstanceResponse() {
        return new FilterInstanceResponse()
                .setId(ID)
                .setTemplateId(ID)
                .setDataSetId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFields(Arrays.asList(
                        new FilterInstanceFieldResponse().setId(0L).setType(FilterFieldTypeEnum.ID_FIELD),
                        new FilterInstanceFieldResponse().setId(4L).setType(FilterFieldTypeEnum.ID_FIELD)))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FilterInstanceAddRequest getFilterInstanceAddRequest() {
        return new FilterInstanceAddRequest()
                .setId(ID)
                .setFolderId(ID)
                .setFolderId(ID)
                .setTemplateId(ID)
                .setDataSetId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCode("")
                .setFields(Arrays.asList(
                        new FilterInstanceFieldAddRequest()
                                .setType(FilterFieldTypeEnum.ID_FIELD),
                        new FilterInstanceFieldAddRequest()
                                .setType(FilterFieldTypeEnum.CODE_FIELD)));
    }

    private FilterInstanceFolderResponse getFolderResponse() {
        return new FilterInstanceFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new FilterInstanceFolderResponse()))
                .setFilterInstances(Collections.singletonList(new FilterInstanceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FilterInstance getFilterInstance(DataSet dataSet) {
        return new FilterInstance()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCode("1234")
                .setDataSet(dataSet)
                .setFolder(new FilterInstanceFolder().setParentFolder(new FilterInstanceFolder()))
                .setFields(new ArrayList<>(Collections.singletonList(
                        new FilterInstanceField()
                                .setId(ID)
                                .setName(NAME)
                                .setDescription(DESCRIPTION)
                                .setTemplateField(
                                        new FilterTemplateField()
                                                .setType(new FilterFieldType().setId(0L))
                                ))));
    }

    private DataSet getDataset() {
        return new DataSet()
                .setId(ID)
                .setType(new DataSetType().setId(0L));
    }

    private DataSet getProcedureDataset() {
        return new DataSet()
                .setId(ID)
                .setType(new DataSetType().setId(1L));
    }

    private FilterValueListRequestData getFilterValueListRequestData() {
        return new FilterValueListRequestData(null, null, null, null,
                null, null, null, null, null, null,
                null, false, null, 1, null);
    }

    private FilterChildNodesRequestData getFilterChildNodesRequestData() {
        return new FilterChildNodesRequestData(
                null, null, null, 1L, null, null, null,
                1L, null, null);
    }

    private FilterData getFilterData() {
        return new FilterData(null, 1L, null, null, null, null,
                null, null, null);
    }

    private FilterGroupAddRequest getFilterGroupAddRequest() {
        return new FilterGroupAddRequest()
                .setFilters(Arrays.asList(
                        new FilterAddRequest()
                                .setFields(new ArrayList<>()),
                        new FilterAddRequest()
                                .setFields(new ArrayList<>())
                ));
    }


}
