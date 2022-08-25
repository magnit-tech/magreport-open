package ru.magnit.magreportbackend.mapper.report;

import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.report.PivotFieldType;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.request.report.ReportFieldEditRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
public class ReportFieldMapper implements Mapper<ReportField, ReportFieldEditRequest> {

    @Override
    public ReportField from(ReportFieldEditRequest source) {
        return new ReportField()
                .setOrdinal(source.getOrdinal())
                .setVisible(source.getVisible())
                .setPivotFieldType(new PivotFieldType(source.getPivotTypeId()))
                .setDataSetField(new DataSetField(source.getDataSetFieldId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
