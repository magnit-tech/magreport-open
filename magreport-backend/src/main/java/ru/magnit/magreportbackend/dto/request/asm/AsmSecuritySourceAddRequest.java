package ru.magnit.magreportbackend.dto.request.asm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AsmSecuritySourceAddRequest {

    private Long id;
    private Long dataSetId;
    private String name;
    private String description;
    private ExternalAuthSourceTypeEnum sourceType;
    private String preSql;
    private String postSql;
    private List<AsmSecuritySecurityFilterAddRequest> securityFilters = Collections.emptyList();
    private List<AsmSecurityDataSetFieldMapRequest> fields = Collections.emptyList();
}
