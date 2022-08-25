package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import ru.magnit.magreportbackend.dto.request.filterinstance.LikenessType;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.service.FolderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static ru.magnit.magreportbackend.controller.FolderController.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class FolderControllerTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @InjectMocks
    private FolderController controller;

    @Mock
    private FolderService service;

    @BeforeEach
    public void initMock() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("TestUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mvc = standaloneSetup(this.controller).build();
        this.objectMapper = new ObjectMapper();
        this.formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);
    }

    @Test
    void addFolder() throws Exception {
        when(service.addFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FOLDER_ADD)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderAddRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.reports", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).addFolder(any());
        verifyNoMoreInteractions(service);
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

    @Test
    void getFolder() throws Exception {
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FOLDER_GET)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.reports", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
            .setId(ID);
    }

    @Test
    void renameFolder() throws Exception {
        when(service.renameFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FOLDER_RENAME)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.reports", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).renameFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addReport() throws Exception{
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FOLDER_GET)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.reports", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteFolder() throws Exception {

        mvc
                .perform(post(FOLDER_DELETE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Folder with ID:" + ID + " successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).deleteFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void testAddReport() throws Exception {
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
                .perform(post(FOLDER_ADD_REPORT)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
                .andExpect(jsonPath("$.data.reports", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).addReport(any());
        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteReport() throws Exception {
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
                .perform(post(FOLDER_DELETE_REPORT)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
                .andExpect(jsonPath("$.data.reports", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).deleteReport(any());
        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void searchFolder() throws Exception {
        when(service.searchFolder(any())).thenReturn(getFolderSearchResponse());

        mvc
                .perform(post(FOLDER_SEARCH)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFolderSearchRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.folders", hasSize(1)))
                .andExpect(jsonPath("$.data.objects", hasSize(0)));

        verify(service).searchFolder(any());
        verifyNoMoreInteractions(service);
    }

    private FolderSearchRequest getFolderSearchRequest() {
        return new FolderSearchRequest()
                .setRootFolderId(ID)
                .setSearchString("name")
                .setLikenessType(LikenessType.CONTAINS)
                .setRecursive(true);
    }

    private FolderSearchResponse getFolderSearchResponse() {
        return new FolderSearchResponse(Collections.emptyList(),
                Collections.singletonList(new FolderSearchResultResponse(Collections.emptyList(),
                        new FolderNodeResponse(ID, ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME))));
    }
}