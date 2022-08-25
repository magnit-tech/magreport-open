package ru.magnit.magreportbackend.mapper.report;

import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.response.report.ReportFieldMetadataResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
public class ReportFieldMetadataResponseMapper implements Mapper<ReportFieldMetadataResponse, ReportField> {
    @Override
    public ReportFieldMetadataResponse from(ReportField source) {
        return new ReportFieldMetadataResponse(
                source.getId(),
                DataTypeEnum.getTypeByOrdinal(source.getDataSetField().getType().getId().intValue()).name(),
                source.getName(),
                source.getDescription(),
                source.getOrdinal(),
                source.getVisible()
        );
    }
}
