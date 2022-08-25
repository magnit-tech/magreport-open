package ru.magnit.magreportbackend.dto.inner.reportjob;

import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record ReportJobData(

        long id,
        long reportId,
        long dataSetId,
        long dataSetTypeId,
        long userId,
        String userName,
        long statusId,
        long stateId,
        long rowCount,
        boolean isComplete,
        DataSourceData dataSource,
        ReportData reportData,
        List<ReportJobFilterData> parameters,
        List<ReportJobFilterData> securityFilterParameters
) {

    public boolean isFilterParametersExists(long filterId) {
        return parameters.stream().anyMatch(filter -> filter.filterId().equals(filterId));
    }

    public ReportJobFilterData getFilterData(long filterId) {
        return parameters.stream().filter(o->o.filterId().equals(filterId)).findFirst().orElse(null);
    }

    public List<ReportFilterData> getFiltersWithSettings(ReportFilterGroupData group) {
        return group.filters().stream().filter(filter -> isFilterParametersExists(filter.filterId())).collect(Collectors.toList());
    }

    public List<ReportFilterGroupData> getNonEmptyFilterGroups(List<ReportFilterGroupData> groups) {
        return groups.stream().filter(this::isFilterGroupNotEmpty).toList();
    }

    public boolean anyNonEmptyGroup(List<ReportFilterGroupData> groups){
        return groups.stream().anyMatch(this::isFilterGroupNotEmpty);
    }

    public boolean isFilterGroupNotEmpty(ReportFilterGroupData group) {
        if (isFiltersHaveSettings(group.filters())) return true;
        return group.groups().stream().anyMatch(this::isFilterGroupNotEmpty);
    }

    public boolean isFiltersHaveSettings(List<ReportFilterData> filters) {
        return filters.stream().anyMatch(filter -> isFilterParametersExists(filter.filterId()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportJobData that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
