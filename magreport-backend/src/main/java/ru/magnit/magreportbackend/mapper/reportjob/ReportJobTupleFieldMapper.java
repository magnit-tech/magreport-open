package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportField;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTupleField;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobTupleFieldMapper implements Mapper<ReportJobTupleField, TupleValue> {

    @Override
    public ReportJobTupleField from(TupleValue source) {

        return new ReportJobTupleField()
                .setFilterReportField(new FilterReportField(source.getFieldId()))
                .setValue(source.getValue().replace("'", ""));
    }
}
