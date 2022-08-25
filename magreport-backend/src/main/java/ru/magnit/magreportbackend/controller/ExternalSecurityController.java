package ru.magnit.magreportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityRefreshRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityResponse;
import ru.magnit.magreportbackend.service.ExternalSecurityService;
import ru.magnit.magreportbackend.util.LogHelper;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление внешними источниками прав доступа")
public class ExternalSecurityController {

    public static final String AMS_SECURITIES_ADD = "/api/v1/asm-securities/add";
    public static final String AMS_SECURITIES_EDIT = "/api/v1/asm-securities/edit";
    public static final String AMS_SECURITIES_GET = "/api/v1/asm-securities/get";
    public static final String AMS_SECURITIES_GET_ALL = "/api/v1/asm-securities/get-all";
    public static final String AMS_SECURITIES_DELETE = "/api/v1/asm-securities/delete";
    public static final String AMS_SECURITIES_REFRESH = "/api/v1/asm-securities/refresh";

    private final ExternalSecurityService service;

    @Operation(summary = "Добавление нового внешнего источника настроек доступа к отчетам")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = AMS_SECURITIES_ADD,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<AsmSecurityResponse> addAsmSecurity(
            @Parameter(name = "Параметры нового источника настроек")
            @RequestBody
                    AsmSecurityAddRequest request) {
        LogHelper.logInfoUserMethodStart();

        var result = ResponseBody.<AsmSecurityResponse>builder()
                .success(true)
                .message("")
                .data(service.addAsmSecurity(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return result;
    }

    @Operation(summary = "Редактирование внешнего источника настроек доступа к отчетам")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = AMS_SECURITIES_EDIT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<AsmSecurityResponse> editAsmSecurity(
            @Parameter(name = "Параметры нового источника настроек")
            @RequestBody
                    AsmSecurityAddRequest request) {
        LogHelper.logInfoUserMethodStart();

        var result = ResponseBody.<AsmSecurityResponse>builder()
                .success(true)
                .message("")
                .data(service.editAsmSecurity(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return result;
    }

    @Operation(summary = "Получение внешнего источника настроек доступа к отчетам")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = AMS_SECURITIES_GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<AsmSecurityResponse> getAsmSecurity(
            @Parameter(name = "Параметры нового источника настроек")
            @RequestBody
                    AsmSecurityRequest request) {
        LogHelper.logInfoUserMethodStart();

        var result = ResponseBody.<AsmSecurityResponse>builder()
                .success(true)
                .message("")
                .data(service.getAsmSecurity(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return result;
    }

    @Operation(summary = "Получение всех внешних источников настроек доступа к отчетам")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = AMS_SECURITIES_GET_ALL,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseList<AsmSecurityResponse> getAllAsmSecurity() {
        LogHelper.logInfoUserMethodStart();

        var result = ResponseList.<AsmSecurityResponse>builder()
                .success(true)
                .message("")
                .data(service.getAllAsmSecurity())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return result;
    }

    @Operation(summary = "Удаление внешнего источника настроек доступа к отчетам")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = AMS_SECURITIES_DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteAsmSecurity(
            @Parameter(name = "Параметры нового источника настроек")
            @RequestBody
                    AsmSecurityRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.deleteAsmSecurity(request);

        var result = ResponseBody.builder()
                .success(true)
                .message("External security source successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return result;
    }

    @Operation(summary = "Запуск обновления секьюрити фильтров AMS")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = AMS_SECURITIES_REFRESH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<Object> refreshAmsFilters(
            @Parameter(name = "Список настроек для обновления")
            @RequestBody
                    AsmSecurityRefreshRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.refreshAmsFilters(request.getIdList());

        ResponseBody<Object> result = ResponseBody.builder()
                .success(true)
                .message("")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return result;
    }
}
