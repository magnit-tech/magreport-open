package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTuple;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterRoleTupleResponseMapper implements Mapper<Tuple, SecurityFilterRoleTuple> {

    private final SecurityFilterRoleTupleValueResponseMapper valueResponseMapper;

    @Override
    public Tuple from(SecurityFilterRoleTuple source) {
        return new Tuple()
            .setValues(valueResponseMapper.from(source.getTupleValues()));
    }
}
