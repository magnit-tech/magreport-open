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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.*;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.*;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.service.SecurityFilterService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.magnit.magreportbackend.controller.SecurityFilterController.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;


@ExtendWith(MockitoExtension.class)
class SecurityFilterControllerTest {

    private static final Long FOLDER_ID = 1L;
    private static final Long ID = 5L;
    private static final Long FILTER_INSTANCE_ID = 44L;
    private static final Long PARENT_ID = 10L;
    private static final Long ROLE_ID = 22L;
    private static final Long TYPE_ID = 55L;
    private static final String NAME = "Name";
    private static final String DESCRIPTION = "Desc";
    private static final String USER_NAME = "user";
    private static final FolderAuthorityEnum AUTHORITY = FolderAuthorityEnum.READ;
    private static final FilterOperationTypeEnum OPERATION_TYPE = FilterOperationTypeEnum.IS_BETWEEN;
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();
    private static final String CATALOG_NAME = "catalog";
    private static final String SCHEMA_NAME = "schema";
    private static final String OBJECT_NAME = "Obj";
    private static final Boolean IS_VALID = true;
    private static final Long FILTER_INSTANCE_FIELD_ID = 99L;
    private static final Long DATASET_FIELD_ID = 66L;

    @Mock
    private SecurityFilterService service;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SecurityFilterController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @BeforeEach
    public void initMock() {
        when(authentication.getName()).thenReturn(USER_NAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);
    }

