package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFieldDataMapper implements Mapper<ReportFieldData, ReportField> {

    @Override
    public ReportFieldData from(ReportField source) {

        return new ReportFieldData(
                source.getId(),
                source.getOrdinal(),
                source.getVisible(),
                DataTypeEnum.getTypeByOrdinal(source.getDataSetField().getType().getId().intValue()),
                source.getDataSetField().getName(),
                source.getName(),
                source.getDescription());
    }
}
