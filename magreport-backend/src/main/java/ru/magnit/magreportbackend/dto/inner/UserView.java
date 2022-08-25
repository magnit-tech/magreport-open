package ru.magnit.magreportbackend.dto.inner;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class UserView {

    private Long id;
    private String name;
    private String description;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String email;
    private UserStatusEnum status;
}
