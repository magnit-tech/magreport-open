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
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceValuesResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.service.FilterInstanceService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление экземплярами фильтров")
public class FilterInstanceController {

    public static final String FILTER_INSTANCE_GET_FOLDER = "/api/v1/filter-instance/get-folder";
    public static final String FILTER_INSTANCE_ADD_FOLDER = "/api/v1/filter-instance/add-folder";
    public static final String FILTER_INSTANCE_RENAME_FOLDER = "/api/v1/filter-instance/rename-folder";
    public static final String FILTER_INSTANCE_DELETE_FOLDER = "/api/v1/filter-instance/delete-folder";
    public static final String FILTER_INSTANCE_FOLDER_CHANGE_PARENT = "/api/v1/filter-instance/change-parent-folder";
    public static final String FILTER_INSTANCE_CHANGE_PARENT_FOLDER = "/api/v1/filter-instance/change-folder";
    public static final String FILTER_INSTANCE_COPY = "/api/v1/filter-instance/copy";
    public static final String FILTER_INSTANCE_GET = "/api/v1/filter-instance/get";
    public static final String FILTER_INSTANCE_ADD = "/api/v1/filter-instance/add";
    public static final String FILTER_INSTANCE_EDIT = "/api/v1/filter-instance/edit";
    public static final String FILTER_INSTANCE_DELETE = "/api/v1/filter-instance/delete";
    public static final String FILTER_INSTANCE_GET_VALUES = "/api/v1/filter-instance/get-values";
    public static final String FILTER_INSTANCE_GET_CHILD_NODES = "/api/v1/filter-instance/get-child-nodes";
    public static final String FILTER_INSTANCE_SEARCH = "/api/v1/filter-instance/search";
    public static final String FILTER_INSTANCE_GET_DEPENDENT_OBJECTS = "/api/v1/filter-instance/get-dependent-objects";
    public static final String FILTER_INSTANCE_FOLDER_COPY = "/api/v1/filter-instance/copy-folder";

    private final FilterInstanceService service;

    @Operation(summary = "Получение каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_GET_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceFolderResponse> getFilterInstanceFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родителя каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_FOLDER_CHANGE_PARENT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceFolderResponse> changeFilterInstanceFolderParent(
            @RequestBody
                    FolderChangeParentRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.changeParentFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога экземпляра фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_CHANGE_PARENT_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> changeFilterInstanceParentFolder(
            @RequestBody
                    ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.changeFilterInstanceParentFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Objects successfully moved")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Копирование экземпляра фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_COPY,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> copyFilterInstance(
            @RequestBody
                    ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.copyFilterInstance(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Objects successfully moved")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_ADD_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceFolderResponse> addFilterInstanceFolder(
            @RequestBody FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.addFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование наименования и описания каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_RENAME_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceFolderResponse> renameFilterInstanceFolder(
            @RequestBody
                    FolderRenameRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.renameFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_DELETE_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteFilterInstanceFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("FilterInstance folder with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового экземпляра фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceResponse> addFilterInstance(
            @RequestBody
                    FilterInstanceAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceResponse>builder()
                .success(true)
                .message("")
                .data(service.addFilterInstance(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление экземпляра фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteFilterInstance(
            @RequestBody
                    FilterInstanceRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteFilterInstance(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("FilterInstance with id:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение экземпляра фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceResponse> getFilterInstance(
            @RequestBody
                    FilterInstanceRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterInstance(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового экземпляра фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceResponse> editFilterInstance(
            @RequestBody
                    FilterInstanceAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceResponse>builder()
                .success(true)
                .message("")
                .data(service.editFilterInstance(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение значений фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_GET_VALUES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceValuesResponse> getFilterInstanceValues(
            @RequestBody
                    ListValuesRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceValuesResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterInstanceValues(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение значений дочерних узлов иерархического фильтра")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_GET_CHILD_NODES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceChildNodesResponse> getChildNodes(
            @RequestBody
                    ChildNodesRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceChildNodesResponse>builder()
                .success(true)
                .message("")
                .data(service.getChildNodes(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск экземпляров фильтров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_SEARCH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchFilterInstance(
            @RequestBody
                    FolderSearchRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderSearchResponse>builder()
                .success(true)
                .message("")
                .data(service.searchFilterInstance(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка зависимых от экземпляра фильтра объектов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_GET_DEPENDENT_OBJECTS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterInstanceDependenciesResponse> getFilterInstanceDependants(
            @RequestBody
                    FilterInstanceRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterInstanceDependenciesResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterInstanceDependants(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Копирование каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_INSTANCE_FOLDER_COPY,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<FilterInstanceFolderResponse> copyFilterInstanceFolder(
            @RequestBody
                    CopyFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<FilterInstanceFolderResponse>builder()
                .success(true)
                .message("Folders successfully moved")
                .data(service.copyFilterInstanceFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}