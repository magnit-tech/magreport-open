package ru.magnit.magreportbackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ResponseBody<T> {
    @Schema(description = "Флаг успешности операции")
    private final Boolean success;

    @Schema(description = "Текстовое сообщение о статусе операции, описание ошибки")
    private final String message;

    @Schema(description = "Данные ответа")
    private final T data;
}
