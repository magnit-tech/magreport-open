package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTupleValue;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleFieldData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobTupleFieldDataSFMapper implements Mapper<ReportJobTupleFieldData, SecurityFilterRoleTupleValue> {

    @Override
    public ReportJobTupleFieldData from(SecurityFilterRoleTupleValue source) {

        return new ReportJobTupleFieldData(
                source.getField().getId(),
                source.getField().getLevel(),
                source.getField().getDataSetField().getName(),
                DataTypeEnum.getTypeByOrdinal(source.getField().getDataSetField().getType().getId().intValue()),
                source.getValue()
        );
    }
}
