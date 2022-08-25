package ru.magnit.magreportbackend.dto.request.filtertemplate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class FilterTemplateFieldAddRequest {

    private Long id;
    private FilterFieldTypeEnum type;
    private Long level;
    private Long linkedId;
    private String name;
    private String description;
}
