package ru.magnit.magreportbackend.dto.response.filtertemplate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class FilterTemplateFieldResponse {

    private Long id;
    private FilterFieldTypeEnum type;
    private Long level;
    private Long linkedId;
    private String name;
    private String description;
    private LocalDateTime created;
    private LocalDateTime modified;
}
