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
import ru.magnit.magreportbackend.dto.request.serversettings.ServerMailTemplateEditRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerMailTemplateRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerMailTemplateTypeRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateResponse;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerMailTemplateTypeResponse;
import ru.magnit.magreportbackend.service.ServerMailTemplateService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление системными шаблонами писем")
public class ServerMailTemplateController {

    public static final String SERVER_MAIL_TEMPLATE_GET = "/api/v1/server-mail-template/get";
    public static final String SERVER_MAIL_TEMPLATE_EDIT = "/api/v1/server-mail-template/edit";
    public static final String SERVER_MAIL_TEMPLATE_GET_ALL = "/api/v1/server-mail-template/get-all";
    public static final String SERVER_MAIL_TEMPLATE_GET_TYPE = "/api/v1/server-mail-template/get-by-type";
    public static final String SERVER_MAIL_TEMPLATE_TYPE_GET = "/api/v1/server-mail-template/type-get";
    public static final String SERVER_MAIL_TEMPLATE_TYPE_GET_ALL = "/api/v1/server-mail-template/type-get-all";

    private final ServerMailTemplateService mailTemplateService;

    @Operation(summary = "Получение шаблона письма")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_MAIL_TEMPLATE_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ServerMailTemplateResponse> getServerMailTemplate(
            @RequestBody
                    ServerMailTemplateRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ServerMailTemplateResponse>builder()
                .success(true)
                .message("")
                .data(mailTemplateService.getServerMailTemplate(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех шаблонов писем")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_MAIL_TEMPLATE_GET_ALL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ServerMailTemplateResponse> getAllServerMailTemplate() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ServerMailTemplateResponse>builder()
                .success(true)
                .message("")
                .data(mailTemplateService.getAllServerMailTemplate())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка шаблонов писем по типу")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_MAIL_TEMPLATE_GET_TYPE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ServerMailTemplateResponse> getServerMailTemplateByType(
            @RequestBody
                    ServerMailTemplateTypeRequest request
    ) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ServerMailTemplateResponse>builder()
                .success(true)
                .message("")
                .data(mailTemplateService.getServerMailTemplateByType(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование шаблона письма ")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_MAIL_TEMPLATE_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ServerMailTemplateResponse> editServerMailTemplate(
            @RequestBody
                    ServerMailTemplateEditRequest request
    ) {

        LogHelper.logInfoUserMethodStart();

        mailTemplateService.editServerMailTemplate(request);

        var response = ResponseList.<ServerMailTemplateResponse>builder()
                .success(true)
                .message("Шаблон письма успешно изменен")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }


    @Operation(summary = "Получение списка типов шаблонов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_MAIL_TEMPLATE_TYPE_GET_ALL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ServerMailTemplateTypeResponse> getAllServerMailTemplateType() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ServerMailTemplateTypeResponse>builder()
                .success(true)
                .message("")
                .data( mailTemplateService.getAllServerMailTemplateType())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение типов шаблонов")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_MAIL_TEMPLATE_TYPE_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ServerMailTemplateTypeResponse> getServerMailTemplateType(
            @RequestBody
                    ServerMailTemplateTypeRequest request
    ) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ServerMailTemplateTypeResponse>builder()
                .success(true)
                .message("")
                .data( mailTemplateService.getServerMailTemplateType(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
