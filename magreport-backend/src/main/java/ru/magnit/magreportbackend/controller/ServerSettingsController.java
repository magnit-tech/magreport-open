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
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingSetRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingsJournalRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerSettingsJournalResponse;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerSettingsResponse;
import ru.magnit.magreportbackend.service.SettingsService;
import ru.magnit.magreportbackend.util.LogHelper;

import java.time.OffsetDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление настройками сервера")
public class ServerSettingsController {

    public static final String SERVER_SETTINGS_GET = "/api/v1/server-settings/get";
    public static final String SERVER_SETTINGS_SET = "/api/v1/server-settings/set";
    public static final String SERVER_SETTINGS_JOURNAL = "/api/v1/server-settings/get-journal";
    public static final String SERVER_SETTINGS_GET_DATE_TIME = "/api/v1/server-settings/get-date-time";

    private final SettingsService service;

    @Operation(summary = "Получение текущих настроек")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_SETTINGS_GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ServerSettingsResponse> getServerSettings() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ServerSettingsResponse>builder()
                .success(true)
                .message("")
                .data(service.getSettings())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка нового значения для настройки")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_SETTINGS_SET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> setServerSetting(
            @RequestBody
                    ServerSettingSetRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.setSetting(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Parameter with id: " + request.getSettingId() + " successfully set.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение текущих настроек")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_SETTINGS_JOURNAL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<ServerSettingsJournalResponse> getServerSettingsJournal(
            @RequestBody
                    ServerSettingsJournalRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<ServerSettingsJournalResponse>builder()
                .success(true)
                .message("")
                .data(service.getJournal(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение текущих даты и времени сервера со смещением от UTC в формате ISO 8601 YYYY-MM-DDThh:mm:ss[.S]±hh:mm")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = SERVER_SETTINGS_GET_DATE_TIME)
    public ResponseBody<String> getServerDateTime() {

        LogHelper.logInfoUserMethodStart();
        var response = ResponseBody.<String>builder()
                .success(true)
                .message("")
                .data(OffsetDateTime.now().toString())
                .build();
        LogHelper.logInfoUserMethodEnd();
        return response;
    }


}
