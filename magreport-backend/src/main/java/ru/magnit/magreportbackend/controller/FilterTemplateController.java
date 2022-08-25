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
import ru.magnit.magreportbackend.dto.request.filtertemplate.FilterTemplateRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterFieldTypeResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterOperationTypeResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTypeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.service.FilterTemplateService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление шаблонами фильтров")
public class FilterTemplateController {

    public static final String FILTER_TEMPLATE_GET_FOLDER = "/api/v1/filter-template/get-folder";
    public static final String FILTER_TEMPLATE_ADD_FOLDER = "/api/v1/filter-template/add-folder";
    public static final String FILTER_TEMPLATE_RENAME_FOLDER = "/api/v1/filter-template/rename-folder";
    public static final String FILTER_TEMPLATE_DELETE_FOLDER = "/api/v1/filter-template/delete-folder";
    public static final String FILTER_TEMPLATE_GET = "/api/v1/filter-template/get";
    public static final String FILTER_TEMPLATE_GET_OPERATION_TYPES = "/api/v1/filter-template/get-operation-types";
    public static final String FILTER_TEMPLATE_GET_FILTER_TYPES = "/api/v1/filter-template/get-filter-types";
    public static final String FILTER_TEMPLATE_GET_FIELD_TYPES = "/api/v1/filter-template/get-field-types";
    public static final String FILTER_TEMPLATE_SEARCH = "/api/v1/filter-template/search";

    private final FilterTemplateService service;

    @Operation(summary = "Получение каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_GET_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterTemplateFolderResponse> getFilterTemplateFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterTemplateFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_ADD_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterTemplateFolderResponse> addFilterTemplateFolder(
            @RequestBody
                    FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterTemplateFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.addFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование наименования и описания каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_RENAME_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterTemplateFolderResponse> renameFilterTemplateFolder(
            @RequestBody
                    FolderRenameRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterTemplateFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.renameFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_DELETE_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteFilterTemplateFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("FilterTemplate folder with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение шаблона фильтра по id")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FilterTemplateResponse> getFilterTemplate(
            @RequestBody
                    FilterTemplateRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FilterTemplateResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterTemplate(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка возможных операций")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_GET_OPERATION_TYPES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<FilterOperationTypeResponse> getFilterOperationTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<FilterOperationTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterOperationTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка типов фильтров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_GET_FILTER_TYPES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<FilterTypeResponse> getFilterTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<FilterTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка типов полей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_GET_FIELD_TYPES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<FilterFieldTypeResponse> getFilterFieldTypes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<FilterFieldTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getFilterFieldTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск шаблонов фильтров")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FILTER_TEMPLATE_SEARCH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchFilterTemplate(
            @RequestBody
                    FolderSearchRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderSearchResponse>builder()
                .success(true)
                .message("")
                .data(service.searchFilterTemplate(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
