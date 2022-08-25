package ru.magnit.magreportbackend.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Редактирование основной информации пользователя")
public class UserEditRequest {

    private Long id;
    private String description;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String email;
}
