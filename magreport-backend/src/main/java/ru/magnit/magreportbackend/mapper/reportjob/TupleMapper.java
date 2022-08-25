package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTuple;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class TupleMapper implements Mapper<Tuple, ReportJobTuple> {

    private final TupleValueMapper tupleValueMapper;

    @Override
    public Tuple from(ReportJobTuple source) {
        return new Tuple().setValues(tupleValueMapper.from(source.getFields()));
    }
}
