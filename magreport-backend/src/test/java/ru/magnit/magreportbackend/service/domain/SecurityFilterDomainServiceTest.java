package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.RoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterRoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterDataFIMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobFilterDataSFMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.FolderNodeResponseSecurityFilterFolderMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterMerger;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterRoleMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterRoleSettingsResponseMapper;
import ru.magnit.magreportbackend.repository.SecurityFilterDataSetFieldRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterDataSetRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterRoleRepository;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityFilterDomainServiceTest {

    @Mock
    private SecurityFilterRepository repository;

    @Mock
    private SecurityFilterRoleRepository roleRepository;

    @Mock
    private SecurityFilterFolderRepository folderRepository;

    @Mock
    private SecurityFilterDataSetRepository dataSetRepository;

    @Mock
    private SecurityFilterDataSetFieldRepository dataSetFieldRepository;

    @Mock
    private FilterQueryExecutor queryExecutor;

    @Mock
    private SecurityFilterMapper securityFilterMapper;

    @Mock
    private SecurityFilterResponseMapper securityFilterResponseMapper;

    @Mock
    private SecurityFilterFolderResponseMapper securityFilterFolderResponseMapper;

    @Mock
    private SecurityFilterFolderMapper securityFilterFolderMapper;

    @Mock
    private SecurityFilterRoleMapper securityFilterRoleMapper;

    @Mock
    private SecurityFilterRoleSettingsResponseMapper securityFilterRoleSettingsResponseMapper;

    @Mock
    private FilterDataFIMapper filterDataFIMapper;

    @Mock
    private SecurityFilterMerger securityFilterMerger;

    @Mock
    private ReportJobFilterDataSFMapper reportJobFilterDataSFMapper;

    @Mock
    private FolderNodeResponseSecurityFilterFolderMapper folderNodeResponseSecurityFilterFolderMapper;

    @InjectMocks
    SecurityFilterDomainService domainService;

    private final static Long ID = 1L;
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static LocalDateTime NOW = LocalDateTime.now();

    @Test
    void addSecurityFilter() {
        when(securityFilterMapper.from(any(SecurityFilterAddRequest.class))).thenReturn(getFilter());
        when(repository.save(any())).thenReturn(getFilter());

        assertNotNull(domainService.addSecurityFilter(new UserView().setId(ID), getSecurityFilterAddRequest()));

        verify(securityFilterMapper).from(any(SecurityFilterAddRequest.class));
        verify(repository).save(any());

        verifyNoMoreInteractions(securityFilterMapper, repository);
    }

    @Test
    void getSecurityFilter() {
        when(securityFilterResponseMapper.from(any(SecurityFilter.class))).thenReturn(getSecurityFilterResponse());
        when(repository.getReferenceById(any())).thenReturn(getFilter());

        assertNotNull(domainService.getSecurityFilter(ID));

        verify(securityFilterResponseMapper).from(any(SecurityFilter.class));
        verify(repository).getReferenceById(any());
        verifyNoMoreInteractions(securityFilterResponseMapper, repository);
    }

    @Test
    void deleteSecurityFilter() {

        domainService.deleteSecurityFilter(ID);

        verify(repository).deleteById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteRoles() {

        domainService.deleteRoles(ID);

        verify(roleRepository).deleteBySecurityFilterId(any());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void getFolder() {
        when(folderRepository.getAllByParentFolderIsNull()).thenReturn(Collections.singletonList(new SecurityFilterFolder()));
        assertNotNull(domainService.getFolder(null));

        verify(folderRepository).getAllByParentFolderIsNull();

        verifyNoMoreInteractions(folderRepository);


        when(securityFilterFolderResponseMapper.from(any(SecurityFilterFolder.class))).thenReturn(new SecurityFilterFolderResponse());
        when(folderRepository.getReferenceById(any())).thenReturn(new SecurityFilterFolder());
        when(folderRepository.existsById(any())).thenReturn(true);

        assertNotNull(domainService.getFolder(ID));

        verify(folderRepository).existsById(any());
        verify(folderRepository).getReferenceById(any());

        verifyNoMoreInteractions(folderRepository);

        Mockito.reset(folderRepository);

        when(folderRepository.existsById(any())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () -> domainService.getFolder(ID));

        verify(folderRepository).existsById(any());
        verifyNoMoreInteractions(folderRepository);

    }

    @Test
    void addFolder() {
        when(securityFilterFolderMapper.from(any(FolderAddRequest.class))).thenReturn(new SecurityFilterFolder());
        when(folderRepository.save(any())).thenReturn(new SecurityFilterFolder().setId(ID));

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        assertNotNull(domainService.addFolder(new FolderAddRequest()));

        verify(securityFilterFolderMapper).from(any(FolderAddRequest.class));
        verify(folderRepository).save(any());
    }

    @Test
    void renameFolder() {

        when(folderRepository.getReferenceById(any())).thenReturn(new SecurityFilterFolder());
        when(folderRepository.existsById(any())).thenReturn(true);

        assertNotNull(domainService.renameFolder(new FolderRenameRequest().setId(ID)));

        verify(folderRepository).getReferenceById(any());
        verify(folderRepository).existsById(any());
        verify(folderRepository).save(any());
    }

    @Test
    void deleteFolder() {

        when(folderRepository.existsById(any())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(any())).thenReturn(false);
        when(repository.existsByFolderId(any())).thenReturn(false);

        domainService.deleteFolder(ID);

        verify(folderRepository).existsById(any());
        verify(folderRepository).existsByParentFolderId(any());
        verify(folderRepository).deleteById(any());
        verify(repository).existsByFolderId(any());

        verifyNoMoreInteractions(folderRepository, repository);

        Mockito.reset(folderRepository);

        when(folderRepository.existsById(any())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(any())).thenReturn(true);

        assertThrows(InvalidParametersException.class, () -> domainService.deleteFolder(ID));

        verify(folderRepository).existsByParentFolderId(any());
        verify(folderRepository).existsById(any());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void setRoleSettings() {

        when(securityFilterRoleMapper.from(anyList())).thenReturn(Collections.emptyList());

        domainService.setRoleSettings(new SecurityFilterSetRoleRequest());

        verify(securityFilterRoleMapper).from(anyList());
        verifyNoMoreInteractions(securityFilterRoleMapper);
    }

    @Test
    void getFilterRoleSettings() {

        when(repository.getReferenceById(any())).thenReturn(getFilter());
        when(filterDataFIMapper.from(any(FilterInstance.class))).thenReturn(getFilterData());
        when(securityFilterRoleSettingsResponseMapper.from(any(SecurityFilter.class)))
                .thenReturn(getSecurityFilterRoleSettingsResponse());
        when(queryExecutor.getFieldsValues(any())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFilterRoleSettings(ID));

        verify(repository).getReferenceById(any());
        verify(filterDataFIMapper).from(any(FilterInstance.class));
        verify(securityFilterRoleSettingsResponseMapper).from(any(SecurityFilter.class));
        verify(queryExecutor).getFieldsValues(any());

        verifyNoMoreInteractions(repository, filterDataFIMapper, securityFilterRoleSettingsResponseMapper, queryExecutor);
    }

    @Test
    void deleteDataSetMappings() {

        domainService.deleteDataSetMappings(ID);

        verify(dataSetRepository).deleteAllBySecurityFilterId(any());
        verify(dataSetFieldRepository).deleteAllBySecurityFilterId(any());

        verifyNoMoreInteractions(dataSetRepository, dataSetFieldRepository);
    }

    @Test
    void editSecurityFilter() {
        when(repository.getReferenceById(any())).thenReturn(getFilter());
        when(securityFilterMerger.merge(any(), any())).thenReturn(getFilter());

        domainService.editSecurityFilter(getSecurityFilterAddRequest());

        verify(repository).getReferenceById(any());
        verify(securityFilterMerger).merge(any(), any());
        verify(repository).save(any());

        verifyNoMoreInteractions(repository, securityFilterMerger);
    }

    @Test
    void getEffectiveSettings() {

        when(dataSetRepository.findAllByDataSetId(any())).thenReturn(Collections.singletonList(
                new SecurityFilterDataSet()
                        .setSecurityFilter(getFilter())));
        when(reportJobFilterDataSFMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getEffectiveSettings(ID, Collections.emptySet()));

        verify(dataSetRepository).findAllByDataSetId(any());
        verify(reportJobFilterDataSFMapper).from(anyList());

        verifyNoMoreInteractions(dataSetRepository, reportJobFilterDataSFMapper);
    }

    @Test
    void deleteRole() {

        domainService.deleteRole(ID, ID);
        verify(roleRepository).deleteAllBySecurityFilterIdAndRoleId(any(), any());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void changeParentFolder() {
        when(folderRepository.getReferenceById(any())).thenReturn(new SecurityFilterFolder());
        when(securityFilterFolderResponseMapper.from(any(SecurityFilterFolder.class))).thenReturn(new SecurityFilterFolderResponse());

        assertNotNull(domainService.changeParentFolder(new FolderChangeParentRequest().setId(ID).setParentId(ID)));

        verify(folderRepository).getReferenceById(any());
        verify(securityFilterFolderResponseMapper).from(any(SecurityFilterFolder.class));
        verify(folderRepository).save(any());

        verifyNoMoreInteractions(folderRepository, securityFilterFolderResponseMapper);

    }

    @Test
    void getPathSecurityFilter() {
        when(repository.getReferenceById(any())).thenReturn(getFilter());
        when(folderNodeResponseSecurityFilterFolderMapper.from(any(SecurityFilterFolder.class))).thenReturn(new FolderNodeResponse(ID, ID, "", "", LocalDateTime.now(), LocalDateTime.now()));

        assertNotNull(domainService.getPathSecurityFilter(ID));

        verify(repository).getReferenceById(any());
        verify(folderNodeResponseSecurityFilterFolderMapper, times(2)).from(any(SecurityFilterFolder.class));

        verifyNoMoreInteractions(repository, folderNodeResponseSecurityFilterFolderMapper);
    }

    @Test
    void checkSecurityFilterExistsForDataset() {
        when(dataSetRepository.findAllByDataSetId(any())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.checkSecurityFilterExistsForDataset(ID));

        verify(dataSetRepository).findAllByDataSetId(any());
        verifyNoMoreInteractions(dataSetFieldRepository);
    }

    private SecurityFilter getFilter() {
        return new SecurityFilter()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW)
                .setFolder(new SecurityFilterFolder().setParentFolder(new SecurityFilterFolder()))
                .setFilterInstance(new FilterInstance()
                        .setId(ID));

    }

    private SecurityFilterAddRequest getSecurityFilterAddRequest() {
        return new SecurityFilterAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFolderId(ID);
    }

    private SecurityFilterResponse getSecurityFilterResponse() {
        return new SecurityFilterResponse(
                ID,
                null,
                FilterOperationTypeEnum.IS_BETWEEN,
                NAME,
                DESCRIPTION,
                new ArrayList<>(),
                new ArrayList<>(),
                "",
                NOW,
                NOW,
                new ArrayList<>()
        );
    }

    private FilterData getFilterData() {
        return new FilterData(
                null,
                ID,
                FilterTypeEnum.TOKEN_INPUT,
                "schema",
                "tableName",
                NAME,
                "code",
                DESCRIPTION,
                new ArrayList<>()
        );
    }

    private SecurityFilterRoleSettingsResponse getSecurityFilterRoleSettingsResponse() {
        return new SecurityFilterRoleSettingsResponse(
                ID,
                Collections.singletonList(new RoleSettingsResponse(
                        new RoleResponse(),
                        Collections.emptyList()
                ))
        );
    }
}
