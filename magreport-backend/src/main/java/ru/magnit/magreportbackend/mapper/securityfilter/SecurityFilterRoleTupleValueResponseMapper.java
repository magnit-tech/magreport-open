package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTupleValue;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterRoleTupleValueResponseMapper implements Mapper<TupleValue, SecurityFilterRoleTupleValue> {

    @Override
    public TupleValue from(SecurityFilterRoleTupleValue source) {
        return new TupleValue()
                .setFieldId(source.getField().getId())
                .setValue(source.getValue());
    }
}
