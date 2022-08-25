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
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddFavoritesRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportAddRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportEditRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportRequest;
import ru.magnit.magreportbackend.dto.request.report.ScheduleReportRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.report.PivotFieldTypeResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportFolderResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportResponse;
import ru.magnit.magreportbackend.service.ReportService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление отчетами")
public class ReportController {

    public static final String REPORT_GET_FOLDER = "/api/v1/report/get-folder";
    public static final String REPORT_ADD_FOLDER = "/api/v1/report/add-folder";
    public static final String REPORT_FOLDER_CHANGE_PARENT = "/api/v1/report/change-parent-folder";
    public static final String REPORT_CHANGE_PARENT_FOLDER = "/api/v1/report/change-folder";
    public static final String REPORT_COPY = "/api/v1/report/copy";
    public static final String REPORT_RENAME_FOLDER = "/api/v1/report/rename-folder";
    public static final String REPORT_DELETE_FOLDER = "/api/v1/report/delete-folder";
    public static final String REPORT_ADD = "/api/v1/report/add";
    public static final String REPORT_GET = "/api/v1/report/get";
    public static final String REPORT_EDIT = "/api/v1/report/edit";
    public static final String REPORT_DELETE = "/api/v1/report/delete";
    public static final String REPORT_ADD_FAVORITES = "/api/v1/report/add-favorites";
    public static final String REPORT_GET_FAVORITES = "/api/v1/report/get-favorites";
    public static final String REPORT_DELETE_FAVORITES = "/api/v1/report/delete-favorites";
    public static final String REPORT_GET_PIVOT_TYPES = "/api/v1/report/get-pivot-types";
    public static final String REPORT_SEARCH = "/api/v1/report/search";
    public static final String REPORT_GET_SCHEDULE = "/api/v1/report/get-schedule";
    public static final String REPORT_COPY_FOLDER = "/api/v1/report/copy-folder";

    private final ReportService service;

    @Operation(summary = "Получение каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_GET_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportFolderResponse> getReportFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родителя для папки")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_FOLDER_CHANGE_PARENT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportFolderResponse> changeReportFolderParent(
            @RequestBody
                    FolderChangeParentRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.changeParentFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_CHANGE_PARENT_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> changeReportParentFolder(
            @RequestBody
                    ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.changeReportParentFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Objects successfully moved")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Копирование отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_COPY,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> copyReport(
            @RequestBody
                    ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.copyReport(request);

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
    @PostMapping(value = REPORT_ADD_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportFolderResponse> addReportFolder(
            @RequestBody
                    FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.addFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование наименования и описания каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_RENAME_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportFolderResponse> renameReportFolder(
            @RequestBody
                    FolderRenameRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.renameFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_DELETE_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteReportFolder(
            @RequestBody
                    FolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Report folder with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportResponse> addReport(
            @RequestBody
                    ReportAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportResponse>builder()
                .success(true)
                .message("")
                .data(service.addReport(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление отчета в избранное")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_ADD_FAVORITES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> addReportToFavorites(
            @RequestBody
                    ReportAddFavoritesRequest request) {

        LogHelper.logInfoUserMethodStart();
        service.addReportToFavorites(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Report with id: " + request.getReportId() + " successfully added to favorites.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка избранных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_GET_FAVORITES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderResponse> getFavReports() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFavReports())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление отчета из списка избранных отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_DELETE_FAVORITES,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteFavReports(
            @RequestBody
                    ReportIdRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteReportToFavorites(request);
        var response = ResponseBody.builder()
                .success(true)
                .message("Report with id:" + request.getId() + " successfully deleted to favorites.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteReport(
            @RequestBody
                    ReportRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteReport(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Report with id:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportResponse> getReport(
            @RequestBody
                    ReportRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportResponse>builder()
                .success(true)
                .message("")
                .data(service.getReport(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение настроек отчета по расписанию")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_GET_SCHEDULE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportResponse> getScheduleReport(
            @RequestBody
                    ScheduleReportRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportResponse>builder()
                .success(true)
                .message("")
                .data(service.getScheduleReport(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование настроек отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ReportResponse> editReport(
            @RequestBody
                    ReportEditRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ReportResponse>builder()
                .success(true)
                .message("")
                .data(service.editReport(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение справочника типов полей для сводной таблицы")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_GET_PIVOT_TYPES,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<PivotFieldTypeResponse> getPivotFieldTypes() {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<PivotFieldTypeResponse>builder()
                .success(true)
                .message("")
                .data(service.getPivotFieldTypes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Поиск по дереву отчетов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_SEARCH,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderSearchResponse> searchReport(
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

    @Operation(summary = "Копирование каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = REPORT_COPY_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ReportFolderResponse> copyReportFolder(
            @RequestBody
                    CopyFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response =  ResponseList.<ReportFolderResponse>builder()
                .success(true)
                .message("Folders successfully copied")
                .data(service.copyReportFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

}