package ru.magnit.magreportbackend.dto.response.olap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OlapConfigResponse {

    Long id;
    String name;
    String description;
    UserShortResponse user;
    String data;
    LocalDateTime created;
    LocalDateTime modified;

}
