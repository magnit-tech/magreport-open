package ru.magnit.magreportbackend.dto.request.filterreport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class FilterGroupAddRequest {

    private Long reportId;
    private Long id;
    private String name;
    private String code;
    private String description;
    private Long ordinal;
    private GroupOperationTypeEnum operationType;
    private Boolean linkedFilters;
    private Boolean mandatory;
    private List<FilterGroupAddRequest> childGroups = Collections.emptyList();
    private List<FilterAddRequest> filters = Collections.emptyList();
}
