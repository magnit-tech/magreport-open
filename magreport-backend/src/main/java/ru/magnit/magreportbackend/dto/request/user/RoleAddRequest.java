package ru.magnit.magreportbackend.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "Запрос на создание/изменение роли пользователей")
public class RoleAddRequest {

    @Schema(description = "id роли (null - будет добавлена новая)")
    private Long id;

    @Schema(description = "Тип роли")
    private Long typeId;

    @Schema(description = "Наименование роли")
    private String name;

    @Schema(description = "Описание роли")
    private String description;
}
