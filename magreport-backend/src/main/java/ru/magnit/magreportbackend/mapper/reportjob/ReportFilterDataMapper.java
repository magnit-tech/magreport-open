package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterLevelData;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum.CODE_FIELD;
import static ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum.ID_FIELD;

@Service
@RequiredArgsConstructor
public class ReportFilterDataMapper implements Mapper<ReportFilterData, FilterReport> {

    private final DataSourceViewMapper dataSourceMapper;

    @Override
    public ReportFilterData from(FilterReport source) {

        Long filterTypeId =   source.getFilterInstance().getFilterTemplate().getType().getId();

      return new ReportFilterData(
                source.getId(),
                source.getFilterInstance().getDataSet() == null ? null : dataSourceMapper.from(source.getFilterInstance().getDataSet().getDataSource()),
                source.getFilterInstance().getDataSet() == null ? null : source.getFilterInstance().getDataSet().getId(),
                source.getCode(),
                source.getFilterInstance().getDataSet() == null ? null : source.getFilterInstance().getDataSet().getSchemaName(),
                source.getFilterInstance().getDataSet() == null ? null : source.getFilterInstance().getDataSet().getObjectName(),
                getLevels(source.getFields(), filterTypeId)
        );
    }

    private List<ReportFilterLevelData> getLevels(List<FilterReportField> fields, Long filterTypeId) {

        var levels = fields.stream().map(field -> field.getFilterInstanceField().getLevel()).collect(Collectors.toSet());

        return levels
                .stream()
                .map(level -> new ReportFilterLevelData(
                        fields.stream().filter(field -> field.getFilterInstanceField().getLevel().equals(level)).filter(field -> FilterFieldTypeEnum.getByOrdinal(field.getFilterInstanceField().getTemplateField().getType().getId()) == ID_FIELD).map(BaseEntity::getId).findFirst().orElse(-1L),
                        fields.stream().filter(field -> field.getFilterInstanceField().getLevel().equals(level)).filter(field -> FilterFieldTypeEnum.getByOrdinal(field.getFilterInstanceField().getTemplateField().getType().getId()) == CODE_FIELD).map(BaseEntity::getId).findFirst().orElse(-1L),
                        fields.stream().filter(field -> field.getFilterInstanceField().getLevel().equals(level)).filter(field -> FilterFieldTypeEnum.getByOrdinal(field.getFilterInstanceField().getTemplateField().getType().getId()) == ID_FIELD).filter(field -> field.getReportField() != null).map(field -> field.getReportField().getDataSetField().getType().getEnum()).findFirst().orElse(null),
                        fields.stream().filter(field -> field.getFilterInstanceField().getLevel().equals(level)).filter(field -> FilterFieldTypeEnum.getByOrdinal(field.getFilterInstanceField().getTemplateField().getType().getId()) == ID_FIELD).filter(field -> field.getReportField() != null).map(field -> field.getReportField().getDataSetField().getName()).findFirst().orElse(null),
                        fields.stream().filter(field -> field.getFilterInstanceField().getLevel().equals(level)).filter(field -> FilterFieldTypeEnum.getByOrdinal(field.getFilterInstanceField().getTemplateField().getType().getId()) == ID_FIELD).filter(field -> field.getFilterInstanceField().getDataSetField() != null).map(field -> field.getFilterInstanceField().getDataSetField().getName()).findFirst().orElse(null),
                        fields.stream().filter(field -> field.getFilterInstanceField().getLevel().equals(level)).filter(field -> FilterFieldTypeEnum.getByOrdinal(field.getFilterInstanceField().getTemplateField().getType().getId()) == CODE_FIELD).filter(field -> field.getFilterInstanceField().getDataSetField() != null).map(field -> field.getFilterInstanceField().getDataSetField().getName()).findFirst().orElse(null),
                        fields.stream().filter(field -> field.getFilterInstanceField().getLevel().equals(level)).filter(field -> FilterFieldTypeEnum.getByOrdinal(field.getFilterInstanceField().getTemplateField().getType().getId()) == ID_FIELD).filter(field -> field.getFilterInstanceField() != null).filter(field -> field.getFilterInstanceField().getDataSetField() != null).map(field -> field.getFilterInstanceField().getDataSetField().getType().getEnum()).findFirst().orElse(FilterTypeEnum.getDataTypeFilter(filterTypeId))))
                .collect(Collectors.toList());
    }
}