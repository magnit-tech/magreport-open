package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterReportFieldResponseMapper implements Mapper<FilterReportFieldResponse, FilterReportField> {

    @Override
    public FilterReportFieldResponse from(FilterReportField source) {

        return new FilterReportFieldResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                FilterFieldTypeEnum.getByOrdinal(source.getFilterInstanceField().getTemplateField().getType().getId()),
                source.getFilterInstanceField().getLevel(),
                source.getOrdinal(),
                source.getFilterInstanceField().getId(),
                source.getReportField() == null ? null : source.getReportField().getId(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime(),
                source.isExpand());
    }
}
