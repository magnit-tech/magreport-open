package ru.magnit.magreportbackend.dto.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Tag(name = "Информация о роли пользователя")
public class RoleResponse {

    @Schema(description ="id роли")
    private Long id;

    @Schema(description ="Тип роли")
    private Long typeId;

    @Schema(description ="Наименование роли")
    private String name;

    @Schema(description ="Описание роли")
    private String description;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время создания роли")
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    @Schema(description ="Дата и время последнего изменения роли")
    private LocalDateTime modified;

}
