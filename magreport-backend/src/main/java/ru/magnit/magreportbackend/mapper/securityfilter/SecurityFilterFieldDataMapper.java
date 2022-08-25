package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;
import ru.magnit.magreportbackend.dto.inner.filter.FilterFieldData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterFieldDataMapper implements Mapper<FilterFieldData, SecurityFilterDataSetField> {
    @Override
    public FilterFieldData from(SecurityFilterDataSetField source) {
        return new FilterFieldData(
                source.getFilterInstanceField().getId(),
                source.getFilterInstanceField().getLevel(),
                source.getFilterInstanceField().getName(),
                source.getFilterInstanceField().getDescription(),
                source.getFilterInstanceField().getDataSetField().getName(),
                DataTypeEnum.getTypeByOrdinal(source.getFilterInstanceField().getDataSetField().getType().getId().intValue()),
                FilterFieldTypeEnum.getByOrdinal(source.getFilterInstanceField().getTemplateField().getType().getId())
        );
    }
}
