package ru.magnit.magreportbackend.dto.inner.asm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetFieldView;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExternalAuthSourceFieldView {

    private Long id;
    private ExternalAuthSourceFieldTypeEnum type;
    private DataSetFieldView dataSetField;

}
