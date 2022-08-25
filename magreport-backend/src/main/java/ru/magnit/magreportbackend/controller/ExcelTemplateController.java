package ru.magnit.magreportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateDeleteRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateGetRequest;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateSetDefaultRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.service.ExcelTemplateService;
import ru.magnit.magreportbackend.util.LogHelper;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление шаблонами выгрузки в формате Excel")
public class ExcelTemplateController {

    public static final String EXCEL_TEMPLATE_ADD_FOLDER = "/api/v1/excel-template/add-folder";
    public static final String EXCEL_TEMPLATE_GET_FOLDER = "/api/v1/excel-template/get-folder";
    public static final String EXCEL_TEMPLATE_RENAME_FOLDER = "/api/v1/excel-template/rename-folder";
    public static final String EXCEL_TEMPLATE_DELETE_FOLDER = "/api/v1/excel-template/delete-folder";
    public static final String EXCEL_TEMPLATE_FOLDER_CHANGE_PARENT = "/api/v1/excel-template/change-parent-folder";
    public static final String EXCEL_TEMPLATE_CHANGE_PARENT_FOLDER = "/api/v1/excel-template/change-folder";
    public static final String EXCEL_TEMPLATE_ADD = "/api/v1/excel-template/add";
    public static final String EXCEL_TEMPLATE_GET_FILE = "/api/v1/excel-template/get-file";
    public static final String EXCEL_TEMPLATE_DELETE = "/api/v1/excel-template/delete";
    public static final String EXCEL_TEMPLATE_SET_DEFAULT = "/api/v1/excel-template/set-default";
    public static final String EXCEL_TEMPLATE_GET_TO_REPORT= "/api/v1/excel-template/get-to-report";
    public static final String EXCEL_TEMPLATE_ADD_TO_REPORT= "/api/v1/excel-template/add-to-report";

    public static final String APPLICATION_EXCEL_XLSM_FILE = "application/vnd.ms-excel.sheet.macroEnabled.12";

    private final ExcelTemplateService service;

    @Operation(summary = "Добавление нового каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_ADD_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ExcelTemplateFolderResponse> addExcelTemplateFolder(
            @RequestBody
                    FolderAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ExcelTemplateFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.addFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение каталога по id")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_GET_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ExcelTemplateFolderResponse> getExcelTemplateFolder(
            @RequestBody
                    FolderRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ExcelTemplateFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родителя каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_FOLDER_CHANGE_PARENT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ExcelTemplateFolderResponse> changeExcelTemplateFolderParent(
            @RequestBody
                    FolderChangeParentRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ExcelTemplateFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.changeParentFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Смена родительского каталога шаблона отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_CHANGE_PARENT_FOLDER,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> changeExcelTemplateParentFolder(
        @RequestBody
            ChangeParentFolderRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.changeExcelTemplateParentFolder(request);

        var response = ResponseBody.builder()
            .success(true)
            .message("Objects successfully moved")
            .data(null)
            .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Переименование каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_RENAME_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ExcelTemplateFolderResponse> renameExcelTemplateFolder(
            @RequestBody
                    FolderRenameRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ExcelTemplateFolderResponse>builder()
                .success(true)
                .message("")
                .data(service.renameFolder(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление каталога")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_DELETE_FOLDER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteExcelTemplateFolder(
            @RequestBody
                    FolderRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteFolder(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Добавление нового шаблона")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_ADD,
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ExcelTemplateResponse> addExcelTemplate(
            @Parameter(name = "Параметры добавляемого шаблона")
            @RequestParam("params")
                    String excelTemplateAddRequest,
            @Parameter(name = "Файл Excel шаблона")
            @RequestParam("file")
                    MultipartFile uploadedFile
    ) {


        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ExcelTemplateResponse>builder()
                .success(true)
                .message("")
                .data(service.addExcelTemplate(excelTemplateAddRequest, uploadedFile))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение файла шаблона")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_GET_FILE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_EXCEL_XLSM_FILE)
    public @org.springframework.web.bind.annotation.ResponseBody byte[]
        getExcelTemplateFile(@RequestBody ExcelTemplateGetRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = service.getExcelTemplateFile(request);

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление шаблона")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteExcelTemplate(
            @RequestBody
                    ExcelTemplateDeleteRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteExcelTemplate(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Назначение шаблона по умолчанию отчету")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_SET_DEFAULT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> setDefaultExcelTemplateToReport(
            @RequestBody
                    ExcelTemplateSetDefaultRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.setDefaultExcelTemplateToReport(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех шаблонов отчета")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_GET_TO_REPORT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<List<ReportExcelTemplateResponse>> getExcelTemplatesToReport(
            @RequestBody
                    ReportIdRequest request) {

        LogHelper.logInfoUserMethodStart();



        var response = ResponseBody.<List<ReportExcelTemplateResponse>>builder()
                .success(true)
                .message("")
                .data(service.getAllReportExcelTemplateToReport(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Привязка шаблона к отчету")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EXCEL_TEMPLATE_ADD_TO_REPORT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> addExcelTemplateToReport(
            @RequestBody
                    ExcelTemplateSetDefaultRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.addExcelTemplateToReport(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
