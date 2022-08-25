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
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.dto.request.filtertemplate.FilterTemplateRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.response.filtertemplate.*;
import ru.magnit.magreportbackend.service.FilterTemplateService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static ru.magnit.magreportbackend.controller.FilterTemplateController.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class FilterTemplateControllerTest {

    private final Long ID = 1L;
    private final Long FIELD_TYPE_ID = 5L;
    private final Long TYPE_ID = 10L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final String USERNAME = "User";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @InjectMocks
    private FilterTemplateController controller;

    @Mock
    private FilterTemplateService service;

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
    void getFilterTemplateFolder() throws Exception {
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FILTER_TEMPLATE_GET_FOLDER)
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
            .andExpect(jsonPath("$.data.filterTemplates", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
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
            .setChildFolders(Collections.singletonList(new FilterTemplateFolderResponse()))
            .setFilterTemplates(Collections.singletonList(new FilterTemplateResponse()))
            .setCreated(CREATED_TIME)
            .setModified(MODIFIED_TIME);
    }

    @Test
    void addFilterTemplateFolder() throws Exception {
        when(service.addFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FILTER_TEMPLATE_ADD_FOLDER)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getFolderAddRequest()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.filterTemplates", hasSize(1)))
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
    void renameFilterTemplateFolder() throws Exception {
        when(service.renameFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FILTER_TEMPLATE_RENAME_FOLDER)
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
            .andExpect(jsonPath("$.data.filterTemplates", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).renameFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteFilterTemplateFolder() throws Exception {

        mvc
                .perform(post(FILTER_TEMPLATE_DELETE_FOLDER)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("FilterTemplate folder with ID:" + ID + " successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).deleteFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getFilterTemplate() throws Exception {
        when(service.getFilterTemplate(any())).thenReturn(getFilterTemplateResponse());

        mvc
                .perform(post(FILTER_TEMPLATE_GET)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFilterTemplateRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.userName", is(USERNAME)))
                .andExpect(jsonPath("$.data.fields", hasSize(1)))
                .andExpect(jsonPath("$.data.supportedOperations", hasSize(1)))
                .andExpect(jsonPath("$.data.type.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFilterTemplate(any());
        verifyNoMoreInteractions(service);
    }

    private FilterTemplateRequest getFilterTemplateRequest() {
        return new FilterTemplateRequest()
                .setId(ID);
    }

    private FilterTemplateResponse getFilterTemplateResponse() {
        return new FilterTemplateResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setType(new FilterTypeResponse(ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME))
                .setUserName(USERNAME)
                .setFields(Collections.singletonList(new FilterTemplateFieldResponse()))
                .setSupportedOperations(Collections.singletonList(FilterOperationTypeEnum.IS_BETWEEN))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    @Test
    void getFilterOperationTypes() throws Exception {
        when(service.getFilterOperationTypes()).thenReturn(Collections.singletonList(getFilterOperationTypeResponse()));

        mvc
                .perform(post(FILTER_TEMPLATE_GET_OPERATION_TYPES)
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

        verify(service).getFilterOperationTypes();
        verifyNoMoreInteractions(service);
    }

    private FilterOperationTypeResponse getFilterOperationTypeResponse() {
        return new FilterOperationTypeResponse(
                ID,
                NAME,
                DESCRIPTION,
                CREATED_TIME,
                MODIFIED_TIME
        );
    }

    @Test
    void getFilterTypes() throws Exception {
        when(service.getFilterTypes()).thenReturn(Collections.singletonList(getFilterTypeResponse()));

        mvc
                .perform(post(FILTER_TEMPLATE_GET_FILTER_TYPES)
                        .accept(APPLICATION_JSON_VALUE)
                        .content("")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data[0].name", is(NAME)))
                .andExpect(jsonPath("$.data[0].description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data[0].created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data[0].modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFilterTypes();
        verifyNoMoreInteractions(service);
    }

    private FilterTypeResponse getFilterTypeResponse() {
        return new FilterTypeResponse(
                TYPE_ID,
                NAME,
                DESCRIPTION,
                CREATED_TIME,
                MODIFIED_TIME
        );
    }

    @Test
    void getFilterFieldTypes() throws Exception {
        when(service.getFilterFieldTypes()).thenReturn(Collections.singletonList(getFilterFieldTypeResponse()));

        mvc
                .perform(post(FILTER_TEMPLATE_GET_FIELD_TYPES)
                        .accept(APPLICATION_JSON_VALUE)
                        .content("")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(FIELD_TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data[0].name", is(NAME)))
                .andExpect(jsonPath("$.data[0].description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data[0].created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data[0].modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFilterFieldTypes();
        verifyNoMoreInteractions(service);
    }

    private FilterFieldTypeResponse getFilterFieldTypeResponse() {
        return new FilterFieldTypeResponse(
                FIELD_TYPE_ID,
                NAME,
                DESCRIPTION,
                CREATED_TIME,
                MODIFIED_TIME
        );
    }
}