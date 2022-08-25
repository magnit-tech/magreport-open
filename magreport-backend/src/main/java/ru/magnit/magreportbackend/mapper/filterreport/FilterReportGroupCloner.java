package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class FilterReportGroupCloner implements Cloner<FilterReportGroup> {

    private final FilterReportCloner filterReportCloner;

    @Override
    public FilterReportGroup clone(FilterReportGroup source) {
        return clone(source, null);
    }

    @Override
    public FilterReportGroup clone(FilterReportGroup source, Object context) {
        final var filterReportGroup = new FilterReportGroup()
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setType(source.getType())
                .setCode(source.getCode())
                .setOrdinal(source.getOrdinal())
                .setMandatory(source.getMandatory())
                .setLinkedFilters(source.getLinkedFilters());

        final var childGroups = clone(source.getChildGroups(), context);
        childGroups.forEach(childGroup -> childGroup.setParentGroup(filterReportGroup));
        filterReportGroup.setChildGroups(childGroups);

        final var childFilters = filterReportCloner.clone(source.getFilterReports(), context);
        childFilters.forEach(filter -> filter.setGroup(filterReportGroup));
        filterReportGroup.setFilterReports(childFilters);

        return filterReportGroup;
    }
}
