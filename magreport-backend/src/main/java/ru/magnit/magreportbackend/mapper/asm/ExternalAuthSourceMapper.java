package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceType;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecuritySourceAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;


@Service
@RequiredArgsConstructor
public class ExternalAuthSourceMapper implements Mapper<ExternalAuthSource, AsmSecuritySourceAddRequest> {

    private final ExternalAuthSourceFieldMapper fieldMapper;
    private final ExternalAuthSecurityFilterMapper securityFilterMapper;

    @Override
    public ExternalAuthSource from(AsmSecuritySourceAddRequest source) {
        final var externalAuthSource = new ExternalAuthSource()
                .setId(source.getId())
                .setType(new ExternalAuthSourceType(source.getSourceType()))
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setPreSql(source.getPreSql())
                .setPostSql(source.getPostSql())
                .setDataSet(new DataSet(source.getDataSetId()))
                .setSecurityFilters(securityFilterMapper.from(source.getSecurityFilters()))
                .setFields(fieldMapper.from(source.getFields()));
        externalAuthSource.getSecurityFilters().forEach(securityFilter -> securityFilter.setSource(externalAuthSource));
        externalAuthSource.getFields().forEach(field -> field.setSource(externalAuthSource));

        return externalAuthSource;
    }
}
