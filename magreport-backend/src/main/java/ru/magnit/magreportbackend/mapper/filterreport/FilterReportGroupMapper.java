package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationType;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterReportGroupMapper implements Mapper<FilterReportGroup, FilterGroupAddRequest> {

    private final FilterReportMapper filterReportMapper;

    @Override
    public FilterReportGroup from(FilterGroupAddRequest source) {
        var reportGroup = new FilterReportGroup()
                .setName(source.getName())
                .setCode(source.getCode())
                .setDescription(source.getDescription())
                .setLinkedFilters(source.getLinkedFilters())
                .setMandatory(source.getMandatory())
                .setOrdinal(source.getOrdinal())
                .setType(new GroupOperationType(source.getOperationType()))
                .setReport(new Report(source.getReportId()))
                .setChildGroups(from(source.getChildGroups()))
                .setFilterReports(filterReportMapper.from(source.getFilters()));
        reportGroup.getChildGroups().forEach(group -> group.setParentGroup(reportGroup));
        reportGroup.getFilterReports().forEach(report -> report.setGroup(reportGroup));

        return reportGroup;
    }
}
