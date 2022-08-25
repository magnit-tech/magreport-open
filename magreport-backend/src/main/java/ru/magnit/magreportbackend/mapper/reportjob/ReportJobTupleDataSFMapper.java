package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTuple;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleData;
import ru.magnit.magreportbackend.mapper.Mapper;


@Service
@RequiredArgsConstructor
public class ReportJobTupleDataSFMapper implements Mapper<ReportJobTupleData, SecurityFilterRoleTuple> {

    private final ReportJobTupleFieldDataSFMapper fieldDataSFMapper;

    @Override
    public ReportJobTupleData from(SecurityFilterRoleTuple source) {

        return new ReportJobTupleData(
                fieldDataSFMapper.from(source.getTupleValues())
        );
    }
}
