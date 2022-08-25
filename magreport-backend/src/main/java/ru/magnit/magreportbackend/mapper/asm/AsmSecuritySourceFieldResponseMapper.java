package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecuritySourceFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldResponseMapper;


@Service
@RequiredArgsConstructor
public class AsmSecuritySourceFieldResponseMapper implements Mapper<AsmSecuritySourceFieldResponse, ExternalAuthSourceField> {

    private final DataSetFieldResponseMapper dataSetFieldResponseMapper;
    private final AsmFilterInstanceFieldResponseMapper fiFieldResponseMapper;

    @Override
    public AsmSecuritySourceFieldResponse from(ExternalAuthSourceField source) {

        return new AsmSecuritySourceFieldResponse(
                source.getId(),
                source.getTypeEnum(),
                dataSetFieldResponseMapper.from(source.getDataSetField()),
                fiFieldResponseMapper.from(source.getFilterInstanceFields()),
                source.getCreatedDateTime(),
                source.getModifiedDateTime());
    }
}
