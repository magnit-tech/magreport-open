package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTupleField;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class TupleValueMapper implements Mapper<TupleValue, ReportJobTupleField> {

    @Override
    public TupleValue from(ReportJobTupleField source) {

        return new TupleValue()
                .setFieldId(source.getFilterReportField().getId())
                .setValue(source.getValue());
    }
}
