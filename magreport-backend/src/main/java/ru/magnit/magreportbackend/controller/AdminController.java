package ru.magnit.magreportbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.magnit.magreportbackend.service.AdminService;
import ru.magnit.magreportbackend.util.LogHelper;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Администрирование приложения")
public class AdminController {

    public static final String ADMIN_LOGS = "/api/v1/admin/logs";
    public static final String OLAP_LOGS = "/api/v1/admin/olap-logs";

    private final AdminService adminService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение текущего основного файла лога")
    @PostMapping(value = ADMIN_LOGS,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[]
    getActiveLog() {
        LogHelper.logInfoUserMethodStart();

        byte[] result = adminService.getMainActiveLog();

        LogHelper.logInfoUserMethodEnd();
        return result;
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение текущего файла лога Olap сервисов")
    @PostMapping(value = OLAP_LOGS,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getOlapActiveLog() {
        LogHelper.logInfoUserMethodStart();
        byte[] result = adminService.getOlapActiveLog();
        LogHelper.logInfoUserMethodEnd();
        return result;
    }
}
