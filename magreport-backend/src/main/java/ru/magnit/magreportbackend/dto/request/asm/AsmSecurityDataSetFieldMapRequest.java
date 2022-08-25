package ru.magnit.magreportbackend.dto.request.asm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AsmSecurityDataSetFieldMapRequest {

    private Long id;
    private Long dataSetFieldId;
    private ExternalAuthSourceFieldTypeEnum fieldType;
    private List<AsmSecurityFilterInstanceFieldAddRequest> filterInstanceFields = Collections.emptyList();
}
