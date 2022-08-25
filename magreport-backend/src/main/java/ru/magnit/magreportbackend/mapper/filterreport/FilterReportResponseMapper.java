package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterReportResponseMapper implements Mapper<FilterReportResponse, FilterReport> {

    private final FilterReportFieldResponseMapper fieldMapper;

    @Override
    public FilterReportResponse from(FilterReport source) {
        return new FilterReportResponse(
                source.getId(),
                source.getFilterInstance().getId(),
                FilterTypeEnum.getByOrdinal(source.getFilterInstance().getFilterTemplate().getType().getId()),
                source.isHidden(),
                source.isMandatory(),
                source.isRootSelectable(),
                source.getName(),
                source.getCode(),
                source.getDescription(),
                source.getOrdinal().intValue(),
                fieldMapper.from(source.getFields()),
                source.getUser().getName(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
