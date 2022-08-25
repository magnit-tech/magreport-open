package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecuritySourceResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;

@Service
@RequiredArgsConstructor
public class AsmSecuritySourceResponseMapper implements Mapper<AsmSecuritySourceResponse, ExternalAuthSource> {

    private final DataSetResponseMapper dataSetResponseMapper;
    private final AsmSecuritySourceFieldResponseMapper fieldResponseMapper;
    private final AsmSecurityFilterResponseMapper securityFilterResponseMapper;

    @Override
    public AsmSecuritySourceResponse from(ExternalAuthSource source) {

        return new AsmSecuritySourceResponse(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getTypeEnum(),
                dataSetResponseMapper.from(source.getDataSet()),
                source.getPreSql(),
                source.getPostSql(),
                fieldResponseMapper.from(source.getFields()),
                securityFilterResponseMapper.from(source.getSecurityFilters()),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
