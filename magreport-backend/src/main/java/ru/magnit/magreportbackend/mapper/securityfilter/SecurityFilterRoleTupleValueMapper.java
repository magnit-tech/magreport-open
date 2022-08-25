package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTupleValue;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterRoleTupleValueMapper implements Mapper<SecurityFilterRoleTupleValue, TupleValue> {

    @Override
    public SecurityFilterRoleTupleValue from(TupleValue source) {

        return new SecurityFilterRoleTupleValue()
                .setField(new FilterInstanceField(source.getFieldId()))
                .setValue(source.getValue());
    }
}
