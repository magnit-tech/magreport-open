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
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetTypeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.service.DataSetService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление наборами данных")
public class DataSetController {

    public static final String DATASET_GET_FOLDER = "/api/v1/dataset/get-folder";
    public static final String DATASET_ADD_FOLDER = "/api/v1/dataset/add-folder";
    public static final String DATASET_RENAME_FOLDER = "/api/v1/dataset/rename-folder";
    public static final String DATASET_DELETE_FOLDER = "/api/v1/dataset/delete-folder";
    public static final String DATASET_FOLDER_CHANGE_PARENT = "/api/v1/dataset/change-parent-folder";
    public static final String DATASET_CHANGE_PARENT_FOLDER = "/api/v1/dataset/change-folder";
    public static final String DATASET_COPY = "/api/v1/dataset/copy";
    public static final String DATASET_GET = "/api/v1/dataset/get";
    public static final String DATASET_ADD = "/api/v1/dataset/add";
    public static final String DATASET_EDIT = "/api/v1/dataset/edit";
    public static final String DATASET_DELETE = "/api/v1/dataset/delete";
    public static final String DATASET_GET_TYPES = "/api/v1/dataset/get-types";
    public static final String DATASET_GET_DATA_TYPES = "/api/v1/dataset/get-data-types";
    public static final String DATASET_REFRESH = "/api/v1/dataset/refresh";
    public static final String DATASET_GET_DEPENDENT_OBJECTS = "/api/v1/dataset/get-dependent-objects";
    public static final String DATASET_ADD_PROCEDURE = "/api/v1/dataset/add-procedure";
    public static final String DATASET_SEARCH = "/api/v1/dataset/search";
    public static final String DATASET_FOLDER_COPY = "/api/v1/dataset/copy-folder";

    private final DataSetService service;

    @Operation(summary = "Получение каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_GET_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetFolderResponse> getDataSetFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_FOLDER_CHANGE_PARENT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetFolderResponse> changeDataSetFolderParent(
            @RequestBody
                    FolderChangeParentRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.changeParentFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога набора данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_CHANGE_PARENT_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> changeDataSetParentFolder(
        @RequestBody
            ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.changeDataSetParentFolder(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("Objects successfully moved")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Копирование набора данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_COPY,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> copyDataSets(
        @RequestBody
            ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.copyDataSets(request);

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
    @PostMapping(value = DATASET_ADD_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetFolderResponse> addDataSetFolder(
            @RequestBody
                    FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.addFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование наименования и описания каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_RENAME_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetFolderResponse> renameDataSetFolder(
            @RequestBody
                    FolderRenameRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.renameFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_DELETE_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteDataSetFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("DataSet folder with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование набора данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetResponse> editDataSet(
            @RequestBody
                    DataSetAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetResponse>builder()
                .success(true)
                .message("")
                .data(service.editDataSet(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление набора данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteDataSet(
            @RequestBody
                    DataSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteDataSet(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("DataSet with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение набора данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetResponse> getDataSet(
            @RequestBody
                    DataSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetResponse>builder()
                .success(true)
                .message("")
                .data(service.getDataSet(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Создание набора данных на основе метаданных объекта БД")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetResponse> createDataSetFromDBMetaData(
            @RequestBody
                    DataSetCreateFromMetaDataRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetResponse>builder()
                .success(true)
                .message("")
                .data(service.createDataSetFromDBMetaData(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка типов наборов данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_GET_TYPES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<DataSetTypeResponse> getDataSetTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<DataSetTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getDataSetTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех возможных типов полей у наборов данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_GET_DATA_TYPES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<DataSetDataTypeResponse> getDataSetDataTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<DataSetDataTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getDataSetDataTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Обновление набора данных из источника")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_REFRESH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetResponse> refreshDataSet(
            @RequestBody
                    DataSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetResponse>builder()
                .success(true)
                .message("")
                .data(service.refreshDataSet(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка зависимых от набора данных объектов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_GET_DEPENDENT_OBJECTS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetDependenciesResponse> getDataSetDependants(
            @RequestBody
                    DataSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetDependenciesResponse>builder()
                .success(true)
                .message("")
                .data(service.getDataSetDependants(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение набора данных хранимых процедур")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_ADD_PROCEDURE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<DataSetResponse> addDataSetProcedure(
            @RequestBody
                    DataSetCreateFromMetaDataRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<DataSetResponse>builder()
                .success(true)
                .message("")
                .data(service.addDataSetFromProcedure(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск наборов данных")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_SEARCH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchDataSet(
            @RequestBody
                    FolderSearchRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderSearchResponse>builder()
                .success(true)
                .message("")
                .data(service.searchDataSet(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Копирование папки")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = DATASET_FOLDER_COPY,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<DataSetFolderResponse> copyDataSetFolders(
            @RequestBody
                    CopyFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<DataSetFolderResponse>builder()
                .success(true)
                .message("Objects successfully copied")
                .data(service.copyDataSetFolders(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
