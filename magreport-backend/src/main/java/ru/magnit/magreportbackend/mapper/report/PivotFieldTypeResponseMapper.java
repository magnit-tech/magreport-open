package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.PivotFieldType;
import ru.magnit.magreportbackend.dto.response.report.PivotFieldTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class PivotFieldTypeResponseMapper implements Mapper<PivotFieldTypeResponse, PivotFieldType> {

    @Override
    public PivotFieldTypeResponse from(PivotFieldType source) {

        return new PivotFieldTypeResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
