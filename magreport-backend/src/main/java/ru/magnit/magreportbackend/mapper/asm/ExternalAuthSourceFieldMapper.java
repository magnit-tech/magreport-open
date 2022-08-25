package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldType;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityDataSetFieldMapRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExternalAuthSourceFieldMapper implements Mapper<ExternalAuthSourceField, AsmSecurityDataSetFieldMapRequest> {

    private final ExternalAuthSourceFieldFilterInstanceFieldMapper filterInstanceFieldMapper;

    @Override
    public ExternalAuthSourceField from(AsmSecurityDataSetFieldMapRequest source) {
        final var externalAuthSourceField = new ExternalAuthSourceField()
                .setId(source.getId())
                .setType(new ExternalAuthSourceFieldType(source.getFieldType()))
                .setDataSetField(new DataSetField(source.getDataSetFieldId()))
                .setFilterInstanceFields(filterInstanceFieldMapper.from(source.getFilterInstanceFields()));

        externalAuthSourceField.getFilterInstanceFields().forEach(field -> field.setSourceField(externalAuthSourceField));

        return externalAuthSourceField;
    }
}
