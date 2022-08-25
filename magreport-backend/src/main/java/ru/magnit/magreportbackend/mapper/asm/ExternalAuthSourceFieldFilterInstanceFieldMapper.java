package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldFilterInstanceField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityFilterInstanceFieldAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExternalAuthSourceFieldFilterInstanceFieldMapper implements Mapper<ExternalAuthSourceFieldFilterInstanceField, AsmSecurityFilterInstanceFieldAddRequest> {

    @Override
    public ExternalAuthSourceFieldFilterInstanceField from(AsmSecurityFilterInstanceFieldAddRequest source) {
        return new ExternalAuthSourceFieldFilterInstanceField()
                .setId(source.getId())
                .setFilterInstanceField(new FilterInstanceField(source.getFilterInstanceFieldId()));
    }
}
