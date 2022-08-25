package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.filtertemplate.FilterTemplateRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateResponseMapper;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRepository;
import ru.magnit.magreportbackend.service.domain.FilterTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
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
class FilterTemplateServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterTemplateService service;

    @Mock
    private FilterTemplateDomainService domainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @Mock
    private FolderEntitySearchDomainService folderEntitySearchDomainService;

    @Mock
    private FilterTemplateFolderRepository repository;

    @Mock
    private FilterTemplateResponseMapper mapper;

    @Test
    void getFolder() {

        when(domainService.getFolder(any())).thenReturn(getFolderResponse().setParentId(ID).setAuthority(FolderAuthorityEnum.NONE));
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.emptyList());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));
        when(folderPermissionsDomainService.getFilterTemplateFolderPermissionsForRoles(anyList(), anyList())).thenReturn(Collections.emptyList());

        var response = service.getFolder(getFolderRequest());

        assertNull(response);

        Mockito.reset(domainService);

        when(domainService.getFolder(any())).thenReturn(getFolderResponse());

        response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        Mockito.reset(userDomainService);
        when(userDomainService.getUserRoles(anyString(), any())).thenReturn(Collections.singletonList(new RoleView().setId(0L)));
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName("TestUser"));

        response = service.getFolder(getFolderRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService, times(2)).getFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void addFolder() {
        when(domainService.addFolder(any())).thenReturn(getFolderResponse());
        when(folderPermissionsDomainService.getFilterTemplateFolderPermissions(any())).thenReturn(new FilterTemplateFolderPermissionsResponse(new FilterTemplateFolderResponse().setId(ID), Collections.singletonList(new RolePermissionResponse(new RoleResponse(), Collections.singletonList(FolderAuthorityEnum.WRITE)))));
        when(domainService.getFolder(any())).thenReturn(getFolderResponse());

        FilterTemplateFolderResponse response = service.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        response = service.addFolder(getFolderAddRequest().setParentId(null));

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService, times(2)).addFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getChildFolders() {
        when(domainService.getChildFolders(any())).thenReturn(Collections.singletonList(getFolderResponse()));

        List<FilterTemplateFolderResponse> responses = service.getChildFolders(getFolderRequest());
        assertNotNull(responses);

        FilterTemplateFolderResponse response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).getChildFolders(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void renameFolder() {
        when(domainService.renameFolder(any())).thenReturn(getFolderResponse());

        FilterTemplateFolderResponse response = service.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(domainService).renameFolder(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void deleteFolder() {
        service.deleteFolder(new FolderRequest().setId(ID));

        verify(domainService).deleteFolder(anyLong());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getFilterOperationTypes() {

        when(domainService.getFilterOperationTypes()).thenReturn(Collections.emptyList());

        var response = service.getFilterOperationTypes();
        assertNotNull(response);
        verify(domainService).getFilterOperationTypes();
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getFilterTypes() {
        when(domainService.getFilterTypes()).thenReturn(Collections.emptyList());

        var response = service.getFilterTypes();
        assertNotNull(response);
        verify(domainService).getFilterTypes();
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getFilterFieldTypes() {
        when(domainService.getFilterFieldTypes()).thenReturn(Collections.emptyList());

        var response = service.getFilterFieldTypes();
        assertNotNull(response);
        verify(domainService).getFilterFieldTypes();
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getFilterTemplate() {

        when(domainService.getFilterTemplate(any())).thenReturn(new FilterTemplateResponse());

        var response = service.getFilterTemplate(new FilterTemplateRequest().setId(ID));
        assertNotNull(response);
        verify(domainService).getFilterTemplate(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void searchFilterInstance() {

        when(folderEntitySearchDomainService.search(any(), any(), any() , any())).thenReturn(new FolderSearchResponse(Collections.emptyList(), Collections.emptyList()));

        var response = service.searchFilterTemplate(new FolderSearchRequest());
        assertNotNull(response);

        verify(folderEntitySearchDomainService).search(any(), any(), any(), any());
        verifyNoMoreInteractions(folderEntitySearchDomainService);

    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private FilterTemplateFolderResponse getFolderResponse() {
        return new FilterTemplateFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(new LinkedList<>(List.of(new FilterTemplateFolderResponse())))
                .setFilterTemplates(Collections.singletonList(new FilterTemplateResponse()))
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

}