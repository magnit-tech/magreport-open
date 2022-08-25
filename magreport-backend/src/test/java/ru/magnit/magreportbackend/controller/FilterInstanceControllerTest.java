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
import org.springframework.test.web.servlet.ResultActions;
import ru.magnit.magreportbackend.dto.request.filterinstance.*;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.response.filterinstance.*;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTypeResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.service.FilterInstanceService;

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
import static ru.magnit.magreportbackend.controller.FilterInstanceController.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class FilterInstanceControllerTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final String USERNAME = "User";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @InjectMocks
    private FilterInstanceController controller;

    @Mock
    private FilterInstanceService service;

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
    void getFilterInstanceFolder() throws Exception {
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FILTER_INSTANCE_GET_FOLDER)
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
            .andExpect(jsonPath("$.data.filterInstances", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    private FilterInstanceFolderResponse getFolderResponse() {
        return new FilterInstanceFolderResponse()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setChildFolders(Collections.singletonList(new FilterInstanceFolderResponse()))
            .setFilterInstances(Collections.singletonList(new FilterInstanceResponse()))
            .setCreated(CREATED_TIME)
            .setModified(MODIFIED_TIME);
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
            .setId(ID);
    }

    @Test
    void addFilterInstanceFolder() throws Exception {
        when(service.addFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FILTER_INSTANCE_ADD_FOLDER)
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
            .andExpect(jsonPath("$.data.filterInstances", hasSize(1)))
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
    void renameFilterInstanceFolder() throws Exception {
        when(service.renameFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(FILTER_INSTANCE_RENAME_FOLDER)
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
            .andExpect(jsonPath("$.data.filterInstances", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).renameFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteFilterInstanceFolder() throws Exception {

        mvc
                .perform(post(FILTER_INSTANCE_DELETE_FOLDER)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("FilterInstance folder with ID:" + ID + " successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).deleteFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addFilterInstance() throws Exception {
        when(service.addFilterInstance(any())).thenReturn(getFilterInstanceResponse());

        final var actions = this.mvc
            .perform(post(FILTER_INSTANCE_ADD)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getFilterInstanceAddRequest()))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")));

        assertFilterInstanceResponseContent(actions);

        verify(service).addFilterInstance(any());
        verifyNoMoreInteractions(service);
    }

    private FilterInstanceResponse getFilterInstanceResponse() {
        return new FilterInstanceResponse()
                .setId(ID)
                .setTemplateId(ID)
                .setDataSetId(ID)
                .setFolderId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setUserName(USERNAME)
                .setFields(Collections.singletonList(new FilterInstanceFieldResponse()))
                .setType(new FilterTypeResponse(ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FilterInstanceAddRequest getFilterInstanceAddRequest() {
        return new FilterInstanceAddRequest()
            .setId(ID)
            .setFolderId(ID)
            .setTemplateId(ID)
            .setDataSetId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setFields(Collections.singletonList(new FilterInstanceFieldAddRequest()));
    }

    @Test
    void deleteFilterInstance() throws Exception {

        mvc
                .perform(post(FILTER_INSTANCE_DELETE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFilterInstanceRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("FilterInstance with id:" + ID + " successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).deleteFilterInstance(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getFilterInstance() throws Exception {

        when(service.getFilterInstance(any())).thenReturn(getFilterInstanceResponse());

        final var actions = mvc
                .perform(post(FILTER_INSTANCE_GET)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFilterInstanceRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertFilterInstanceResponseContent(actions);

        verify(service).getFilterInstance(any());
        verifyNoMoreInteractions(service);

    }

    @Test
    void editFilterInstance() throws Exception {

        when(service.editFilterInstance(any())).thenReturn(getFilterInstanceResponse());

        final var actions = mvc
                .perform(post(FILTER_INSTANCE_EDIT)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getFilterInstanceAddRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertFilterInstanceResponseContent(actions);

        verify(service).editFilterInstance(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getFilterInstanceValues() throws Exception {

        when(service.getFilterInstanceValues(any())).thenReturn(getFilterInstanceValuesResponse());

        mvc
                .perform(post(FILTER_INSTANCE_GET_VALUES)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getListValuesRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.filter", is(not(anEmptyMap()))))
                .andExpect(jsonPath("$.data.tuples", hasSize(1)));

        verify(service).getFilterInstanceValues(any());
        verifyNoMoreInteractions(service);

    }

    @Test
    void getChildNodes() throws Exception {

        when(service.getChildNodes(any())).thenReturn(getFilterInstanceChildNodesResponse());

        mvc
                .perform(post(FILTER_INSTANCE_GET_CHILD_NODES)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getChildNodesRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.filter", is(not(anEmptyMap()))))
                .andExpect(jsonPath("$.data.fieldId", is(ID.intValue())))
                .andExpect(jsonPath("$.data.rootNode", is(not(anEmptyMap()))));

        verify(service).getChildNodes(any());
        verifyNoMoreInteractions(service);
    }

    private ChildNodesRequest getChildNodesRequest() {
        return new ChildNodesRequest()
                .setFilterId(ID);
    }

    private FilterInstanceChildNodesResponse getFilterInstanceChildNodesResponse() {
        return new FilterInstanceChildNodesResponse(
                getFilterInstanceResponse(),
                ID,
                new FilterNodeResponse(ID, 1L, "ID", NAME, Collections.emptyList())
        );
    }

    private FilterInstanceRequest getFilterInstanceRequest() {
        return new FilterInstanceRequest()
                .setId(ID);
    }

    private ListValuesRequest getListValuesRequest() {
        return new ListValuesRequest();
    }

    private FilterInstanceValuesResponse getFilterInstanceValuesResponse() {
        return new FilterInstanceValuesResponse(
                getFilterInstanceResponse(),
                Collections.singletonList(new Tuple())
        );
    }

    private void assertFilterInstanceResponseContent(ResultActions actions) throws Exception {
        actions
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.folderId", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.templateId", is(ID.intValue())))
                .andExpect(jsonPath("$.data.userName", is(USERNAME)))
                .andExpect(jsonPath("$.data.created", is(formatter.format(CREATED_TIME))))
                .andExpect(jsonPath("$.data.modified", is(formatter.format(MODIFIED_TIME))))
                .andExpect(jsonPath("$.data.fields", hasSize(1)))
                .andExpect(jsonPath("$.data.type.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.type.name", is(NAME)))
                .andExpect(jsonPath("$.data.type.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.type.created", is(formatter.format(CREATED_TIME))))
                .andExpect(jsonPath("$.data.type.modified", is(formatter.format(MODIFIED_TIME))));
    }
}