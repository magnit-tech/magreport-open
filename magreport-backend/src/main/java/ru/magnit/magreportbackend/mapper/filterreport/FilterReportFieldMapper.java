package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterFieldAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterReportFieldMapper implements Mapper<FilterReportField, FilterFieldAddRequest> {

    @Override
    public FilterReportField from(FilterFieldAddRequest source) {

        return new FilterReportField()
                .setName(source.getName())
                .setOrdinal(source.getOrdinal())
                .setDescription(source.getDescription())
                .setReportField(source.getReportFieldId() == null ? null : new ReportField(source.getReportFieldId()))
                .setFilterInstanceField(new FilterInstanceField(source.getFilterInstanceFieldId()))
                .setExpand(source.getExpand() != null && source.getExpand());
    }
}
