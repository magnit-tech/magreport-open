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
import ru.magnit.magreportbackend.dto.request.mail.EmailSendRequest;
import ru.magnit.magreportbackend.dto.request.mail.ListEmailsCheckRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.mail.ListEmailsCheckResponse;
import ru.magnit.magreportbackend.service.EmailService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление отправкой электронных писем")
public class EmailController {

    public static final String EMAIL_SEND = "/api/v1/email/send";
    public static final String EMAIL_CHECK = "/api/v1/email/check";

    private final EmailService service;

    @Operation(summary = "Отправка письма ")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EMAIL_SEND,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> sendMail(
            @RequestBody EmailSendRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.sendMail(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Проверка получателей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = EMAIL_CHECK,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ListEmailsCheckResponse> checkEmails(
            @RequestBody ListEmailsCheckRequest request) {
        LogHelper.logInfoUserMethodStart();

      var result =   service.checkEmails(request);

        var response = ResponseBody.<ListEmailsCheckResponse>builder()
                .success(true)
                .message("")
                .data(result)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

}
