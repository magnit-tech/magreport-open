package ru.magnit.magreportbackend.dto.inner.asm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ExternalAuthSecurityFilterView {

    private Long id;
    private Long securityFilterId;
    private DataSetView dataSet;
    private FilterData filterInstance;
}
