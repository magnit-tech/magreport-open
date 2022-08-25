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
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderAddReportRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.report.PublishedReportResponse;
import ru.magnit.magreportbackend.service.FolderService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление папками опубликованных отчетов")
public class FolderController {

    public static final String FOLDER_ADD = "/api/v1/folder/add-folder";
    public static final String FOLDER_GET = "/api/v1/folder/get-folder";
    public static final String FOLDER_RENAME = "/api/v1/folder/rename-folder";
    public static final String FOLDER_DELETE = "/api/v1/folder/delete-folder";
    public static final String FOLDER_ADD_REPORT = "/api/v1/folder/add-report";
    public static final String FOLDER_DELETE_REPORT = "/api/v1/folder/delete-report";
    public static final String FOLDER_SEARCH = "/api/v1/folder/search";
    public static final String FOLDER_CHANGE_PARENT = "/api/v1/folder/change-parent-folder";
    public static final String FOLDER_GET_PUBLISHED_REPORT = "/api/v1/folder/get-published-report";

    private final FolderService service;

    @Operation(summary = "Добавление нового каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderResponse> addFolder(
            @RequestBody
                    FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderResponse>builder()
                .success(true)
                .message("")
                .data(service.addFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderResponse> getFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родителя каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_CHANGE_PARENT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderResponse> changeParentFolder(
            @RequestBody
                    FolderChangeParentRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderResponse>builder()
                .success(true)
                .message("")
                .data(service.changeParentFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование наименования и описания каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_RENAME,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderResponse> renameFolder(
            @RequestBody
                    FolderRenameRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderResponse>builder()
                .success(true)
                .message("")
                .data(service.renameFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Folder with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Публикация отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_ADD_REPORT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderResponse> addReport(
            @RequestBody
                    FolderAddReportRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.addReport(request);

        var response = ResponseBody.<FolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(new FolderRequest((request.getFolderId()))))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_DELETE_REPORT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderResponse> deleteReport(
            @RequestBody
                    FolderAddReportRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteReport(request);

        var response = ResponseBody.<FolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(new FolderRequest((request.getFolderId()))))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск по дереву опубликованных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_SEARCH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchFolder(
            @RequestBody
                    FolderSearchRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderSearchResponse>builder()
                .success(true)
                .message("")
                .data(service.searchFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение опубликованных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_GET_PUBLISHED_REPORT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<PublishedReportResponse> getPublishedReport(
            @RequestBody
                    ReportIdRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<PublishedReportResponse>builder()
                .success(true)
                .message("")
                .data(service.getPublishedReports(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }


}
