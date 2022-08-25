package ru.magnit.magreportbackend.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Запрос на добавление пользователям роли")
public class UserStatusSetRequest {

    private List<String> userNames;
    private UserStatusEnum status;
}