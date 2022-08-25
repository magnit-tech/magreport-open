package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.dto.inner.filter.FilterFieldData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterFieldDataFIMapper implements Mapper<FilterFieldData, FilterInstanceField> {

    @Override
    public FilterFieldData from(FilterInstanceField source) {

        return new FilterFieldData(
                source.getId(),
                source.getLevel(),
                source.getName(),
                source.getDescription(),
                source.getDataSetField() == null? null :source.getDataSetField().getName(),
                source.getDataSetField() == null? null : DataTypeEnum.getTypeByOrdinal(source.getDataSetField().getType().getId().intValue()),
                FilterFieldTypeEnum.getByOrdinal(source.getTemplateField().getType().getId())
        );
    }
}
