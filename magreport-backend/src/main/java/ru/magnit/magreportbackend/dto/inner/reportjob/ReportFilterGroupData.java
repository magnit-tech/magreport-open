package ru.magnit.magreportbackend.dto.inner.reportjob;

import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public record ReportFilterGroupData(
        Long id,
        Long parentId,
        String code,
        String parentCode,
        GroupOperationTypeEnum operationType,
        List<ReportFilterData> filters,
        List<ReportFilterGroupData> groups
) {

    public List<ReportFilterData> getAllFilters() {
        var result = new LinkedList<>(filters);
        if (groups.isEmpty()) return result;
        result.addAll(groups.stream().flatMap(group -> group.getAllFilters().stream()).collect(Collectors.toList()));
        return result;
    }
}
