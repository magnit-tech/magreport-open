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
import ru.magnit.magreportbackend.dto.request.theme.ThemeAddRequest;
import ru.magnit.magreportbackend.dto.request.theme.ThemeRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.response.ResponseBody;
import ru.magnit.magreportbackend.dto.response.theme.ThemeResponse;
import ru.magnit.magreportbackend.service.ThemeService;
import ru.magnit.magreportbackend.util.LogHelper;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Управление темами оформления")
public class ThemeController {

    public static final String THEME_ADD = "/api/v1/theme/add";
    public static final String THEME_GET = "/api/v1/theme/get";
    public static final String THEME_EDIT = "/api/v1/theme/edit";
    public static final String THEME_DELETE = "/api/v1/theme/delete";
    public static final String THEME_GET_ALL = "/api/v1/theme/get-all";
    public static final String THEME_GET_ALL_USER = "/api/v1/theme/get-user-all";
    public static final String THEME_SET_FAVORITE = "/api/v1/theme/favorite";

    private final ThemeService service;

    @Operation(summary = "Добавление новой темы")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = THEME_ADD,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ThemeResponse> addTheme(
            @RequestBody
                    ThemeAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ThemeResponse>builder()
                .success(true)
                .message("")
                .data(service.addTheme(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение темы")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = THEME_GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ThemeResponse> getTheme(
            @RequestBody
                    ThemeRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ThemeResponse>builder()
                .success(true)
                .message("")
                .data(service.getTheme(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Редактирование существующей темы")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = THEME_EDIT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<ThemeResponse> editTheme(
            @RequestBody
                    ThemeAddRequest request) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<ThemeResponse>builder()
                .success(true)
                .message("")
                .data(service.editTheme(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Удаление темы")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = THEME_DELETE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> deleteTheme(
            @RequestBody
                    ThemeRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.deleteTheme(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Theme with ID:" + request.getId() + " successfully deleted.")
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Назначение темы избранной")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = THEME_SET_FAVORITE,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<Object> setFavoriteTheme(
            @RequestBody
                    ThemeRequest request) {

        LogHelper.logInfoUserMethodStart();

        service.setFavoriteTheme(request);

        var response = ResponseBody.builder()
                .success(true)
                .message("Theme with ID:" + request.getId() +" successfully add favorite" )
                .data(null)
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех тем")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = THEME_GET_ALL,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<List<ThemeResponse>> getAllThemes() {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<List<ThemeResponse>>builder()
                .success(true)
                .message("")
                .data(service.getAllThemes())
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }

    @Operation(summary = "Получение списка всех тем пользователя")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = THEME_GET_ALL_USER,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseBody<List<ThemeResponse>> getAllUserThemes(
            @RequestBody
                    UserRequest request
    ) {

        LogHelper.logInfoUserMethodStart();

        var response = ResponseBody.<List<ThemeResponse>>builder()
                .success(true)
                .message("")
                .data(service.getAllUserThemes(request))
                .build();

        LogHelper.logInfoUserMethodEnd();
        return response;
    }
    

}
