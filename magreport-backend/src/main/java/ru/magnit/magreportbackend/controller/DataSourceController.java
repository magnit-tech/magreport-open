package ru.magnit.magreportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceCheckRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceObjectFieldsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemaObjectsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemasRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceObjectResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.service.DataSourceService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление источниками данных")
public class DataSourceController {
    public static final String DATASOURCE_GET_FOLDER = "/api/v1/datasource/get-folder";
    public static final String DATASOURCE_ADD_FOLDER = "/api/v1/datasource/add-folder";
    public static final String DATASOURCE_RENAME_FOLDER = "/api/v1/datasource/rename-folder";
    public static final String DATASOURCE_DELETE_FOLDER = "/api/v1/datasource/delete-folder";
    public static final String DATASOURCE_FOLDER_CHANGE_PARENT = "/api/v1/datasource/change-parent-folder";
    public static final String DATASOURCE_CHANGE_PARENT_FOLDER = "/api/v1/datasource/change-folder";
    public static final String DATASOURCE_COPY = "/api/v1/datasource/copy";
    public static final String DATASOURCE_GET = "/api/v1/datasource/get";
    public static final String DATASOURCE_ADD = "/api/v1/datasource/add";
    public static final String DATASOURCE_EDIT = "/api/v1/datasource/edit";
    public static final String DATASOURCE_DELETE = "/api/v1/datasource/delete";
    public static final String DATASOURCE_CHECK = "/api/v1/datasource/check";
    public static final String DATASOURCE_CHECK_CONNECT = "/api/v1/datasource/check-connect";
    public static final String DATASOURCE_GET_CATALOGS = "/api/v1/datasource/get-catalogs";
    public static final String DATASOURCE_GET_SCHEMAS = "/api/v1/datasource/get-schemas";
    public static final String DATASOURCE_GET_OBJECTS = "/api/v1/datasource/get-objects";
    public static final String DATASOURCE_GET_OBJECT_FIELDS = "/api/v1/datasource/get-object-fields";
    public static final String DATASOURCE_GET_PROCEDURES = "/api/v1/datasource/get-procedures";
    public static final String DATASOURCE_GET_PROCEDURE_FIELDS = "/api/v1/datasource/get-procedure-fields";
    public static final String DATASOURCE_GET_GET_TYPES = "/api/v1/datasource/get-types";
    public static final String DATASOURCE_GET_DATASETS = "/api/v1/datasource/get-datasets";
    public static final String DATASOURCE_SEARCH = "/api/v1/datasource/search";
    public static final String DATASOURCE_FOLDER_COPY = "/api/v1/datasource/copy-folder";

    private final DataSourceService service;

    @Operation(summary = "Получение каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceFolderResponse> getDataSourceFolder(
        @RequestBody
        FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceFolderResponse>builder()
            .success(true)
            .message("")
            .data(service.getFolder(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_FOLDER_CHANGE_PARENT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceFolderResponse> changeDataSourceFolderParent(
        @RequestBody
        FolderChangeParentRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceFolderResponse>builder()
            .success(true)
            .message("")
            .data(service.changeParentFolder(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_CHANGE_PARENT_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> changeDataSourceParentFolder(
        @RequestBody
        ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.changeDataSourceParentFolder(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("Objects successfully moved")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Копирование источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_COPY,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> copyDataSource(
        @RequestBody
        ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.copyDataSource(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("Objects successfully copied")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_ADD_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceFolderResponse> addDataSourceFolder(
        @RequestBody
        FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceFolderResponse>builder()
            .success(true)
            .message("")
            .data(service.addFolder(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование наименования и описания каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_RENAME_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceFolderResponse> renameDataSourceFolder(
        @RequestBody
        FolderRenameRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceFolderResponse>builder()
            .success(true)
            .message("")
            .data(service.renameFolder(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_DELETE_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteDataSourceFolder(
        @RequestBody
        FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("DataSource folder with ID:" + request.getId() + " successfully deleted.")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_ADD,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceResponse> addDataSource(
        @RequestBody
        DataSourceAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceResponse>builder()
            .success(true)
            .message("")
            .data(service.addDataSource(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_EDIT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceResponse> editDataSource(
        @RequestBody
        DataSourceAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceResponse>builder()
            .success(true)
            .message("")
            .data(service.editDataSource(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_DELETE,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteDataSource(
        @RequestBody
        DataSourceRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteDataSource(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("DataSource with ID:" + request.getId() + " successfully deleted.")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceResponse> getDataSource(
        @RequestBody
        DataSourceRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceResponse>builder()
            .success(true)
            .message("")
            .data(service.getDataSource(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение каталогов источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_CATALOGS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<String> getDataSourceCatalogs(
        @RequestBody
        DataSourceRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<String>builder()
            .success(true)
            .message("")
            .data(service.getCatalogs(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Проверка источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_CHECK,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<Object> checkDataSource(
        @RequestBody
        DataSourceRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.checkDataSource(request);

        var response = ResponseList.builder()
            .success(true)
            .message("Data Source check successful")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Проверка параметров подключения к источнику данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_CHECK_CONNECT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> checkDataSourceConnectivity(
        @RequestBody
        DataSourceCheckRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.checkDataSourceConnectivity(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("Data Source check successful")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение схем источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_SCHEMAS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<String> getDataSourceSchemas(
        @RequestBody
        DataSourceSchemasRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<String>builder()
            .success(true)
            .message("")
            .data(service.getSchemas(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение объектов схемы источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_OBJECTS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<DataSourceObjectResponse> getDataSourceObjects(
        @RequestBody
        DataSourceSchemaObjectsRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<DataSourceObjectResponse>builder()
            .success(true)
            .message("")
            .data(service.getSchemaObjects(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение полей объекта источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_OBJECT_FIELDS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<ObjectFieldResponse> getDataSourceObjectFields(
        @RequestBody
        DataSourceObjectFieldsRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ObjectFieldResponse>builder()
            .success(true)
            .message("")
            .data(service.getObjectFields(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка процедур схемы источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_PROCEDURES,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<DataSourceObjectResponse> getDataSourceProcedures(
        @RequestBody
        DataSourceSchemaObjectsRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<DataSourceObjectResponse>builder()
            .success(true)
            .message("")
            .data(service.getSchemaProcedures(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение полей процедуры источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_PROCEDURE_FIELDS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<ObjectFieldResponse> getDataSourceProcedureFields(
        @RequestBody
        DataSourceObjectFieldsRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ObjectFieldResponse>builder()
            .success(true)
            .message("")
            .data(service.getSchemaProcedureFields(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение типов источников данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_GET_TYPES,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<DataSourceTypeResponse> getDataSourceTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<DataSourceTypeResponse>builder()
            .success(true)
            .message("")
            .data(service.getDataSourceTypes())
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение иерархии зависимых сущностей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_GET_DATASETS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSourceDependenciesResponse> getDataSourceDependencies(
        @RequestBody
        DataSourceRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSourceDependenciesResponse>builder()
            .success(true)
            .message("")
            .data(service.getDataSourceDependencies(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск источников данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_SEARCH,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchDataSource(
        @RequestBody
        FolderSearchRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderSearchResponse>builder()
            .success(true)
            .message("")
            .data(service.searchDataSource(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Копирование каталога источника данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASOURCE_FOLDER_COPY,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseList<DataSourceFolderResponse> copyDataSourceFolder(
        @RequestBody
        CopyFolderRequest request) {
        LogHelper.logInfoUserMethodStart();


        var response = ResponseList.<DataSourceFolderResponse>builder()
            .success(true)
            .message("Objects successfully copied")
            .data(service.copyDataSourceFolder(request))
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
