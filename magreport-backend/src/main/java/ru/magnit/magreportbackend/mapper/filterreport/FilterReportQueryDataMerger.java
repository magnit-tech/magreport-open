package ru.magnit.magreportbackend.mapper.filterreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.Merger2;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

@Service
@RequiredArgsConstructor
public class FilterReportQueryDataMerger implements Merger2<FilterValueListRequestData, FilterReport, ListValuesRequest> {

    private final DataSourceViewMapper dataSourceMapper;

    @Override
    public FilterValueListRequestData merge(FilterReport source1, ListValuesRequest source2) {

        var filterField = getFilterField(source1, source2.getFilterFieldId());
        var idField = getFilterFieldByType(source1, FilterFieldTypeEnum.ID_FIELD);
        var codeField = getFilterFieldByType(source1, FilterFieldTypeEnum.CODE_FIELD);
        var nameField = getFilterFieldByType(source1, FilterFieldTypeEnum.NAME_FIELD);

        return new FilterValueListRequestData(
                dataSourceMapper.from(source1.getFilterInstance().getDataSet().getDataSource()),
                source1.getFilterInstance().getDataSet().getSchemaName(),
                source1.getFilterInstance().getDataSet().getObjectName(),
                filterField.getFilterInstanceField().getDataSetField().getName(),
                DataTypeEnum.getTypeByOrdinal(filterField.getFilterInstanceField().getDataSetField().getType().getId().intValue()),
                idField.getId(),
                idField.getFilterInstanceField().getDataSetField().getName(),
                codeField.getId(),
                codeField.getFilterInstanceField().getDataSetField().getName(),
                nameField.getId(),
                nameField.getFilterInstanceField().getDataSetField().getName(),
                !source2.getIsCaseInsensitive(),
                source2.getLikenessType(),
                source2.getMaxCount(),
                source2.getSearchValue()
        );
    }

    private FilterReportField getFilterFieldByType(FilterReport source1, FilterFieldTypeEnum fieldType) {
        return source1.getFields()
                .stream()
                .filter(o -> o.getFilterInstanceField().getTemplateField().getType().getId().equals((long) fieldType.ordinal()))
                .findFirst()
                .orElseThrow(() -> new InvalidParametersException("There is no field with type:" + fieldType + " in filter with id:" + source1.getId()));
    }

    private FilterReportField getFilterField(FilterReport source1, Long filterFieldId) {
        return source1.getFields()
                .stream()
                .filter(o -> o.getId().equals(filterFieldId))
                .findFirst()
                .orElseThrow(() -> new InvalidParametersException("There is no field with id:" + filterFieldId + " in filter with id:" + source1.getId()));
    }
}
