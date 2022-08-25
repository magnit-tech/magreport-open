package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.report.PivotFieldType;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.request.report.ReportFieldEditRequest;
import ru.magnit.magreportbackend.mapper.Merger;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportFieldMerger implements Merger<ReportField, ReportFieldEditRequest> {

    @Override
    public ReportField merge(ReportField target, ReportFieldEditRequest source) {
        return target
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setOrdinal(source.getOrdinal())
                .setVisible(source.getVisible())
                .setDataSetField(new DataSetField(source.getDataSetFieldId()))
                .setPivotFieldType(new PivotFieldType(source.getPivotTypeId()))
                .setModifiedDateTime(LocalDateTime.now());
    }
}
