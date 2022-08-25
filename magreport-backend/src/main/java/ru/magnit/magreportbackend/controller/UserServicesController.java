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
import ru.magnit.magreportbackend.dto.request.folder.FolderPathRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.folder.FolderPathResponse;
import ru.magnit.magreportbackend.service.AuxiliaryService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Прочие сервисы для всех пользователей")
public class UserServicesController {

    public static final String FOLDER_PATH_SERVICE = "/api/v1/user-services/get-folder-path";

    private final AuxiliaryService service;

    @Operation(summary = "Проверка доступа к каталогу")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = FOLDER_PATH_SERVICE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<FolderPathResponse> getFolderPath(
            @RequestBody
                    FolderPathRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<FolderPathResponse>builder()
                .success(true)
                .message("")
                .data(service.getFolderPath(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
