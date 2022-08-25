package ru.magnit.magreportbackend.dto.request.securityfilter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class FieldMapping {

    private Long dataSetFieldId;
    private Long filterInstanceFieldId;
}