    @Test
    void getDataSetFolder() throws Exception {

        when(service.getFolder(any())).thenReturn(getFolderResponse());

        final var actions = mockMvc.perform(post(SECURITY_FILTER_GET_FOLDER)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertSecurityFilterFolderResponseContent(actions);

        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addDataSetFolder() throws Exception {

        when(service.addFolder(any())).thenReturn(getFolderResponse());

        final var actions = mockMvc.perform(post(SECURITY_FILTER_ADD_FOLDER)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderAddRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertSecurityFilterFolderResponseContent(actions);

        verify(service).addFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void renameDataSetFolder() throws Exception {

        when(service.renameFolder(any())).thenReturn(getFolderResponse());

        final var actions = mockMvc.perform(post(SECURITY_FILTER_RENAME_FOLDER)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderRenameRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertSecurityFilterFolderResponseContent(actions);

        verify(service).renameFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteDataSetFolder() throws Exception {

        mockMvc.perform(post(SECURITY_FILTER_DELETE_FOLDER)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Security filter folder with ID:" + FOLDER_ID + " successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())))
        ;

        verify(service).deleteFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addSecurityFilter() throws Exception {

        when(service.addSecurityFilter(any())).thenReturn(getSecurityFilterResponse());

        final var actions = mockMvc.perform(post(SECURITY_FILTER_ADD)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getSecurityFilterAddRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));

        assertSecurityFilterResponseContent(actions);

        verify(service).addSecurityFilter(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void editSecurityFilter() throws Exception {

        when(service.editSecurityFilter(any())).thenReturn(getSecurityFilterResponse());

        final var actions = mockMvc.perform(post(SECURITY_FILTER_EDIT)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getSecurityFilterAddRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));

        assertSecurityFilterResponseContent(actions);

        verify(service).editSecurityFilter(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getSecurityFilter() throws Exception {

        when(service.getSecurityFilter(any())).thenReturn(getSecurityFilterResponse());

        final var actions = mockMvc.perform(post(SECURITY_FILTER_GET)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getSecurityFilterRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertSecurityFilterResponseContent(actions);

        verify(service).getSecurityFilter(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteSecurityFilter() throws Exception {


        mockMvc.perform(post(SECURITY_FILTER_DELETE)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getSecurityFilterRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", is(nullValue())))
        ;

        verify(service).deleteSecurityFilter(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void setRoleSettings() throws Exception {

        when(service.setRoleSettings(any())).thenReturn(getSecurityFilterRoleSettingsResponse());

        final var acitons = mockMvc.perform(post(SECURITY_FILTER_SET_ROLE_SETTINGS)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getSecurityFilterSetRoleRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertSecurityFilterRoleSettingsResponseContent(acitons);

        verify(service).setRoleSettings(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getRoleSettings() throws Exception {

        when(service.getRoleSettings(any())).thenReturn(getSecurityFilterRoleSettingsResponse());

        final var actions = mockMvc.perform(post(SECURITY_FILTER_GET_ROLE_SETTINGS)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getSecurityFilterRequest()))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")));

        assertSecurityFilterRoleSettingsResponseContent(actions);

        verify(service).getRoleSettings(any());
        verifyNoMoreInteractions(service);
    }

    private SecurityFilterFolderResponse getFolderResponse() {
        return new SecurityFilterFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setAuthority(AUTHORITY)
                .setSecurityFilters(Collections.singletonList(getSecurityFilterResponse()))
                .setChildFolders(Collections.emptyList())
                .setParentId(PARENT_ID)
                .setCreated(CREATED_DATE)
                .setModified(MODIFIED_DATE);
    }

    private SecurityFilterResponse getSecurityFilterResponse() {
        return new SecurityFilterResponse(
                ID,
                new FilterInstanceResponse(),
                FilterOperationTypeEnum.IS_BETWEEN,
                NAME,
                DESCRIPTION,
                Collections.singletonList(new SecurityFilterDataSetResponse(
                        new DataSetResponse()
                                .setId(ID)
                                .setName(NAME)
                                .setDescription(DESCRIPTION)
                                .setUserName(USER_NAME)
                                .setTypeId(TYPE_ID)
                                .setCreated(CREATED_DATE)
                                .setModified(MODIFIED_DATE)
                                .setCatalogName(CATALOG_NAME)
                                .setSchemaName(SCHEMA_NAME)
                                .setObjectName(OBJECT_NAME)
                                .setIsValid(IS_VALID),
                        Collections.singletonList(new FieldMappingResponse(FILTER_INSTANCE_FIELD_ID, DATASET_FIELD_ID))
                )),
                Collections.singletonList(new RoleSettingsResponse(
                        new RoleResponse()
                                .setId(ID)
                                .setName(NAME)
                                .setDescription(DESCRIPTION)
                                .setTypeId(TYPE_ID),
                        Collections.singletonList(new Tuple())
                )),
                USER_NAME,
                CREATED_DATE,
                MODIFIED_DATE,
                Collections.emptyList()
        );
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(FOLDER_ID);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(PARENT_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private SecurityFilterAddRequest getSecurityFilterAddRequest() {
        return new SecurityFilterAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFilterInstanceId(FILTER_INSTANCE_ID)
                .setOperationType(OPERATION_TYPE)
                .setFolderId(FOLDER_ID)
                .setDataSets(Collections.singletonList(new SecurityFilterDataSetAddRequest()));
    }

    private SecurityFilterRequest getSecurityFilterRequest() {
        return new SecurityFilterRequest()
                .setId(ID);
    }

    private SecurityFilterRoleSettingsResponse getSecurityFilterRoleSettingsResponse() {
        return new SecurityFilterRoleSettingsResponse(
                ID,
                Collections.singletonList(new RoleSettingsResponse(
                        new RoleResponse()
                                .setId(ROLE_ID)
                                .setName(NAME)
                                .setDescription(DESCRIPTION)
                                .setTypeId(TYPE_ID),
                        Collections.singletonList(new Tuple())
                ))
        );
    }

    private SecurityFilterSetRoleRequest getSecurityFilterSetRoleRequest() {
        return new SecurityFilterSetRoleRequest()
                .setSecurityFilterId(ID)
                .setRoleSettings(Collections.singletonList(new RoleSettingsRequest()));
    }

    private void assertSecurityFilterResponseContent(ResultActions actions) throws Exception {
        actions
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.operationType", is(OPERATION_TYPE.name())))
                .andExpect(jsonPath("$.data.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.dataSets", hasSize(1)))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.catalogName", is(CATALOG_NAME)))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.schemaName", is(SCHEMA_NAME)))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.objectName", is(OBJECT_NAME)))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.name", is(NAME)))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.isValid", is(IS_VALID)))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.dataSets[0].dataSet.modified", is(formatter.format(MODIFIED_DATE))))
                .andExpect(jsonPath("$.data.dataSets[0].fields", hasSize(1)))
                .andExpect(jsonPath("$.data.dataSets[0].fields[0].dataSetFieldId", is(DATASET_FIELD_ID.intValue())))
                .andExpect(jsonPath("$.data.dataSets[0].fields[0].filterInstanceFieldId", is(FILTER_INSTANCE_FIELD_ID.intValue())))
                .andExpect(jsonPath("$.data.roleSettings", hasSize(1)))
                .andExpect(jsonPath("$.data.roleSettings[0].role.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.roleSettings[0].role.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.roleSettings[0].role.name", is(NAME)))
                .andExpect(jsonPath("$.data.roleSettings[0].role.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.roleSettings[0].tuples", hasSize(1)))
                .andExpect(jsonPath("$.data.filterInstance", is(notNullValue())))
                .andExpect(jsonPath("$.data.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.modified", is(formatter.format(MODIFIED_DATE))));
    }

    private void assertSecurityFilterRoleSettingsResponseContent(ResultActions actions) throws Exception {
        actions
                .andExpect(jsonPath("$.data.securityFilterId", is(ID.intValue())))
                .andExpect(jsonPath("$.data.roleSettings", hasSize(1)))
                .andExpect(jsonPath("$.data.roleSettings[0].role.id", is(ROLE_ID.intValue())))
                .andExpect(jsonPath("$.data.roleSettings[0].role.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.roleSettings[0].role.name", is(NAME)))
                .andExpect(jsonPath("$.data.roleSettings[0].role.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.roleSettings[0].tuples", hasSize(1)));
    }

    private void assertSecurityFilterFolderResponseContent(ResultActions actions) throws Exception {
        actions
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.authority", is(AUTHORITY.name())))
                .andExpect(jsonPath("$.data.securityFilters", hasSize(1)))
                .andExpect(jsonPath("$.data.childFolders", hasSize(0)))
                .andExpect(jsonPath("$.data.parentId", is(PARENT_ID.intValue())))
                .andExpect(jsonPath("$.data.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.modified", is(formatter.format(MODIFIED_DATE))));
    }
}