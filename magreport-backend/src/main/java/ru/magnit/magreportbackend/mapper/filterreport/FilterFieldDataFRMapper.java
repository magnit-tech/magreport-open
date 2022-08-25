package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.dto.inner.filter.FilterFieldData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterFieldDataFRMapper implements Mapper<FilterFieldData, FilterReportField> {

    @Override
    public FilterFieldData from(FilterReportField source) {

        return new FilterFieldData(
                source.getId(),
                source.getFilterInstanceField().getLevel(),
                source.getName(),
                source.getDescription(),
                source.getFilterInstanceField().getDataSetField().getName(),
                DataTypeEnum.getTypeByOrdinal(source.getFilterInstanceField().getDataSetField().getType().getId().intValue()),
                FilterFieldTypeEnum.getByOrdinal(source.getFilterInstanceField().getTemplateField().getType().getId())
        );
    }
}
