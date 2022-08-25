package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.response.report.ReportFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportFieldResponseMapper implements Mapper<ReportFieldResponse, ReportField> {

    @Override
    public ReportFieldResponse from(ReportField source) {

        return new ReportFieldResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setDataSetFieldId(source.getDataSetField().getId())
                .setOrdinal(source.getOrdinal())
                .setVisible(source.getVisible())
                .setPivotTypeId(source.getPivotFieldType().getId())
                .setValid(source.getDataSetField().getIsSync())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
