package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateAddRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateSetDefaultRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateResponseMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ReportExcelTemplateResponseMapper;
import ru.magnit.magreportbackend.repository.ExcelTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.ExcelTemplateRepository;
import ru.magnit.magreportbackend.repository.ReportExcelTemplateRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateDomainServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String RENAME = "rename";
    private final String DESCRIPTION = "Folder description";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private ExcelTemplateDomainService domainService;

    @Mock
    private ExcelTemplateFolderRepository folderRepository;

    @Mock
    private ExcelTemplateRepository excelTemplateRepository;

    @Mock
    private ExcelTemplateFolderMapper excelTemplateFolderMapper;

    @Mock
    private ExcelTemplateFolderResponseMapper excelTemplateFolderResponseMapper;

    @Mock
    private ExcelTemplateMapper excelTemplateMapper;

    @Mock
    private ExcelTemplateResponseMapper excelTemplateResponseMapper;

    @Mock
    private ReportExcelTemplateRepository reportExcelTemplateRepository;

    @Mock
    private ReportExcelTemplateResponseMapper reportExcelTemplateResponseMapper;

    @Test
    void addFolder() {
        when(excelTemplateFolderMapper.from((FolderAddRequest) any())).thenReturn(new ExcelTemplateFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new ExcelTemplateFolder().setId(ID));
        when(excelTemplateFolderResponseMapper.from((ExcelTemplateFolder) any())).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        ExcelTemplateFolderResponse response = domainService.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(excelTemplateFolderMapper).from((FolderAddRequest) any());
        verify(excelTemplateFolderResponseMapper).from((ExcelTemplateFolder) any());
        verify(folderRepository).saveAndFlush(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderRepository, excelTemplateFolderResponseMapper, excelTemplateFolderMapper);
    }

    @Test
    void getFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new ExcelTemplateFolder());

        when(excelTemplateFolderResponseMapper.from((ExcelTemplateFolder) any())).thenReturn(getFolderResponse());

        ExcelTemplateFolderResponse response = domainService.getFolder(getFolderRequest().getId());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(excelTemplateFolderResponseMapper).from((ExcelTemplateFolder) any());
        verifyNoMoreInteractions(excelTemplateFolderResponseMapper);
        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);


        assertNotNull(domainService.getFolder(null));
    }

    @Test
    void getChildFolders() {
        when(folderRepository.getAllByParentFolderId(ID)).thenReturn(Collections.singletonList(new ExcelTemplateFolder()));
        when(excelTemplateFolderResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getFolderResponse()));

        List<ExcelTemplateFolderResponse> responseList = domainService.getChildFolders(ID);

        assertNotNull(responseList);

        ExcelTemplateFolderResponse response = responseList.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
        verify(excelTemplateFolderResponseMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(excelTemplateFolderResponseMapper);
    }

    @Test
    void renameFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new ExcelTemplateFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new ExcelTemplateFolder());
        when(excelTemplateFolderResponseMapper.from((ExcelTemplateFolder) any())).thenReturn(getRenameFolderResponse());

        ExcelTemplateFolderResponse response = domainService.renameFolder(getRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(RENAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).saveAndFlush(any());
        verifyNoMoreInteractions(folderRepository);
        verify(excelTemplateFolderResponseMapper).from((ExcelTemplateFolder) any());
        verifyNoMoreInteractions(excelTemplateFolderResponseMapper);
    }

    @Test
    void addExcelTemplate() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(excelTemplateMapper.from((ExcelTemplateAddRequest) any())).thenReturn(new ExcelTemplate());
        when(excelTemplateRepository.save(any())).thenReturn(new ExcelTemplate());
        when(excelTemplateResponseMapper.from((ExcelTemplate) any())).thenReturn(getExcelTemplateResponse());

        ExcelTemplateResponse response = domainService.addExcelTemplate(new UserView().setId(1L), getExcelTemplateAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
        verify(excelTemplateMapper).from((ExcelTemplateAddRequest) any());
        verifyNoMoreInteractions(excelTemplateMapper);
        verify(excelTemplateRepository).save(any());
        verifyNoMoreInteractions(excelTemplateRepository);
    }

    @Test
    void checkFolderExists() {

        when(folderRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(domainService, "checkFolderExists", ID));

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkFolderEmpty() {

        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(true);

        Assertions.assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(domainService, "checkFolderEmpty", ID));

        verify(folderRepository).existsByParentFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository, excelTemplateRepository);

        reset(folderRepository, excelTemplateRepository);

        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(excelTemplateRepository.existsByFolderId(anyLong())).thenReturn(true);

        Assertions.assertThrows(InvalidParametersException.class, () -> ReflectionTestUtils.invokeMethod(domainService, "checkFolderEmpty", ID));

        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(excelTemplateRepository).existsByFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository, excelTemplateRepository);

    }

    @Test
    void deleteFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(excelTemplateRepository.existsByFolderId(anyLong())).thenReturn(false);

        domainService.deleteFolder(ID);

        verify(folderRepository).deleteById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).existsByParentFolderId(anyLong());
        verify(excelTemplateRepository).existsByFolderId(anyLong());
        verifyNoMoreInteractions(folderRepository, excelTemplateRepository);
    }

    @Test
    void deleteExcelTemplate() {

        assertThrows(InvalidParametersException.class, () -> domainService.deleteExcelTemplate(1L));

        domainService.deleteExcelTemplate(2L);

        verify(excelTemplateRepository).deleteById(anyLong());
        verify(reportExcelTemplateRepository).setDefaultExcelTemplateInsteadOfRemote();
        verifyNoMoreInteractions(excelTemplateRepository, reportExcelTemplateRepository);
    }

    @Test
    void getTemplatePathForReport() {

        ReflectionTestUtils.setField(domainService, "templatesPath", "");
        when(reportExcelTemplateRepository.getTopByReportIdAndIsDefaultIsTrue(anyLong())).thenReturn(new ReportExcelTemplate().setExcelTemplate(new ExcelTemplate().setId(ID)));

        assertNotNull(domainService.getTemplatePathForReport(ID, null));

        verify(reportExcelTemplateRepository).getTopByReportIdAndIsDefaultIsTrue(anyLong());
        verifyNoMoreInteractions(reportExcelTemplateRepository);
    }

    @Test
    void changeParentFolder() {

        when(folderRepository.getReferenceById(anyLong())).thenReturn(getExcelTemplateFolder());
        when(excelTemplateFolderResponseMapper.from(any(ExcelTemplateFolder.class))).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        domainService.changeParentFolder(new FolderChangeParentRequest().setId(ID));

        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).save(any());
        verify(folderRepository).checkRingPath(anyLong());
        verify(excelTemplateFolderResponseMapper).from(any(ExcelTemplateFolder.class));
        verifyNoMoreInteractions(folderRepository, excelTemplateFolderResponseMapper);
    }

    @Test
    void setDefaultExcelTemplateToReport() {

        when(reportExcelTemplateRepository.findAllByReportId(anyLong())).thenReturn(getListReportExcelTemplate());

        domainService.setDefaultExcelTemplateToReport(new ExcelTemplateSetDefaultRequest().setExcelTemplateId(ID).setReportId(ID));

        verify(reportExcelTemplateRepository).saveAll(any());
        verifyNoMoreInteractions(reportExcelTemplateRepository);

    }

    @Test
    void setDefaultSystemExcelTemplateToReport() {

        domainService.setDefaultSystemExcelTemplateToReport(ID);

        verify(reportExcelTemplateRepository).save(any());
        verifyNoMoreInteractions(reportExcelTemplateRepository);
    }

    @Test
    void removeReportExcelTemplate() {

        domainService.removeReportExcelTemplate(ID);

        verify(reportExcelTemplateRepository).removeReportExcelTemplate(any());
        verifyNoMoreInteractions(reportExcelTemplateRepository);
    }

    @Test
    void getAllReportExcelTemplateToReport() {
        when(reportExcelTemplateRepository.findAllByReportId(anyLong())).thenReturn(Collections.singletonList(getReportExcelTemplate()));
        when(reportExcelTemplateResponseMapper.from(any(ReportExcelTemplate.class))).thenReturn(new ReportExcelTemplateResponse());

        assertNotNull(domainService.getAllReportExcelTemplateToReport(new ReportIdRequest().setId(ID)));

        verify(reportExcelTemplateRepository).findAllByReportId(anyLong());
        verify(reportExcelTemplateResponseMapper).from(any(ReportExcelTemplate.class));
        verifyNoMoreInteractions(reportExcelTemplateRepository, reportExcelTemplateResponseMapper);
    }

    @Test
    void addExcelTemplateToReport() {

        domainService.addExcelTemplateToReport(new ExcelTemplateSetDefaultRequest().setReportId(ID).setExcelTemplateId(ID));

        verify(reportExcelTemplateRepository).save(any());
        verifyNoMoreInteractions(reportExcelTemplateRepository);

    }

    private ExcelTemplateResponse getExcelTemplateResponse() {
        return new ExcelTemplateResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private ExcelTemplateAddRequest getExcelTemplateAddRequest() {
        return new ExcelTemplateAddRequest()
                .setFolderId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private ExcelTemplateFolderResponse getFolderResponse() {
        return new ExcelTemplateFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new ExcelTemplateFolderResponse()))
                .setExcelTemplates(Collections.singletonList(new ExcelTemplateResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private FolderRenameRequest getRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION);
    }

    private ExcelTemplateFolderResponse getRenameFolderResponse() {
        return new ExcelTemplateFolderResponse()
                .setId(ID)
                .setName(RENAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new ExcelTemplateFolderResponse()))
                .setExcelTemplates(Collections.singletonList(new ExcelTemplateResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private ExcelTemplateFolder getExcelTemplateFolder() {
        return new ExcelTemplateFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private ReportExcelTemplate getReportExcelTemplate() {
        return new ReportExcelTemplate();
    }
    private List<ReportExcelTemplate> getListReportExcelTemplate() {
        return Collections.singletonList(new ReportExcelTemplate().setExcelTemplate(new ExcelTemplate().setId(ID)));
    }
}
