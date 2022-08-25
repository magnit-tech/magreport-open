package ru.magnit.magreportbackend.dto.inner.dataset;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DataSetFieldView
{

    @EqualsAndHashCode.Include
    private Long id;

    private DataTypeEnum dataType;

    private String name;

    private String description;

    private Boolean valid;
}
