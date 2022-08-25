package ru.magnit.magreportbackend.dto.request.theme;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class ThemeAddRequest {

    Long id;
    String name;
    String description;
    Long typeId;
    Long userId;
    Boolean favorite;
    Object data;


}
