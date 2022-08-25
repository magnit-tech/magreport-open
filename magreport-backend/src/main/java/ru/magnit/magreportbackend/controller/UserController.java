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
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.request.user.UserStatusSetRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.ResponseList;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.UserService;
import ru.magnit.magreportbackend.util.LogHelper;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление пользовательскими учетными записями")
public class UserController {

    public static final String USERS_GET = "/api/v1/users";
    public static final String USERS_LOGOFF = "/api/v1/users/logoff";
    public static final String USERS_LOGOFF_ALL = "/api/v1/users/logoff-all";
    public static final String USERS_GET_ALL_STATUSES = "/api/v1/users/get-all-statuses";
    public static final String USERS_SET_STATUS = "/api/v1/users/set-status";
    public static final String USERS_GET_ONE = "/api/v1/users/get-one";
    public static final String USERS_WHO_AM_I = "/api/v1/users/who-am-i";
    public static final String USERS_GET_BY_ROLE = "/api/v1/users/get-by-role";
    public static final String USERS_GET_ACTUAL = "/api/v1/users/actual";

    private final UserService service;

    @Operation(summary = "Получение списка всех пользователей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<UserResponse> getAllUsers() {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<UserResponse>builder()
                .success(true)
                .message("")
                .data(service.getAllUsers())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "LogOff списка пользователей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_LOGOFF,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> logOffUsers(
            @RequestBody(required = false)
                    List<String> userNames    ) {
        LogHelper.logInfoUserMethodStart();

        service.logoffUsers(userNames);

        var response = ResponseBody.builder()
                .success(true)
                .message("Users successfully logged off.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "LogOff всех пользователей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_LOGOFF_ALL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> logOffAllUsers() {
        LogHelper.logInfoUserMethodStart();

        service.logoffAllUsers();

        var response = ResponseBody.builder()
                .success(true)
                .message("All users successfully logged off.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех статусов пользователей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_GET_ALL_STATUSES,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<String> getAllStatuses() {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<String>builder()
                .success(true)
                .message("")
                .data(service.getAllStatuses())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Установка статуса пользователю")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_SET_STATUS,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> setStatus(
            @RequestBody
                    UserStatusSetRequest request) {
        LogHelper.logInfoUserMethodStart();

        service.setUserStatus(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Status successfully changed")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение информации о пользователе")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_GET_ONE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<UserResponse> getUserInfo(
            @RequestBody
                    UserRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<UserResponse>builder()
                .success(true)
                .message("")
                .data(service.getUserResponse(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение информации о себе")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_WHO_AM_I,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<UserResponse> getWhoAmI() {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<UserResponse>builder()
                .success(true)
                .message("")
                .data(service.getUserResponse(new UserRequest().setUserName(service.getCurrentUserName())))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка пользователей по роли")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_GET_BY_ROLE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<UserResponse> getAllUsersByRole(@RequestBody RoleRequest request) {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<UserResponse>builder()
                .success(true)
                .message("")
                .data(service.getUsersByRole(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех актульных пользователей")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = USERS_GET_ACTUAL,
            produces = APPLICATION_JSON_VALUE)
    public ResponseList<UserResponse> getAllActualUsers() {
        LogHelper.logInfoUserMethodStart();

        var response = ResponseList.<UserResponse>builder()
                .success(true)
                .message("")
                .data(service.getAllActualUsers())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
}
