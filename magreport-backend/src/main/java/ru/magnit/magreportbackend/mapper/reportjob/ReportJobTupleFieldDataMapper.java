package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTupleField;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleFieldData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobTupleFieldDataMapper implements Mapper<ReportJobTupleFieldData, ReportJobTupleField> {

    @Override
    public ReportJobTupleFieldData from(ReportJobTupleField source) {

        return new ReportJobTupleFieldData(
                source.getFilterReportField().getId(),
                source.getFilterReportField().getFilterInstanceField().getLevel(),
                null,
                null,
                source.getValue()
        );
    }
}
