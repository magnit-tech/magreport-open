package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class ReportFieldCloner implements Cloner<ReportField> {
    @Override
    public ReportField clone(ReportField source) {
        return new ReportField()
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setDataSetField(source.getDataSetField())
            .setOrdinal(source.getOrdinal())
            .setVisible(source.getVisible())
            .setPivotFieldType(source.getPivotFieldType());
    }
}
