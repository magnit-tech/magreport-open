package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetTypeResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.DataSetService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.magnit.magreportbackend.controller.DataSetController.DATASET_ADD_FOLDER;
import static ru.magnit.magreportbackend.controller.DataSetController.DATASET_EDIT;
import static ru.magnit.magreportbackend.controller.DataSetController.DATASET_GET;
import static ru.magnit.magreportbackend.controller.DataSetController.DATASET_GET_FOLDER;
import static ru.magnit.magreportbackend.controller.DataSetController.DATASET_RENAME_FOLDER;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class DataSetControllerTest {

    private static final String USER_NAME = "TestUser";
    private static final Long ID = 1L;
    private static final Long DATASOURCE_ID = 3L;
    private static final Long FOLDER_ID = 2L;
    private static final Long TYPE_ID = 1L;
    private static final String NAME = "Test folder";
    private static final String DESCRIPTION = "Folder description";
    private static final String CATALOG_NAME = "catalog";
    private static final String SCHEMA_NAME = "schema";
    private static final String OBJECT_NAME = "object";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);
    private static final Long DATASET_TYPE_ID = 1L;
    private static final String DATASET_TYPE_NAME = "Type name";
    private static final String DATASET_TYPE_DESCRIPTION = "Type description";
    private static final LocalDateTime DATASET_TYPE_CREATED = LocalDateTime.now();
    private static final LocalDateTime DATASET_TYPE_MODIFIED = DATASET_TYPE_CREATED.plusMinutes(2);
    private static final Long DATASET_DATA_TYPE_ID = 1L;
    private static final String DATASET_DATA_TYPE_NAME = "INTEGER";
    private static final String DATASET_DATA_TYPE_DESCRIPTION = "Integer data type";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @InjectMocks
    private DataSetController controller;

    @Mock
    private DataSetService service;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

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

        mockMvc
                .perform(post(DATASET_GET_FOLDER)
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
                .andExpect(jsonPath("$.data.dataSets", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addDataSetFolder() throws Exception {
        when(service.addFolder(any())).thenReturn(getFolderResponse());

        mockMvc
                .perform(post(DATASET_ADD_FOLDER)
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
                .andExpect(jsonPath("$.data.dataSets", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).addFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void renameDataSetFolder() throws Exception {
        when(service.renameFolder(any())).thenReturn(getFolderResponse());

        mockMvc
                .perform(post(DATASET_RENAME_FOLDER)
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
                .andExpect(jsonPath("$.data.dataSets", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).renameFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDataSet() throws Exception {
        when(service.getDataSet(any())).thenReturn(getDataSetResponse());

        mockMvc
                .perform(post(DATASET_GET)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDataSetRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.catalogName", is(CATALOG_NAME)))
                .andExpect(jsonPath("$.data.schemaName", is(SCHEMA_NAME)))
                .andExpect(jsonPath("$.data.objectName", is(OBJECT_NAME)))
                .andExpect(jsonPath("$.data.fields", hasSize(1)));

        verify(service).getDataSet(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void editDataSet() throws Exception {
        when(service.editDataSet(any())).thenReturn(getDataSetResponse());

        mockMvc
                .perform(post(DATASET_EDIT)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDataSetAddRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.catalogName", is(CATALOG_NAME)))
                .andExpect(jsonPath("$.data.schemaName", is(SCHEMA_NAME)))
                .andExpect(jsonPath("$.data.objectName", is(OBJECT_NAME)))
                .andExpect(jsonPath("$.data.fields", hasSize(1)));

        verify(service).editDataSet(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteDataSetFolder() throws Exception {

        mockMvc
                .perform(post(DataSetController.DATASET_DELETE_FOLDER)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.success", is(true)));

        verify(service).deleteFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteDataSet() throws Exception {

        mockMvc
                .perform(post(DataSetController.DATASET_DELETE)
                        .content(objectMapper.writeValueAsString(getDataSetRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.success", is(true)));

        verify(service).deleteDataSet(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void createDataSetFromDBMetaData() throws Exception {

        given(service.createDataSetFromDBMetaData(any())).willReturn(getDataSetResponse());

        mockMvc
                .perform(post(DataSetController.DATASET_ADD)
                        .content(objectMapper.writeValueAsString(getMetaDataRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.catalogName", is(CATALOG_NAME)))
                .andExpect(jsonPath("$.data.objectName", is(OBJECT_NAME)))
                .andExpect(jsonPath("$.data.schemaName", is(SCHEMA_NAME)))
        ;

        verify(service).createDataSetFromDBMetaData(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDataSetTypes() throws Exception {

        given(service.getDataSetTypes()).willReturn(getDataSetTypeResponse());

        mockMvc
                .perform(post(DataSetController.DATASET_GET_TYPES)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].id", is(DATASET_TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data[0].name", is(DATASET_TYPE_NAME)))
                .andExpect(jsonPath("$.data[0].description", is(DATASET_TYPE_DESCRIPTION)))
        ;

        verify(service).getDataSetTypes();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDataSetDataTypes() throws Exception {
        when(service.getDataSetDataTypes()).thenReturn(getDataSetDataTypeResponse());

        mockMvc
                .perform(post(DataSetController.DATASET_GET_DATA_TYPES)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].id", is(DATASET_DATA_TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data[0].name", is(DATASET_DATA_TYPE_NAME)))
                .andExpect(jsonPath("$.data[0].description", is(DATASET_DATA_TYPE_DESCRIPTION)));
    }

    @Test
    void refreshDataSet() throws Exception {

        when(service.refreshDataSet(any())).thenReturn(getDataSetResponse());

        mockMvc
                .perform(post(DataSetController.DATASET_REFRESH)
                        .content(objectMapper.writeValueAsString(getDataSetRequest()))
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.catalogName", is(CATALOG_NAME)))
                .andExpect(jsonPath("$.data.objectName", is(OBJECT_NAME)))
                .andExpect(jsonPath("$.data.schemaName", is(SCHEMA_NAME)))
                .andExpect(jsonPath("$.data.fields", hasSize(1)));
        verify(service).refreshDataSet(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDataSetDependants() throws Exception {

        when(service.getDataSetDependants(any())).thenReturn(getDataSetDependenciesResponse());

        mockMvc
                .perform(post(DataSetController.DATASET_GET_DEPENDENT_OBJECTS)
                        .content(objectMapper.writeValueAsString(getDataSetRequest()))
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.objectName", is(OBJECT_NAME)))
                .andExpect(jsonPath("$.data.schemaName", is(SCHEMA_NAME)))
                .andExpect(jsonPath("$.data.valid", is(true)))
                .andExpect(jsonPath("$.data.creator.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.asmSecurities", hasSize(1)))
                .andExpect(jsonPath("$.data.securityFilters", hasSize(1)))
                .andExpect(jsonPath("$.data.filterInstances", hasSize(1)))
                .andExpect(jsonPath("$.data.reports", hasSize(1)))
        ;

        verify(service).getDataSetDependants(any());
        verifyNoMoreInteractions(service);
    }

    private List<DataSetTypeResponse> getDataSetTypeResponse() {
        return Collections.singletonList(
                new DataSetTypeResponse(
                        DATASET_TYPE_ID,
                        DATASET_TYPE_NAME,
                        DATASET_TYPE_DESCRIPTION,
                        DATASET_TYPE_CREATED,
                        DATASET_TYPE_MODIFIED
                ));
    }

    private List<DataSetDataTypeResponse> getDataSetDataTypeResponse() {
        return Collections.singletonList(new DataSetDataTypeResponse()
                .setId(DATASET_DATA_TYPE_ID)
                .setName(DATASET_DATA_TYPE_NAME)
                .setDescription(DATASET_DATA_TYPE_DESCRIPTION)
        );
    }

    private DataSetCreateFromMetaDataRequest getMetaDataRequest() {

        return new DataSetCreateFromMetaDataRequest();
    }

    private DataSetResponse getDataSetResponse() {
        return new DataSetResponse()
                .setId(ID)
                .setTypeId(TYPE_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCatalogName(CATALOG_NAME)
                .setObjectName(OBJECT_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setFields(Collections.singletonList(new DataSetFieldResponse()));
    }

    private DataSetRequest getDataSetRequest() {
        return new DataSetRequest()
                .setId(ID);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private DataSetAddRequest getDataSetAddRequest() {
        return new DataSetAddRequest()
                .setId(DATASOURCE_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFolderId(FOLDER_ID)
                .setTypeId(TYPE_ID)
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(OBJECT_NAME)
                .setFields(Collections.singletonList(new DataSetFieldAddRequest()));
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private DataSetFolderResponse getFolderResponse() {
        return new DataSetFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSetFolderResponse()))
                .setDataSets(Collections.singletonList(new DataSetResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private DataSetDependenciesResponse getDataSetDependenciesResponse() {
        return new DataSetDependenciesResponse()
                .setId(ID)
                .setTypeId(TYPE_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setObjectName(OBJECT_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setValid(true)
                .setCreator(new UserResponse().setId(ID))
                .setAsmSecurities(Collections.singletonList(null))
                .setSecurityFilters(Collections.singletonList(null))
                .setFilterInstances(Collections.singletonList(null))
                .setReports(Collections.singletonList(null));
    }
}