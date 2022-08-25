package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.favorite.FavReport;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.report.ReportFolder;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddFavoritesRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFieldResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.folder.FolderNodeResponseFolderMapper;
import ru.magnit.magreportbackend.mapper.report.FolderNodeResponseReportFolderMapper;
import ru.magnit.magreportbackend.mapper.report.PivotFieldTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFieldMapperDataSet;
import ru.magnit.magreportbackend.mapper.report.ReportFolderMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportMapper;
import ru.magnit.magreportbackend.mapper.report.ReportMerger;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportShortResponseMapper;
import ru.magnit.magreportbackend.repository.FavReportRepository;
import ru.magnit.magreportbackend.repository.PivotFieldTypeRepository;
import ru.magnit.magreportbackend.repository.ReportFieldRepository;
import ru.magnit.magreportbackend.repository.ReportFolderRepository;
import ru.magnit.magreportbackend.repository.ReportRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportDomainServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private ReportDomainService domainService;

    @Mock
    private ReportFolderRepository folderRepository;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private FavReportRepository favReportRepository;

    @Mock
    private PivotFieldTypeRepository pivotFieldTypeRepository;

    @Mock
    private ReportFolderResponseMapper reportFolderResponseMapper;

    @Mock
    private ReportFolderMapper reportFolderMapper;

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private ReportResponseMapper reportResponseMapper;

    @Mock
    private ReportFieldMapperDataSet reportFieldMapper;

    @Mock
    private DataSetDomainService dataSetDomainService;

    @Mock
    private ReportMerger reportMerger;

    @Mock
    private PivotFieldTypeResponseMapper pivotFieldTypeResponseMapper;

    @Mock
    private ReportFieldRepository reportFieldRepository;

    @Mock
    private FolderNodeResponseFolderMapper folderNodeResponseFolderMapper;

    @Mock
    private FolderNodeResponseReportFolderMapper folderNodeResponseReportFolderMapper;

    @Mock
    private ReportShortResponseMapper reportShortResponseMapper;

    @Test
    void getFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new ReportFolder());
        when(favReportRepository.existsByUserIdAndReportId(any(), any())).thenReturn(false);
        when(reportFolderResponseMapper.from((ReportFolder) any())).thenReturn(getFolderResponse());

        ReportFolderResponse response = domainService.getFolder(0L, ID);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).getReferenceById(anyLong());
        verify(reportFolderResponseMapper).from((ReportFolder) any());
        verifyNoMoreInteractions(favReportRepository, folderRepository, reportFolderResponseMapper);

        Mockito.reset(favReportRepository, folderRepository, reportFolderResponseMapper);

        when(folderRepository.getAllByParentFolderIsNull()).thenReturn(Collections.emptyList());
        when(reportFolderResponseMapper.shallowMap(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFolder(ID, null));

        verify(folderRepository).getAllByParentFolderIsNull();
        verify(reportFolderResponseMapper).shallowMap(anyList());
        verifyNoMoreInteractions(favReportRepository, folderRepository, reportFolderResponseMapper);


    }

    @Test
    void addFolder() {
        when(reportFolderMapper.from((FolderAddRequest) any())).thenReturn(new ReportFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new ReportFolder().setId(ID));
        when(reportFolderResponseMapper.from((ReportFolder) any())).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        ReportFolderResponse response = domainService.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(reportFolderMapper).from((FolderAddRequest) any());
        verify(reportFolderResponseMapper).from((ReportFolder) any());
        verify(folderRepository).saveAndFlush(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(reportFolderMapper, folderRepository, reportFolderResponseMapper);
    }

    @Test
    void getChildFolders() {
        when(folderRepository.getAllByParentFolderId(ID)).thenReturn(Collections.singletonList(new ReportFolder()));
        when(reportFolderResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getFolderResponse()));

        List<ReportFolderResponse> responseList = domainService.getChildFolders(ID);

        assertNotNull(responseList);

        ReportFolderResponse response = responseList.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
        verify(reportFolderResponseMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(reportFolderResponseMapper);
    }

    @Test
    void renameFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new ReportFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new ReportFolder());
        when(reportFolderResponseMapper.from((ReportFolder) any())).thenReturn(getFolderResponse());

        ReportFolderResponse response = domainService.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).saveAndFlush(any());
        verifyNoMoreInteractions(folderRepository);
        verify(reportFolderResponseMapper).from((ReportFolder) any());
        verifyNoMoreInteractions(reportFolderResponseMapper);
    }

    @Test
    void deleteFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(reportRepository.existsByFolderId(anyLong())).thenReturn(false);

        domainService.deleteFolder(ID);

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(folderRepository).deleteById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(reportRepository).existsByFolderId(any());
        verifyNoMoreInteractions(reportRepository);
    }

    @Test
    void deleteReport() {
        when(reportRepository.existsById(anyLong())).thenReturn(true);
        when(reportRepository.getReferenceById(anyLong())).thenReturn(get_Report());

        domainService.deleteReport(ID);

        verify(reportRepository).existsById(anyLong());
        verify(reportRepository).deleteById(anyLong());
        verify(reportRepository).getReferenceById(any());
        verifyNoMoreInteractions(reportRepository);
    }

    @Test
    void addReport() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(reportMapper.from((ReportAddRequest) any())).thenReturn(new Report());
        when(reportRepository.save(any())).thenReturn(new Report());
        when(dataSetDomainService.getDataSet(anyLong())).thenReturn(new DataSetResponse());

        domainService.addReport(new UserView().setId(1L), getReportAddRequest());

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(reportRepository).save(any());
        verifyNoMoreInteractions(reportRepository);
        verify(reportMapper).from((ReportAddRequest) any());
        verifyNoMoreInteractions(reportMapper);
    }

    @Test
    void deleteReportToFavorites() {

        domainService.deleteReportToFavorites(new UserView().setId(ID), new ReportIdRequest().setId(ID));

        verify(favReportRepository).deleteByUserIdAndReportId(anyLong(), anyLong());
        verifyNoMoreInteractions(favReportRepository, reportRepository);
    }

    @Test
    void addReportToFavorites() {

        domainService.addReportToFavorites(
                new ReportAddFavoritesRequest()
                        .setReportId(ID)
                        .setFolderId(ID),
                new UserView()
                        .setId(ID));

        verify(favReportRepository).save(any());
        verifyNoMoreInteractions(favReportRepository, reportRepository);
    }

    @Test
    void getReport() {

        when(reportRepository.getReferenceById(anyLong())).thenReturn(get_Report());
        when(reportResponseMapper.from(any(Report.class))).thenReturn(new ReportResponse());

        assertNotNull(domainService.getReport(ID));

        verify(reportRepository).getReferenceById(anyLong());
        verify(reportResponseMapper).from(any(Report.class));
        verifyNoMoreInteractions(reportRepository, reportResponseMapper);
    }

    @Test
    void editReport() {

        when(reportMerger.merge(any(), any())).thenReturn(get_Report());
        when(reportRepository.getReferenceById(anyLong())).thenReturn(get_Report());

        domainService.editReport(new ReportEditRequest().setId(ID));

        verify(reportMerger).merge(any(), any());
        verify(reportRepository).getReferenceById(anyLong());
        verify(reportRepository).save(any());
        verifyNoMoreInteractions(reportMerger, reportRepository);
    }

    @Test
    void getPivotFieldTypes() {

        when(pivotFieldTypeResponseMapper.from(anyList())).thenReturn(Collections.emptyList());
        when(pivotFieldTypeRepository.findAll()).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getPivotFieldTypes());

        verify(pivotFieldTypeResponseMapper).from(anyList());
        verify(pivotFieldTypeRepository).findAll();
        verifyNoMoreInteractions(pivotFieldTypeRepository, pivotFieldTypeResponseMapper);
    }

    @Test
    void deleteFields() {

        domainService.deleteFields(ID);

        verify(reportFieldRepository).deleteAllByReportId(anyLong());
        verifyNoMoreInteractions(reportRepository, reportFieldRepository);
    }

    @Test
    void getDeletedFields() {

        when(reportRepository.getReferenceById(anyLong())).thenReturn(get_Report());

        assertNotNull(domainService.getDeletedFields(
                new ReportEditRequest()
                        .setId(ID)
                        .setFields(Collections.emptyList())));

        verify(reportRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(reportRepository);
    }

    @Test
    void deleteFields1() {

        domainService.deleteFields(Collections.singletonList(ID));

        verify(reportFieldRepository).deleteAllByIdIn(anyList());
        verifyNoMoreInteractions(reportRepository, reportFieldRepository);
    }

    @Test
    void addFields() {
        when(reportFieldRepository.getFirstByReportIdOrderByOrdinalDesc(anyLong())).thenReturn(new ReportField().setOrdinal(1));
        when(reportFieldMapper.from(anyList())).thenReturn(Collections.singletonList(new ReportField()));

        domainService.addFields(ID, Collections.singletonList(new DataSetFieldResponse()));

        verify(reportFieldRepository).getFirstByReportIdOrderByOrdinalDesc(anyLong());
        verify(reportFieldRepository).saveAll(anyList());
        verify(reportFieldMapper).from(anyList());

        verifyNoMoreInteractions(reportRepository, reportFieldRepository, reportFieldMapper);
    }

    @Test
    void changeParentFolder() {

        when(folderRepository.getReferenceById(anyLong())).thenReturn(new ReportFolder());
        when(reportFolderResponseMapper.from(any(ReportFolder.class))).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        assertNotNull(domainService.changeParentFolder(new FolderChangeParentRequest().setId(ID)));

        verify(folderRepository).getReferenceById(anyLong());
        verify(reportFolderResponseMapper).from(any(ReportFolder.class));
        verify(folderRepository).save(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderRepository, reportFolderResponseMapper);
    }

    @Test
    void getFavReports() {

        when(favReportRepository.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(
                new FavReport()
                        .setFolder(new Folder().setParentFolder(new Folder()))
                        .setReport(get_Report())));
        when(reportResponseMapper.from(any(Report.class))).thenReturn(getReportResponse());
        when(folderNodeResponseFolderMapper.from(any(Folder.class))).thenReturn(getFolderNodeResponse());

        assertNotNull(domainService.getFavReports(new UserView().setId(ID)));

        verify(favReportRepository).findAllByUserId(anyLong());
        verify(reportResponseMapper).from(any(Report.class));
        verify(folderNodeResponseFolderMapper, times(2)).from(any(Folder.class));
        verifyNoMoreInteractions(favReportRepository, reportResponseMapper);

    }

    @Test
    void getPathReport() {
        when(reportRepository.getReferenceById(anyLong())).thenReturn(get_Report());

        assertNotNull(domainService.getPathReport(ID));

        verify(reportRepository).getReferenceById(anyLong());
        verify(folderNodeResponseReportFolderMapper, times(2)).from(any(ReportFolder.class));
        verifyNoMoreInteractions(reportRepository, reportResponseMapper);
    }

    @Test
    void checkReportExistsForDataset() {
        when(reportShortResponseMapper.from(any(Report.class))).thenReturn(new ReportShortResponse(ID, NAME));
        when(reportRepository.findByDataSetId(ID)).thenReturn(Collections.singletonList(get_Report()));

        assertNotNull(domainService.checkReportExistsForDataset(ID));

        verify(reportShortResponseMapper).from(any(Report.class));
        verify(reportRepository).findByDataSetId(anyLong());
        verifyNoMoreInteractions(reportRepository, reportShortResponseMapper);
    }

    @Test
    void checkFolderExists() {

        when(folderRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "checkFolderExists", ID));

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkFolderEmpty() {

        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(true);

        assertThrows(InvalidParametersException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "checkFolderEmpty", ID));

        verify(folderRepository).existsByParentFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkReportExists() {

        when(reportRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "checkReportExists", ID));

        verify(reportRepository).existsById(anyLong());
        verifyNoMoreInteractions(reportRepository);

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
                .setDataSetId(ID)
                .setFolderId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private ReportFolderResponse getFolderResponse() {
        return new ReportFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new ReportFolderResponse()))
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

    private Report get_Report() {
        return new Report()
                .setFields(Collections.singletonList(new ReportField().setId(ID)))
                .setFolder(new ReportFolder().setParentFolder(new ReportFolder()))
                .setReportJobs(Collections.emptyList());
    }

    private FolderNodeResponse getFolderNodeResponse() {
        return new FolderNodeResponse(
                ID, null, "", "", CREATED_TIME, MODIFIED_TIME
        );
    }

}
