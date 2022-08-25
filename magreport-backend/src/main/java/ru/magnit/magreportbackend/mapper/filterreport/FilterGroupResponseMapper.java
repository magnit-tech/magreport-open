package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterGroupResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterGroupResponseMapper implements Mapper<FilterGroupResponse, FilterReportGroup> {

    private final FilterReportResponseMapper filterResponseMapper;

    @Override
    public FilterGroupResponse from(FilterReportGroup source) {
        return source == null ? new FilterGroupResponse() : getFilterGroupResponse(source, filterResponseMapper);
    }

    private FilterGroupResponse getFilterGroupResponse(FilterReportGroup source, FilterReportResponseMapper filterResponseMapper) {
        return new FilterGroupResponse(
                source.getId(),
                source.getReport().getId(),
                source.getCode(),
                source.getName(),
                source.getDescription(),
                source.getOrdinal(),
                source.getLinkedFilters(),
                source.getMandatory() != null && source.getMandatory(),
                source.getTypeEnum(),
                from(getSortedFilterGroups(source.getChildGroups())),
                filterResponseMapper.from(getSortedFilterReports(source.getFilterReports())),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }

    private List<FilterReportGroup> getSortedFilterGroups(List<FilterReportGroup> source) {
        return source.stream().sorted(Comparator.comparingLong(FilterReportGroup::getOrdinal)).collect(Collectors.toList());
    }

    private List<FilterReport> getSortedFilterReports(List<FilterReport> source) {
        return source.stream().sorted(Comparator.comparingLong(FilterReport::getOrdinal)).collect(Collectors.toList());
    }
}
