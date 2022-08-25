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
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceObjectFieldsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemaObjectsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemasRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceObjectResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.DataSourceService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_ADD;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_ADD_FOLDER;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_DELETE;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_DELETE_FOLDER;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_EDIT;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_CATALOGS;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_DATASETS;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_FOLDER;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_GET_TYPES;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_OBJECTS;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_OBJECT_FIELDS;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_PROCEDURES;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_PROCEDURE_FIELDS;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_GET_SCHEMAS;
import static ru.magnit.magreportbackend.controller.DataSourceController.DATASOURCE_RENAME_FOLDER;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class DataSourceControllerTest {

    private static final short POOL_SIZE = (short) 5;
    private final Long ID = 1L;
    private final String NAME = "name";
    private final String DESCRIPTION = "description";
    private final String URL = "URL";
    private final String USER_NAME = "user";
    private final String PASSWORD = "pass";
    private final String SCHEMA_NAME = "schema";
    private static final String FIELD_NAME = "field";
    private static final String OBJECT_TYPE = "object_type";
    private static final String COMMENT = "comment";
    private static final String CATALOG_NAME = "catalog";
    private static final DataSourceTypeEnum TYPE = DataSourceTypeEnum.TERADATA;
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @InjectMocks
    private DataSourceController controller;

    @Mock
    private DataSourceService dataSourceServiceMock;

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
    void dataSourceGetFolderTest() throws Exception {
        when(dataSourceServiceMock.getFolder(any())).thenReturn(getFolderResponse());
        this.mvc
                .perform(post(DATASOURCE_GET_FOLDER)
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
                .andExpect(jsonPath("$.data.dataSources", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(dataSourceServiceMock).getFolder(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
                .setId(ID);
    }

    private DataSourceFolderResponse getFolderResponse() {
        return new DataSourceFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new DataSourceFolderResponse()))
                .setDataSources(Collections.singletonList(new DataSourceResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    @Test
    void dataSourceAddFolderTest() throws Exception {
        when(dataSourceServiceMock.addFolder(any())).thenReturn(getFolderResponse());

        this.mvc
                .perform(post(DATASOURCE_ADD_FOLDER)
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
                .andExpect(jsonPath("$.data.dataSources", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(dataSourceServiceMock).addFolder(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    @Test
    void dataSourceRenameFolderTest() throws Exception {
        when(dataSourceServiceMock.renameFolder(any())).thenReturn(getFolderResponse());

        this.mvc
                .perform(post(DATASOURCE_RENAME_FOLDER)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getFolderRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
                .andExpect(jsonPath("$.data.dataSources", hasSize(1)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(dataSourceServiceMock).renameFolder(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    @Test
    void dataSourceGetTest() throws Exception {
        when(dataSourceServiceMock.getDataSource(any())).thenReturn(getDataSourceResponse());

        this.mvc
                .perform(post(DATASOURCE_GET)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDataSourceRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.url", is(URL)))
                .andExpect(jsonPath("$.data.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.poolSize", is((int)POOL_SIZE)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(dataSourceServiceMock).getDataSource(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceResponse getDataSourceResponse() {

        return new DataSourceResponse(
                ID,
                NAME,
                DESCRIPTION,
                URL,
                USER_NAME,
                new DataSourceTypeResponse(ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME),
                POOL_SIZE,
                "Creator",
                CREATED_TIME,
                MODIFIED_TIME
        );
    }

    private DataSourceRequest getDataSourceRequest() {
        return new DataSourceRequest()
                .setId(ID);
    }

    @Test
    void dataSourceAddTest() throws Exception {
        when(dataSourceServiceMock.addDataSource(any())).thenReturn(getDataSourceResponse());

        this.mvc
                .perform(post(DATASOURCE_ADD)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDataSourceAddRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.url", is(URL)))
                .andExpect(jsonPath("$.data.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.poolSize", is((int)POOL_SIZE)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(dataSourceServiceMock).addDataSource(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceAddRequest getDataSourceAddRequest() {
        return new DataSourceAddRequest()
                .setId(ID)
                .setFolderId(ID)
                .setTypeId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setUrl(URL)
                .setUserName(USER_NAME)
                .setPassword(PASSWORD);
    }

    @Test
    void dataSourceEditTest() throws Exception {
        when(dataSourceServiceMock.editDataSource(any())).thenReturn(getDataSourceResponse());

        this.mvc
                .perform(post(DATASOURCE_EDIT)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDataSourceAddRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.url", is(URL)))
                .andExpect(jsonPath("$.data.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.poolSize", is((int)POOL_SIZE)))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(dataSourceServiceMock).editDataSource(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    @Test
    void dataSourceGetCatalogsTest() throws Exception {
        when(dataSourceServiceMock.getCatalogs(any())).thenReturn(Collections.singletonList(""));

        this.mvc
                .perform(post(DATASOURCE_GET_CATALOGS)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDataSourceRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(dataSourceServiceMock).getCatalogs(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    @Test
    void dataSourceGetSchemasTest() throws Exception {
        when(dataSourceServiceMock.getSchemas(any())).thenReturn(Collections.singletonList(""));

        this.mvc
                .perform(post(DATASOURCE_GET_SCHEMAS)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSchemasRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(dataSourceServiceMock).getSchemas(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceSchemasRequest getSchemasRequest() {
        return new DataSourceSchemasRequest()
                .setId(ID)
                .setCatalogName(NAME);
    }

    @Test
    void dataSourceGetObjects() throws Exception {
        when(dataSourceServiceMock.getSchemaObjects(any())).thenReturn(Collections.singletonList(new DataSourceObjectResponse()));

        this.mvc
                .perform(post(DATASOURCE_GET_OBJECTS)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSchemaObjectsRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(dataSourceServiceMock).getSchemaObjects(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceSchemaObjectsRequest getSchemaObjectsRequest() {
        return new DataSourceSchemaObjectsRequest()
                .setId(ID)
                .setCatalogName(NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectType(SCHEMA_NAME);
    }

    @Test
    void dataSourceGetObjectsFieldsTest() throws Exception {
        when(dataSourceServiceMock.getObjectFields(any())).thenReturn(Collections.singletonList(new ObjectFieldResponse()));

        this.mvc
                .perform(post(DATASOURCE_GET_OBJECT_FIELDS)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSchemaObjectFieldsRequest()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(dataSourceServiceMock).getObjectFields(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceObjectFieldsRequest getSchemaObjectFieldsRequest() {
        return new DataSourceObjectFieldsRequest()
                .setId(ID)
                .setCatalogName(NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(SCHEMA_NAME);
    }

    @Test
    void deleteDataSourceFolder() throws Exception {
        final var request = getFolderRequest();

        this.mvc
                .perform(post(DATASOURCE_DELETE_FOLDER)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.data", nullValue()));

        verify(dataSourceServiceMock).deleteFolder(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    @Test
    void deleteDataSource() throws Exception {
        final var request = getDataSourceRequest();

        this.mvc
                .perform(post(DATASOURCE_DELETE)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.data", nullValue()));

        verify(dataSourceServiceMock).deleteDataSource(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    @Test
    void getDataSourceProcedures() throws Exception {
        when(dataSourceServiceMock.getSchemaProcedures(any())).thenReturn(Collections.singletonList(getDataSourceObjectResponse()));

        final var request = getDataSourceSchemaObjectsRequest();

        this.mvc
                .perform(post(DATASOURCE_GET_PROCEDURES)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data[0].name", is(NAME)))
                .andExpect(jsonPath("$.data[0].catalog", is(CATALOG_NAME)))
                .andExpect(jsonPath("$.data[0].schema", is(SCHEMA_NAME)))
                .andExpect(jsonPath("$.data[0].type", is(OBJECT_TYPE)))
                .andExpect(jsonPath("$.data[0].comment", is(COMMENT)));

        verify(dataSourceServiceMock).getSchemaProcedures(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceObjectResponse getDataSourceObjectResponse() {
        return new DataSourceObjectResponse()
                .setName(NAME)
                .setCatalog(CATALOG_NAME)
                .setComment(COMMENT)
                .setSchema(SCHEMA_NAME)
                .setType(OBJECT_TYPE);
    }

    private DataSourceSchemaObjectsRequest getDataSourceSchemaObjectsRequest() {
        return new DataSourceSchemaObjectsRequest()
                .setId(ID)
                .setSchemaName(SCHEMA_NAME)
                .setCatalogName(CATALOG_NAME)
                .setObjectType(OBJECT_TYPE);
    }

    @Test
    void getDataSourceProcedureFields() throws Exception {
        when(dataSourceServiceMock.getSchemaProcedureFields(any())).thenReturn(Collections.singletonList(getObjectFieldResponse()));

        final var request = getDataSourceObjectFieldsRequest();

        this.mvc
                .perform(post(DATASOURCE_GET_PROCEDURE_FIELDS)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data[0].fieldName", is(FIELD_NAME)))
                .andExpect(jsonPath("$.data[0].catalogName", is(CATALOG_NAME)))
                .andExpect(jsonPath("$.data[0].schemaName", is(SCHEMA_NAME)));

        verify(dataSourceServiceMock).getSchemaProcedureFields(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceObjectFieldsRequest getDataSourceObjectFieldsRequest() {
        return new DataSourceObjectFieldsRequest()
                .setId(ID)
                .setObjectName(NAME)
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME);
    }

    private ObjectFieldResponse getObjectFieldResponse() {
        return new ObjectFieldResponse()
                .setFieldName(FIELD_NAME)
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME); //TODO: and many more
    }

    @Test
    void getDataSourceTypes() throws Exception {

        when(dataSourceServiceMock.getDataSourceTypes()).thenReturn(Collections.singletonList(getDataSourceTypeResponse()));

        this.mvc
                .perform(post(DATASOURCE_GET_GET_TYPES)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data[0].id", is(ID.intValue())))
                .andExpect(jsonPath("$.data[0].name", is(NAME)))
                .andExpect(jsonPath("$.data[0].description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data[0].created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data[0].modified", is(MODIFIED_TIME.format(formatter))));

        verify(dataSourceServiceMock).getDataSourceTypes();
        verifyNoMoreInteractions(dataSourceServiceMock);

    }

    private DataSourceTypeResponse getDataSourceTypeResponse() {
        return new DataSourceTypeResponse(ID, NAME, DESCRIPTION, CREATED_TIME, MODIFIED_TIME);
    }

    @Test
    void getDataSourceDependencies() throws Exception {

        when(dataSourceServiceMock.getDataSourceDependencies(any())).thenReturn(getDataSourceDependenciesResponse());

        final var request = getDataSourceRequest();

        this.mvc
                .perform(post(DATASOURCE_GET_DATASETS)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.url", is(URL)))
                .andExpect(jsonPath("$.data.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.type", is(TYPE.name())))
                .andExpect(jsonPath("$.data.creator.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))))
                .andExpect(jsonPath("$.data.dataSets", hasSize(1)));

        verify(dataSourceServiceMock).getDataSourceDependencies(any());
        verifyNoMoreInteractions(dataSourceServiceMock);
    }

    private DataSourceDependenciesResponse getDataSourceDependenciesResponse() {
        return new DataSourceDependenciesResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setUrl(URL)
                .setUserName(USER_NAME)
                .setType(TYPE)
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME)
                .setCreator(new UserResponse().setId(ID))
                .setDataSets(Collections.singletonList(null));
    }
}