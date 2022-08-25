package ru.magnit.magreportbackend.dto.response.serversettings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.serversettings.ServerMailTemplateTypeEnum;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class ServerMailTemplateResponse {

    Long id;
    ServerMailTemplateTypeEnum type;
    String code;
    String name;
    String description;
    String subject;
    String body;
    UserResponse user;
    LocalDateTime created;
    LocalDateTime modified;

}
