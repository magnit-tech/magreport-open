package ru.magnit.magreportbackend.dto.inner;

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
public class UserInfo {

    private String loginName;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String email;
}
