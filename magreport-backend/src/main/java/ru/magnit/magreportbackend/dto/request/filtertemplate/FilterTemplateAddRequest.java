package ru.magnit.magreportbackend.dto.request.filtertemplate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class FilterTemplateAddRequest {

    private Long id;
    private Long folderId;
    private FilterTypeEnum type;
    private String name;
    private String description;
    private List<FilterTemplateFieldAddRequest> fields = Collections.emptyList();
    private List<FilterOperationTypeEnum> supportedOperations = Collections.emptyList();
}
