package ru.magnit.magreportbackend.service.domain.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRole;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRole;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRole;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRole;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.domain.report.ReportFolderRole;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRole;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSetFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSourceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ExcelTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterInstanceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ReportFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.SecurityFilterFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderReportPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderRoleMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderRoleViewMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderRoleMapper;
import ru.magnit.magreportbackend.repository.DataSetFolderRepository;
import ru.magnit.magreportbackend.repository.DataSetFolderRoleRepository;
import ru.magnit.magreportbackend.repository.DataSourceFolderRepository;
import ru.magnit.magreportbackend.repository.DataSourceFolderRoleRepository;
import ru.magnit.magreportbackend.repository.ExcelTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.ExcelTemplateFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FolderRepository;
import ru.magnit.magreportbackend.repository.FolderRoleRepository;
import ru.magnit.magreportbackend.repository.ReportFolderRepository;
import ru.magnit.magreportbackend.repository.ReportFolderRoleRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRoleRepository;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FolderPermissionsDomainServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FolderPermissionsDomainService domainService;

    @Mock
    private FolderReportPermissionsResponseMapper folderReportPermissionsResponseMapper;

    @Mock
    private ReportFolderPermissionsResponseMapper reportFolderPermissionsResponseMapper;

    @Mock
    private DataSourceFolderRoleMapper dataSourceFolderRoleMapper;

    @Mock
    private DataSetFolderRoleMapper dataSetFolderRoleMapper;

    @Mock
    private ExcelTemplateFolderRoleMapper excelTemplateFolderRoleMapper;

    @Mock
    private FilterInstanceFolderRoleMapper filterInstanceFolderRoleMapper;

    @Mock
    private FilterTemplateFolderRoleMapper filterTemplateFolderRoleMapper;

    @Mock
    private SecurityFilterFolderRoleMapper securityFilterFolderRoleMapper;

    @Mock
    private DataSourceFolderPermissionsResponseMapper dataSourceFolderPermissionsResponseMapper;

    @Mock
    private DataSetFolderPermissionsResponseMapper dataSetFolderPermissionsResponseMapper;

    @Mock
    private ExcelTemplateFolderPermissionsResponseMapper excelTemplateFolderPermissionsResponseMapper;

    @Mock
    private FilterInstanceFolderPermissionsResponseMapper filterInstanceFolderPermissionsResponseMapper;

    @Mock
    private FilterTemplateFolderPermissionsResponseMapper filterTemplateFolderPermissionsResponseMapper;

    @Mock
    private SecurityFilterFolderPermissionsResponseMapper securityFilterFolderPermissionsResponseMapper;

    @Mock
    private FolderRoleViewMapper folderRoleViewMapper;

    @Mock
    private ReportFolderRoleMapper reportFolderRoleMapper;

    @Mock
    private FolderRoleMapper folderRoleMapper;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FolderRoleRepository folderRoleRepository;

    @Mock
    private ReportFolderRepository reportFolderRepository;

    @Mock
    private ReportFolderRoleRepository reportFolderRoleRepository;

    @Mock
    private DataSourceFolderRepository dataSourceFolderRepository;

    @Mock
    private DataSourceFolderRoleRepository dataSourceFolderRoleRepository;

    @Mock
    private DataSetFolderRepository dataSetFolderRepository;

    @Mock
    private DataSetFolderRoleRepository dataSetFolderRoleRepository;

    @Mock
    private ExcelTemplateFolderRepository excelTemplateFolderRepository;

    @Mock
    private ExcelTemplateFolderRoleRepository excelTemplateFolderRoleRepository;

    @Mock
    private FilterInstanceFolderRepository filterInstanceFolderRepository;

    @Mock
    private FilterInstanceFolderRoleRepository filterInstanceFolderRoleRepository;

    @Mock
    private FilterTemplateFolderRepository filterTemplateFolderRepository;

    @Mock
    private FilterTemplateFolderRoleRepository filterTemplateFolderRoleRepository;

    @Mock
    private SecurityFilterFolderRepository securityFilterFolderRepository;

    @Mock
    private SecurityFilterFolderRoleRepository securityFilterFolderRoleRepository;


    @Test
    void getFolderReportPermissions() {
        when(folderRepository.getReferenceById(any())).thenReturn(getFolder());
        when(folderReportPermissionsResponseMapper.from((Folder) any())).thenReturn(new FolderPermissionsResponse(getFolderResponse(), Collections.emptyList()));

        FolderPermissionsResponse response = domainService.getFolderReportPermissions(ID);

        assertNotNull(response.folder());
        assertNotNull(response.rolePermissions());

        verify(folderRepository).getReferenceById(any());
        verifyNoMoreInteractions(folderRepository);
        verify(folderReportPermissionsResponseMapper).from((Folder) any());
        verifyNoMoreInteractions(folderReportPermissionsResponseMapper);
    }

    @Test
    void setFolderReportPermissions() {
        domainService.setFolderReportPermissions(Collections.singletonList(ID), getFolderPermissionsRequest());

        verify(folderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(folderRoleRepository);
    }

    @Test
    void getFoldersReportPermissionsForRoles() {
        when(folderRoleRepository.getAllByFolderIdInAndRoleIdIn(any(), any())).thenReturn(Collections.singletonList(new FolderRole().setFolder(getFolder().setId(ID))));

        List<FolderRoleResponse> responses = domainService.getFoldersReportPermissionsForRoles(Collections.singletonList(ID), Collections.singletonList(ID));

        assertNotNull(responses);

        verify(folderRoleRepository).getAllByFolderIdInAndRoleIdIn(any(), any());
        verifyNoMoreInteractions(folderRoleRepository);
    }

    @Test
    void getReportFolderPermissions() {
        when(reportFolderRepository.getReferenceById(any())).thenReturn(getReportFolder());
        when(reportFolderPermissionsResponseMapper.from((ReportFolder) any())).thenReturn(new ReportFolderPermissionsResponse(getReportFolderResponse(), Collections.emptyList()));

        ReportFolderPermissionsResponse response = domainService.getReportFolderPermissions(ID);

        assertNotNull(response.folder());
        assertNotNull(response.rolePermissions());

        verify(reportFolderRepository).getReferenceById(any());
        verifyNoMoreInteractions(reportFolderRepository);
        verify(reportFolderPermissionsResponseMapper).from((ReportFolder) any());
        verifyNoMoreInteractions(reportFolderPermissionsResponseMapper);
    }

    @Test
    void setReportFolderPermissions() {
        domainService.setReportFolderPermissions(Collections.singletonList(ID), getFolderPermissionsRequest());

        verify(reportFolderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(reportFolderRoleRepository);
    }

    @Test
    void setDataSourceFolderPermissions() {
        when(dataSourceFolderRoleMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSourceFolderRole()));

        domainService.setDataSourceFolderPermissions(getFolderPermissionsRequest());

        verify(dataSourceFolderRoleRepository).deleteByFolderId(any());
        verify(dataSourceFolderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(dataSourceFolderRoleRepository);
        verify(dataSourceFolderRoleMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(dataSourceFolderRoleMapper);
    }

    @Test
    void getDataSourceFolderPermissions() {
        when(dataSourceFolderRepository.getReferenceById(ID)).thenReturn(getDataSourceFolder());
        when(dataSourceFolderPermissionsResponseMapper.from((DataSourceFolder) any())).thenReturn(new DataSourceFolderPermissionsResponse(getDataSourceFolderResponse(), Collections.emptyList()));

        DataSourceFolderPermissionsResponse response = domainService.getDataSourceFolderPermissions(ID);

        assertNotNull(response.folder());
        assertNotNull(response.rolePermissions());

        verify(dataSourceFolderRepository).getReferenceById(any());
        verifyNoMoreInteractions(dataSourceFolderRepository);
        verify(dataSourceFolderPermissionsResponseMapper).from((DataSourceFolder) any());
        verifyNoMoreInteractions(dataSourceFolderPermissionsResponseMapper);
    }

    @Test
    void getDataSetFolderPermissions() {

        when(dataSetFolderRepository.getReferenceById(ID)).thenReturn(getDataSetFolder());
        when(dataSetFolderPermissionsResponseMapper.from((DataSetFolder) any())).thenReturn(new DataSetFolderPermissionsResponse(getDataSetFolderResponse(), Collections.emptyList()));

        DataSetFolderPermissionsResponse response = domainService.getDataSetFolderPermissions(ID);

        assertNotNull(response.folder());
        assertNotNull(response.rolePermissions());

        verify(dataSetFolderRepository).getReferenceById(any());
        verifyNoMoreInteractions(dataSetFolderRepository);
        verify(dataSetFolderPermissionsResponseMapper).from((DataSetFolder) any());
        verifyNoMoreInteractions(dataSetFolderPermissionsResponseMapper);
    }

    @Test
    void setDataSetFolderPermissions() {
        when(dataSetFolderRoleMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSetFolderRole()));

        domainService.setDataSetFolderPermissions(getFolderPermissionsRequest());

        verify(dataSetFolderRoleRepository).deleteByFolderId(any());
        verify(dataSetFolderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(dataSetFolderRoleRepository);
        verify(dataSetFolderRoleMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(dataSetFolderRoleMapper);
    }

    @Test
    void getExcelTemplateFolderPermissions() {
        when(excelTemplateFolderRepository.existsById(any())).thenReturn(true);
        when(excelTemplateFolderRepository.getReferenceById(ID)).thenReturn(getExcelTemplateFolder());
        when(excelTemplateFolderPermissionsResponseMapper.from((ExcelTemplateFolder) any())).thenReturn(new ExcelTemplateFolderPermissionsResponse(getExcelTemplateFolderResponse(), Collections.emptyList()));

        ExcelTemplateFolderPermissionsResponse response = domainService.getExcelTemplateFolderPermissions(ID);

        assertNotNull(response.folder());
        assertNotNull(response.rolePermissions());

        verify(excelTemplateFolderRepository).existsById(any());
        verify(excelTemplateFolderRepository).getReferenceById(any());
        verifyNoMoreInteractions(excelTemplateFolderRepository);
        verify(excelTemplateFolderPermissionsResponseMapper).from((ExcelTemplateFolder) any());
        verifyNoMoreInteractions(excelTemplateFolderPermissionsResponseMapper);
    }

    @Test
    void setExcelTemplateFolderPermissions() {

        when(excelTemplateFolderRoleMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new ExcelTemplateFolderRole()));

        domainService.setExcelTemplateFolderPermissions(getFolderPermissionsRequest());

        verify(excelTemplateFolderRoleRepository).deleteByFolderId(any());
        verify(excelTemplateFolderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(excelTemplateFolderRoleRepository);
        verify(excelTemplateFolderRoleMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(excelTemplateFolderRoleMapper);
    }

    @Test
    void getFilterInstanceFolderPermissions() {
        when(filterInstanceFolderRepository.getReferenceById(any())).thenReturn(getFilterInstanceFolder());
        when(filterInstanceFolderPermissionsResponseMapper.from((FilterInstanceFolder) any())).thenReturn(new FilterInstanceFolderPermissionsResponse(getFilterInstanceFolderResponse(), Collections.emptyList()));

        domainService.getFilterInstanceFolderPermissions(ID);

        verify(filterInstanceFolderRepository).getReferenceById(any());
        verifyNoMoreInteractions(filterInstanceFolderRepository);
        verify(filterInstanceFolderPermissionsResponseMapper).from((FilterInstanceFolder) any());
        verifyNoMoreInteractions(filterInstanceFolderPermissionsResponseMapper);
    }

    @Test
    void setFilterInstanceFolderPermissions() {
        when(filterInstanceFolderRoleMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new FilterInstanceFolderRole()));

        domainService.setFilterInstanceFolderPermissions(getFolderPermissionsRequest());

        verify(filterInstanceFolderRoleRepository).deleteByFolderId(any());
        verify(filterInstanceFolderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(filterInstanceFolderRoleRepository);
        verify(filterInstanceFolderRoleMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(filterInstanceFolderRoleMapper);
    }

    @Test
    void getFilterTemplateFolderPermissions() {
        when(filterTemplateFolderRepository.getReferenceById(any())).thenReturn(getFilterTemplateFolder());
        when(filterTemplateFolderPermissionsResponseMapper.from((FilterTemplateFolder) any())).thenReturn(new FilterTemplateFolderPermissionsResponse(getFilterTemplateFolderResponse(), Collections.emptyList()));

        domainService.getFilterTemplateFolderPermissions(ID);

        verify(filterTemplateFolderRepository).getReferenceById(any());
        verifyNoMoreInteractions(filterTemplateFolderRepository);
        verify(filterTemplateFolderPermissionsResponseMapper).from((FilterTemplateFolder) any());
        verifyNoMoreInteractions(filterTemplateFolderPermissionsResponseMapper);
    }

    @Test
    void setFilterTemplateFolderPermissions() {
        when(filterTemplateFolderRoleMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new FilterTemplateFolderRole()));

        domainService.setFilterTemplateFolderPermissions(getFolderPermissionsRequest());

        verify(filterTemplateFolderRoleRepository).deleteByFolderId(any());
        verify(filterTemplateFolderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(filterTemplateFolderRoleRepository);

        verify(filterTemplateFolderRoleMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(filterTemplateFolderRoleMapper);
    }

    @Test
    void getSecurityFilterFolderPermissions() {
        when(securityFilterFolderRepository.existsById(any())).thenReturn(true);
        when(securityFilterFolderRepository.getReferenceById(ID)).thenReturn(getSecurityFilterFolder());
        when(securityFilterFolderPermissionsResponseMapper.from((SecurityFilterFolder) any())).thenReturn(new SecurityFilterFolderPermissionsResponse(getSecurityFilterFolderResponse(), Collections.emptyList()));

        SecurityFilterFolderPermissionsResponse response = domainService.getSecurityFilterFolderPermissions(ID);

        assertNotNull(response.folder());
        assertNotNull(response.rolePermissions());

        verify(securityFilterFolderRepository).existsById(any());
        verify(securityFilterFolderRepository).getReferenceById(any());
        verifyNoMoreInteractions(securityFilterFolderRepository);
        verify(securityFilterFolderPermissionsResponseMapper).from((SecurityFilterFolder) any());
        verifyNoMoreInteractions(securityFilterFolderPermissionsResponseMapper);
    }

    @Test
    void setSecurityFilterFolderPermissions() {

        when(securityFilterFolderRoleMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new SecurityFilterFolderRole()));

        domainService.setSecurityFilterFolderPermissions(getFolderPermissionsRequest());

        verify(securityFilterFolderRoleRepository).saveAll(any());
        verifyNoMoreInteractions(securityFilterFolderRoleRepository);
        verify(securityFilterFolderRoleMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(securityFilterFolderRoleMapper);
    }

    @Test
    void clearFolderReportPermissions() {
        domainService.clearFolderReportsPermissions(Collections.singletonList(ID));

        verify(folderRoleRepository).deleteAllByFolderIdIn(any());
        verifyNoMoreInteractions(folderRoleRepository);
    }

    @Test
    void getFolderReportBranch() {
        when(folderRepository.getAllByParentFolderId(any())).thenReturn(Collections.emptyList());

        List<Long> folderReportBranch = domainService.getFolderReportBranch(ID);

        assertNotNull(folderReportBranch);

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void getReportFolderBranch() {
        when(reportFolderRepository.getAllByParentFolderId(any())).thenReturn(Collections.emptyList());

        List<Long> reportFolderBranch = domainService.getReportFolderBranch(ID);

        assertNotNull(reportFolderBranch);

        verify(reportFolderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(reportFolderRepository);
    }

    @Test
    void clearReportFolderPermissions() {
        domainService.clearReportFoldersPermissions(Collections.singletonList(ID));

        verify(reportFolderRoleRepository).deleteAllByFolderIdIn(any());
        verifyNoMoreInteractions(reportFolderRoleRepository);
    }

    @Test
    void getReportFolderPermissionsForRoles() {

        when(reportFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(any(), any())).thenReturn(Collections.singletonList(new ReportFolderRole().setFolder(getReportFolder().setId(ID))));

        List<FolderRoleResponse> responses = domainService.getReportFolderPermissionsForRoles(Collections.singletonList(ID), Collections.singletonList(ID));

        assertNotNull(responses);

        verify(reportFolderRoleRepository).getAllByFolderIdInAndRoleIdIn(any(), any());
        verifyNoMoreInteractions(reportFolderRoleRepository);
    }

    @Test
    void getDataSourceFolderPermissionsForRoles() {
        when(dataSourceFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(any(), any())).thenReturn(Collections.singletonList(new DataSourceFolderRole().setFolder(getDataSourceFolder().setId(ID))));

        List<FolderRoleResponse> responses = domainService.getDataSourceFolderPermissionsForRoles(Collections.singletonList(ID), Collections.singletonList(ID));

        assertNotNull(responses);

        verify(dataSourceFolderRoleRepository).getAllByFolderIdInAndRoleIdIn(any(), any());
        verifyNoMoreInteractions(dataSourceFolderRoleRepository);
    }

    @Test
    void getDataSetFolderPermissionsForRoles() {
        when(dataSetFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(any(), any())).thenReturn(Collections.singletonList(new DataSetFolderRole().setFolder(getDataSetFolder().setId(ID))));

        List<FolderRoleResponse> responses = domainService.getDataSetFolderPermissionsForRoles(Collections.singletonList(ID), Collections.singletonList(ID));

        assertNotNull(responses);

        verify(dataSetFolderRoleRepository).getAllByFolderIdInAndRoleIdIn(any(), any());
        verifyNoMoreInteractions(dataSetFolderRoleRepository);
    }

    @Test
    void getFilterTemplateFolderPermissionsForRoles() {
        when(filterTemplateFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(any(), any())).thenReturn(Collections.singletonList(new FilterTemplateFolderRole().setFolder(getFilterTemplateFolder().setId(ID))));

        List<FolderRoleResponse> responses = domainService.getFilterTemplateFolderPermissionsForRoles(Collections.singletonList(ID), Collections.singletonList(ID));

        assertNotNull(responses);

        verify(filterTemplateFolderRoleRepository).getAllByFolderIdInAndRoleIdIn(any(), any());
        verifyNoMoreInteractions(filterTemplateFolderRoleRepository);
    }

    @Test
    void getFilterInstanceFolderPermissionsForRoles() {
        when(filterInstanceFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(any(), any())).thenReturn(Collections.singletonList(new FilterInstanceFolderRole().setFolder(getFilterInstanceFolder().setId(ID))));

        List<FolderRoleResponse> responses = domainService.getFilterInstanceFolderPermissionsForRoles(Collections.singletonList(ID), Collections.singletonList(ID));

        assertNotNull(responses);

        verify(filterInstanceFolderRoleRepository).getAllByFolderIdInAndRoleIdIn(any(), any());
        verifyNoMoreInteractions(filterInstanceFolderRoleRepository);
    }

    private Folder getFolder() {
        return new Folder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new Folder()))
                .setFolderRoles(Collections.singletonList(new FolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private ReportFolder getReportFolder() {
        return new ReportFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new ReportFolder()))
                .setFolderRoles(Collections.singletonList(new ReportFolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private DataSourceFolder getDataSourceFolder() {
        return new DataSourceFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSourceFolder()))
                .setFolderRoles(Collections.singletonList(new DataSourceFolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private DataSetFolder getDataSetFolder() {
        return new DataSetFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSetFolder()))
                .setFolderRoles(Collections.singletonList(new DataSetFolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private ExcelTemplateFolder getExcelTemplateFolder() {
        return new ExcelTemplateFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new ExcelTemplateFolder()))
                .setFolderRoles(Collections.singletonList(new ExcelTemplateFolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private FilterInstanceFolder getFilterInstanceFolder() {
        return new FilterInstanceFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new FilterInstanceFolder()))
                .setFolderRoles(Collections.singletonList(new FilterInstanceFolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private FilterTemplateFolder getFilterTemplateFolder() {
        return new FilterTemplateFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new FilterTemplateFolder()))
                .setFolderRoles(Collections.singletonList(new FilterTemplateFolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private SecurityFilterFolder getSecurityFilterFolder() {
        return new SecurityFilterFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new SecurityFilterFolder()))
                .setFolderRoles(Collections.singletonList(new SecurityFilterFolderRole()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private FolderResponse getFolderResponse() {
        return new FolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new FolderResponse()))
                .setReports(Collections.singletonList(new ReportResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderPermissionSetRequest getFolderPermissionsRequest() {
        return new FolderPermissionSetRequest(ID, Collections.emptyList());
    }

    private ReportFolderResponse getReportFolderResponse() {
        return new ReportFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new ReportFolderResponse())))
                .setReports(Collections.singletonList(new ReportResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private DataSourceFolderResponse getDataSourceFolderResponse() {
        return new DataSourceFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new DataSourceFolderResponse())))
                .setDataSources(Collections.singletonList(new DataSourceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private DataSetFolderResponse getDataSetFolderResponse() {
        return new DataSetFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new DataSetFolderResponse())))
                .setDataSets(Collections.singletonList(new DataSetResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private ExcelTemplateFolderResponse getExcelTemplateFolderResponse() {
        return new ExcelTemplateFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new ExcelTemplateFolderResponse())))
                .setExcelTemplates(Collections.singletonList(new ExcelTemplateResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FilterInstanceFolderResponse getFilterInstanceFolderResponse() {
        return new FilterInstanceFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new FilterInstanceFolderResponse())))
                .setFilterInstances(Collections.singletonList(new FilterInstanceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FilterTemplateFolderResponse getFilterTemplateFolderResponse() {
        return new FilterTemplateFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new FilterTemplateFolderResponse())))
                .setFilterTemplates(Collections.singletonList(new FilterTemplateResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private SecurityFilterFolderResponse getSecurityFilterFolderResponse() {
        return new SecurityFilterFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new SecurityFilterFolderResponse())))
                .setSecurityFilters(Collections.emptyList())
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }
}
