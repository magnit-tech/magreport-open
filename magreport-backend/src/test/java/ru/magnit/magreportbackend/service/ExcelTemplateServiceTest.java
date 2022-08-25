package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateAddRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateDeleteRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateGetRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateSetDefaultRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.exception.FileSystemException;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateAddRequestMapper;
import ru.magnit.magreportbackend.service.domain.ExcelTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@ExtendWith(MockitoExtension.class)
class ExcelTemplateServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private ExcelTemplateService service;

    @Mock
    private ExcelTemplateDomainService domainService;

    @Mock
    private FileService fileService;

    @Mock
    private ExcelTemplateAddRequestMapper excelTemplateAddRequestMapper;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private PermissionCheckerSystem permissionCheckerSystem;

    @Test
    void addFolder() {
        when(domainService.addFolder(any())).thenReturn(getFolderResponse());

        ExcelTemplateFolderResponse response = service.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).addFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getFolder() {
        when(domainService.getFolder(any())).thenReturn(getFolderResponse());
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));
        when(folderPermissionsDomainService.getFoldersReportPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.emptyList());

        ExcelTemplateFolderResponse response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).getFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getChildFolders() {
        when(domainService.getChildFolders(any())).thenReturn(Collections.singletonList(getFolderResponse()));

        List<ExcelTemplateFolderResponse> responses = service.getChildFolders(getFolderRequest());
        assertNotNull(responses);

        ExcelTemplateFolderResponse response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).getChildFolders(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void renameFolder() {
        when(domainService.renameFolder(any())).thenReturn(getFolderResponse());

        ExcelTemplateFolderResponse response = service.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getExcelTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).renameFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addExcelTemplate() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));
        when(excelTemplateAddRequestMapper.from(anyString())).thenReturn(new ExcelTemplateAddRequest());
        when(domainService.addExcelTemplate(any(), any())).thenReturn(getExcelTemplateResponse());

        MockMultipartFile file = new MockMultipartFile("file", "testPath", MULTIPART_FORM_DATA_VALUE, "getByte".getBytes());

        ExcelTemplateResponse response = service.addExcelTemplate(" ", file);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).addExcelTemplate(any(), any());
        verifyNoMoreInteractions(domainService);

        doThrow(new FileSystemException("")).when(fileService).storeFile(any(),any());

        assertThrows(FileSystemException.class, () -> service.addExcelTemplate(" ", null));
        verify(domainService).deleteExcelTemplate(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getExcelTemplateFile() {
        byte[] content = "File content".getBytes();
        when(fileService.retrieveFile(any())).thenReturn(content);

        byte[] actual = service.getExcelTemplateFile(new ExcelTemplateGetRequest().setId(ID));

        assertArrayEquals(content, actual);
        verify(fileService).retrieveFile(ID);
    }


    @Test
    void deleteFolder() {

        service.deleteFolder(getFolderRequest());

        verify(domainService).deleteFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void changeParentFolder() {

        when(domainService.changeParentFolder(any())).thenReturn(getFolderResponse());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));
        var response = service.changeParentFolder(new FolderChangeParentRequest());
        assertNotNull(response);

        verify(domainService).changeParentFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void deleteExcelTemplate() {

        service.deleteExcelTemplate(new ExcelTemplateDeleteRequest().setId(ID));

        verify(domainService).deleteExcelTemplate(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void setDefaultExcelTemplateToReport() {

        service.setDefaultExcelTemplateToReport(new ExcelTemplateSetDefaultRequest());

        verify(domainService).setDefaultExcelTemplateToReport(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getAllReportExcelTemplateToReport() {

        when(domainService.getAllReportExcelTemplateToReport(any())).thenReturn(Collections.emptyList());

        var response = service.getAllReportExcelTemplateToReport(new ReportIdRequest());
        assertNotNull(response);

        verify(domainService).getAllReportExcelTemplateToReport(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addExcelTemplateToReport() {

        service.addExcelTemplateToReport(new ExcelTemplateSetDefaultRequest());

        verify(domainService).addExcelTemplateToReport(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void changeExcelTemplateParentFolder(){

        service.changeExcelTemplateParentFolder(getChangeParentFolderRequest());

        verify(permissionCheckerSystem).checkPermissionsOnAllFolders(any(),any(),any());
        verify(domainService).changeExcelTemplateParentFolder(any());
        verifyNoMoreInteractions(permissionCheckerSystem,domainService);

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

    private ExcelTemplateResponse getExcelTemplateResponse() {
        return new ExcelTemplateResponse()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreated(CREATED_TIME)
            .setModified(MODIFIED_TIME);
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private ChangeParentFolderRequest getChangeParentFolderRequest() {
        return new ChangeParentFolderRequest()
                .setDestFolderId(ID)
                .setObjIds(Collections.emptyList());
    }
}