package ru.magnit.magreportbackend.dto.request.securityfilter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class SecurityFilterAddRequest {

    private Long id;

    private String name;

    private String description;

    private Long filterInstanceId;

    private FilterOperationTypeEnum operationType;

    private Long folderId;

    private List<SecurityFilterDataSetAddRequest> dataSets;
}
