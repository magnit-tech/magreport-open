package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderAddReportRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.report.PublishedReportResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.domain.FolderDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);
    private final String LINK = "LINK";

    @InjectMocks
    private FolderService service;

    @Mock
    private FolderDomainService domainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private ReportDomainService reportDomainService;

    @Test
    void addFolder() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser").setId(0L));
        when(domainService.addFolder(any())).thenReturn(getFolderResponse());
        when(folderPermissionsDomainService.getFolderReportPermissions(any())).thenReturn(new FolderPermissionsResponse(new FolderResponse().setId(ID), Collections.singletonList(new RolePermissionResponse(new RoleResponse(), Collections.singletonList(FolderAuthorityEnum.WRITE)))));
        when(domainService.getFolder(any(), any())).thenReturn(getFolderResponse());

        FolderResponse response = service.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).addFolder(any());
        verifyNoMoreInteractions(domainService);

        response = service.addFolder(getFolderAddRequest().setParentId(null));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService, times(2)).addFolder(any());
        verifyNoMoreInteractions(domainService);


    }

    @Test
    void getFolder() {

        when(domainService.getFolder(any(), any())).thenReturn(getFolderResponse().setParentId(ID).setAuthority(FolderAuthorityEnum.NONE));
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser").setId(0L));
        when(folderPermissionsDomainService.getFoldersReportPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.emptyList());

        FolderResponse response = service.getFolder(new FolderRequest().setId(ID));

        assertNull(response);

        verify(domainService).getFolder(any(), any());
        verifyNoMoreInteractions(domainService);

        Mockito.reset(domainService);

        when(domainService.getFolder(any(), any())).thenReturn(getFolderResponse());

        response = service.getFolder(new FolderRequest().setId(ID));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).getFolder(any(), any());
        verifyNoMoreInteractions(domainService);

        Mockito.reset(userDomainService);

        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.singletonList(new RoleView().setId(0L)));
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser").setId(0L));

        response = service.getFolder(new FolderRequest().setId(ID));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService, times(2)).getFolder(any(), any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getChildFolders() {
        when(domainService.getChildFolders(any())).thenReturn(Collections.singletonList(getFolderResponse()));

        List<FolderResponse> responses = service.getChildFolders(new FolderRequest().setId(ID));
        assertNotNull(responses);

        FolderResponse response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).getChildFolders(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void renameFolder() {
        when(domainService.renameFolder(any())).thenReturn(getFolderResponse());

        FolderResponse response = service.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getReports());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).renameFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    @Test
    void deleteFolder() {
        service.deleteFolder(new FolderRequest().setId(ID));

        verify(domainService).deleteFolder(anyLong());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addReport() {
        service.addReport(new FolderAddReportRequest().setReportId(ID).setFolderId(ID));

        verify(domainService).addReport(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void deleteReport() {

        service.deleteReport(getFolderAddReportRequest());

        verify(domainService).deleteReport(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void searchFolder() {

        service.searchFolder(new FolderSearchRequest());

        verify(domainService).searchFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void changeParentFolder() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));

        service.changeParentFolder(new FolderChangeParentRequest());

        verify(domainService).changeParentFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getPublishedReports() {

        when(domainService.getPublishedReports(any())).thenReturn(null);
        when(reportDomainService.getPublishedReportResponse(anyLong())).thenReturn(getPublishedReportResponse());

        var response = service.getPublishedReports(getReportIdRequest());

        assertEquals(ID, response.getId());
        assertEquals(ID, response.getDataSetId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertEquals(LINK, response.getRequirementsLink());
        assertEquals(1, response.getFolders().size());
        assertEquals(0, response.getPath().size());
        assertNotNull(response.getUserPublisher());

        verify(domainService).getPublishedReports(any());
        verify(reportDomainService).getPublishedReportResponse(anyLong());
        verify(reportDomainService).getPathReport(anyLong());
        verifyNoMoreInteractions(domainService, reportDomainService);

    }

    private FolderResponse getFolderResponse() {
        return new FolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(Collections.singletonList(new FolderResponse())))
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

    private FolderAddReportRequest getFolderAddReportRequest() {
        return new FolderAddReportRequest()
                .setFolderId(ID)
                .setReportId(ID);
    }

    private ReportIdRequest getReportIdRequest() {
        return new ReportIdRequest().setId(ID);
    }

    private PublishedReportResponse getPublishedReportResponse() {
        return new PublishedReportResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setDataSetId(ID)
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME)
                .setRequirementsLink(LINK)
                .setFolders(Collections.singletonList(new FolderSearchResultResponse(null, null)))
                .setPath(Collections.singletonList(new FolderNodeResponse(null, null, null, null,null,null)))
                .setUserPublisher(new UserResponse());
    }
}