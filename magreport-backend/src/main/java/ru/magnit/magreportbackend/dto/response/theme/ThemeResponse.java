package ru.magnit.magreportbackend.dto.response.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.theme.ThemeTypeEnum;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ThemeResponse {

    Long id;
    String name;
    String description;
    ThemeTypeEnum type;
    UserShortResponse user;
    Boolean favorites;
    String data;
    LocalDateTime created;
    LocalDateTime modified;


}
