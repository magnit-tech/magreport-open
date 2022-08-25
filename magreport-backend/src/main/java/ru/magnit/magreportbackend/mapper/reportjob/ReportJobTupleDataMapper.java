package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobTuple;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobTupleDataMapper implements Mapper<ReportJobTupleData, ReportJobTuple> {

    private final ReportJobTupleFieldDataMapper fieldDataMapper;

    @Override
    public ReportJobTupleData from(ReportJobTuple source) {
        return new ReportJobTupleData(
            fieldDataMapper.from(source.getFields()));
    }
}
