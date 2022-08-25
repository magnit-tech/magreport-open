package ru.magnit.magreportbackend.mapper.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.mapper.Merger3;

import java.util.List;

import static ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum.ID_FIELD;
import static ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum.NAME_FIELD;

@Service
@RequiredArgsConstructor
public class FilterChildNodesRequestDataMerger implements Merger3<FilterChildNodesRequestData, FilterData, ChildNodesRequest, List<ReportJobFilterData>> {

    @Override
    public FilterChildNodesRequestData merge(FilterData source1, ChildNodesRequest source2, List<ReportJobFilterData> source3) {

        final var rootField = source1.getRootField(source2.getPathNodes());
        long level = source2.getPathNodes().get(0).getValue() == null ? 0 : rootField.level();
        final var idField = source1.getFieldByLevelAndType(level + 1, ID_FIELD);
        final var nameField = source1.getFieldByLevelAndType(level + 1, NAME_FIELD);

        return new FilterChildNodesRequestData(
                source1.dataSource(),
                source1.schemaName(),
                source1.tableName(),
                rootField.fieldId(),
                idField.fieldId(),
                idField.fieldName(),
                nameField.fieldName(),
                level,
                source1.getPathValues(source2.getPathNodes()),
                source3
        );
    }
}
