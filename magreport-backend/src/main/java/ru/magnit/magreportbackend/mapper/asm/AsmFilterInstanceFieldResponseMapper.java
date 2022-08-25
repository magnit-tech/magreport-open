package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldFilterInstanceField;
import ru.magnit.magreportbackend.dto.response.asm.AsmFilterInstanceFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFieldResponseMapper;

@Service
@RequiredArgsConstructor
public class AsmFilterInstanceFieldResponseMapper implements Mapper<AsmFilterInstanceFieldResponse, ExternalAuthSourceFieldFilterInstanceField> {

    private final FilterInstanceFieldResponseMapper filterInstanceFieldMapper;

    @Override
    public AsmFilterInstanceFieldResponse from(ExternalAuthSourceFieldFilterInstanceField source) {

        return new AsmFilterInstanceFieldResponse(
                source.getId(),
                filterInstanceFieldMapper.from(source.getFilterInstanceField()),
                source.getCreatedDateTime(),
                source.getModifiedDateTime()
        );
    }
}
