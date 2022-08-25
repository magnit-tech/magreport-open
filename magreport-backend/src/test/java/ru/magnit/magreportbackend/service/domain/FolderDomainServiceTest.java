package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.filterinstance.LikenessType;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderAddReportRequest;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.folder.FolderNodeResponseFolderMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderReportMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.repository.FavReportRepository;
import ru.magnit.magreportbackend.repository.FolderReportRepository;
import ru.magnit.magreportbackend.repository.FolderRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FolderDomainServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FolderDomainService domainService;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FolderReportRepository folderReportRepository;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private FavReportRepository favReportRepository;

    @Mock
    private FolderMapper folderMapper;

    @Mock
    private FolderResponseMapper folderResponseMapper;

    @Mock
    private FolderReportMapper folderReportMapper;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderNodeResponseFolderMapper folderNodeResponseFolderMapper;

    @Mock
    private ReportResponseMapper reportResponseMapper;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;


    @Test
    void addFolder() {
        when(folderMapper.from((FolderAddRequest) any())).thenReturn(new Folder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new Folder().setId(ID));
        when(folderResponseMapper.from((Folder) any())).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        FolderResponse response = domainService.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderMapper).from((FolderAddRequest) any());
        verify(folderResponseMapper).from((Folder) any());
        verify(folderRepository).saveAndFlush(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderMapper, folderResponseMapper, folderRepository);
    }

    @Test
    void getFolder() {
        when(favReportRepository.existsByUserIdAndReportId(any(), any())).thenReturn(true);
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new Folder());
        when(folderResponseMapper.from((Folder) any())).thenReturn(getFolderResponse());

        FolderResponse response = domainService.getFolder(0L, ID);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).getReferenceById(anyLong());
        verify(folderResponseMapper).from((Folder) any());
        verifyNoMoreInteractions(folderRepository, folderResponseMapper);

        Mockito.reset(folderRepository, folderResponseMapper);

        when(folderRepository.getAllByParentFolderIsNull()).thenReturn(Collections.emptyList());
        when(folderResponseMapper.shallowMap(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFolder(0L, null));

        verify(folderRepository).getAllByParentFolderIsNull();
        verify(folderResponseMapper).shallowMap(anyList());
        verifyNoMoreInteractions(folderRepository, folderResponseMapper);
    }

    @Test
    void getChildFolders() {
        when(folderRepository.getAllByParentFolderId(ID)).thenReturn(Collections.singletonList(new Folder()));
        when(folderResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getFolderResponse()));

        List<FolderResponse> responseList = domainService.getChildFolders(ID);

        assertNotNull(responseList);

        FolderResponse response = responseList.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
        verify(folderResponseMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(folderResponseMapper);
    }

    @Test
    void renameFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new Folder());
        when(folderRepository.save(any())).thenReturn(new Folder());
        when(folderResponseMapper.from((Folder) any())).thenReturn(getFolderResponse());

        FolderResponse response = domainService.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).save(any());
        verifyNoMoreInteractions(folderRepository);
        verify(folderResponseMapper).from((Folder) any());
        verifyNoMoreInteractions(folderResponseMapper);
    }

    @Test
    void deleteFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(folderReportRepository.existsByFolderId(anyLong())).thenReturn(false);

        domainService.deleteFolder(ID);

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(folderRepository).deleteById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(folderReportRepository).existsByFolderId(any());
        verifyNoMoreInteractions(folderReportRepository);
    }

    @Test
    void addReport() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(reportRepository.existsById(anyLong())).thenReturn(true);
        when(folderReportMapper.from((FolderAddReportRequest) any())).thenReturn(new FolderReport());
        when(folderReportRepository.save(any())).thenReturn(new FolderReport());

        domainService.addReport(new FolderAddReportRequest().setFolderId(ID).setReportId(ID));

        verify(reportRepository).existsById(anyLong());
        verifyNoMoreInteractions(reportRepository);
        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(folderReportRepository).save(any());
        verify(folderReportRepository).existsByFolderIdAndReportId(anyLong(), anyLong());
        verifyNoMoreInteractions(folderReportRepository);
        verify(folderReportMapper).from((FolderAddReportRequest) any());
        verifyNoMoreInteractions(folderResponseMapper);
    }

    @Test
    void deleteReport() {

        domainService.deleteReport(new FolderAddReportRequest().setReportId(ID).setFolderId(ID));

        verify(folderReportRepository).deleteByFolderIdAndReportId(anyLong(), anyLong());
        verifyNoMoreInteractions(folderReportRepository);
    }

    @Test
    void changeParentFolder() {

        when(folderRepository.getReferenceById(anyLong())).thenReturn(get_folder());
        when(folderResponseMapper.from(any(Folder.class))).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        assertNotNull(domainService.changeParentFolder(new FolderChangeParentRequest().setId(ID)));

        verify(folderRepository).getReferenceById(anyLong());
        verify(folderResponseMapper).from(any(Folder.class));
        verify(folderRepository).save(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkReportAlreadyInFolder() {

        when(folderReportRepository.existsByFolderIdAndReportId(anyLong(), anyLong())).thenReturn(true);

        assertThrows(InvalidParametersException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "checkReportAlreadyInFolder", ID, ID));

        verify(folderReportRepository).existsByFolderIdAndReportId(anyLong(), anyLong());
        verifyNoMoreInteractions(folderReportRepository);

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

    private FolderSearchRequest getFolderSearchRequest(Long rootFolderId) {
        return new FolderSearchRequest()
                .setLikenessType(LikenessType.CONTAINS)
                .setRootFolderId(rootFolderId)
                .setRecursive(true)
                .setSearchString("");
    }

    private Folder get_folder() {
        return new Folder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setParentFolder(new Folder())
                .setFolderReports(Collections.singletonList(
                        new FolderReport().setReport(new Report().setName(NAME).setId(ID))))
                .setChildFolders(Collections.singletonList(
                        new Folder()
                                .setParentFolder(new Folder())
                                .setFolderReports(Collections.singletonList(
                                        new FolderReport()
                                                .setId(ID)
                                                .setReport(new Report().setId(ID).setName(NAME))))
                                .setName(NAME)))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}
