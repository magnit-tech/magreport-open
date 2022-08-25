package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTuple;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobTupleMapper implements Mapper<ReportJobTuple, Tuple> {

    private final ReportJobTupleFieldMapper tupleFieldMapper;

    @Override
    public ReportJobTuple from(Tuple source) {
        final var tuple = new ReportJobTuple()
            .setFields(tupleFieldMapper.from(source.getValues()));
        tuple.getFields().forEach(field -> field.setReportJobTuple(tuple));

        return tuple;
    }
}