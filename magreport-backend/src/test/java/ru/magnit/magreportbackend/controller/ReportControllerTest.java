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
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportFieldEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.report.PivotFieldTypeResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFieldResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.service.ReportService;

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
import static ru.magnit.magreportbackend.controller.ReportController.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    private final Long ID = 1L;
    private final Long JOB_ID = 2L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @InjectMocks
    private ReportController controller;

    @Mock
    private ReportService service;

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
    void getReportFolder() throws Exception {
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(REPORT_GET_FOLDER)
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

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
            .setId(ID);
    }

    @Test
    void addReportFolder() throws Exception {
        when(service.addFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(REPORT_ADD_FOLDER)
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

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
            .setParentId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }

    @Test
    void renameReportFolder() throws Exception {
        when(service.renameFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(REPORT_RENAME_FOLDER)
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
    void addReport() throws Exception {
        when(service.addReport(any())).thenReturn(getReportResponse());

        mvc
            .perform(post(REPORT_ADD)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getReportAddRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.fields", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).addReport(any());
        verifyNoMoreInteractions(service);
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

    @Test
    void deleteReportFolder() throws Exception {
        mvc
                .perform(post(REPORT_DELETE_FOLDER)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Report folder with ID:" + ID + " successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).deleteFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteReport() throws Exception {
        mvc
                .perform(post(REPORT_DELETE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getReportRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Report with id:" + ID + " successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).deleteReport(any());
        verifyNoMoreInteractions(service);
    }

    private ReportRequest getReportRequest() {
        return new ReportRequest()
                .setId(ID)
                .setJobId(JOB_ID);
    }

    @Test
    void getReport() throws Exception {
        when(service.getReport(any())).thenReturn(getReportResponse());

        mvc
                .perform(post(REPORT_GET)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getReportRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.fields", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getReport(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void editReport() throws Exception {
        when(service.editReport(any())).thenReturn(getReportResponse());

        mvc
                .perform(post(REPORT_EDIT)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getReportEditRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.fields", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).editReport(any());
        verifyNoMoreInteractions(service);
    }

    private ReportEditRequest getReportEditRequest() {
        return new ReportEditRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFields(Collections.singletonList(new ReportFieldEditRequest()));
    }

    @Test
    void getPivotFieldTypes() throws Exception {
        when(service.getPivotFieldTypes()).thenReturn(Collections.singletonList(getPivotFieldTypeResponse()));

        mvc
                .perform(post(REPORT_GET_PIVOT_TYPES)
                        .accept(APPLICATION_JSON_VALUE)
                        .content("")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(ID.intValue())))
                .andExpect(jsonPath("$.data[0].name", is(NAME)))
                .andExpect(jsonPath("$.data[0].description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data[0].created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data[0].modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getPivotFieldTypes();
        verifyNoMoreInteractions(service);
    }

    private PivotFieldTypeResponse getPivotFieldTypeResponse() {
        return new PivotFieldTypeResponse(
                ID,
                NAME,
                DESCRIPTION,
                CREATED_TIME,
                MODIFIED_TIME
        );
    }

    @Test
    void searchFolder() throws Exception {
        when(service.searchFolder(any())).thenReturn(getFolderSearchResponse());

        mvc
                .perform(post(REPORT_SEARCH)
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
        return new FolderSearchResponse(
                Collections.emptyList(),
                Collections.singletonList(new FolderSearchResultResponse(Collections.emptyList(),
                        new FolderNodeResponse(ID, ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME)))
        );
    }
}