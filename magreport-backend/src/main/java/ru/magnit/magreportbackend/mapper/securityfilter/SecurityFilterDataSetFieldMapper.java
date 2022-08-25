package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;
import ru.magnit.magreportbackend.dto.request.securityfilter.FieldMapping;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterDataSetFieldMapper implements Mapper<SecurityFilterDataSetField, FieldMapping> {

    @Override
    public SecurityFilterDataSetField from(FieldMapping source) {
        return new SecurityFilterDataSetField()
                .setFilterInstanceField(new FilterInstanceField(source.getFilterInstanceFieldId()))
                .setDataSetField(new DataSetField(source.getDataSetFieldId()));
    }
}
