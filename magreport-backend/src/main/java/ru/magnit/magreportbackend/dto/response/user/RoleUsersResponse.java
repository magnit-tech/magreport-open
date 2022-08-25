package ru.magnit.magreportbackend.dto.response.user;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Tag(name = "Информация о роли пользователя")
public class RoleUsersResponse {

    @Schema(description ="id роли")
    private Long id;

    @Schema(description ="Пользователи, имеющие роль")
    private List<UserResponse> users;

    public boolean isEmpty() {
        return users.isEmpty();
    }
}
