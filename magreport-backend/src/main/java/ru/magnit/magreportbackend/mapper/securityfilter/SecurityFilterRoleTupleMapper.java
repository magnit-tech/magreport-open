package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTuple;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterRoleTupleMapper implements Mapper<SecurityFilterRoleTuple, Tuple> {

    private final SecurityFilterRoleTupleValueMapper tupleValueMapper;

    @Override
    public SecurityFilterRoleTuple from(Tuple source) {
        final var securityFilterRoleTuple = new SecurityFilterRoleTuple()
            .setTupleValues(tupleValueMapper.from(source.getValues()));
        securityFilterRoleTuple.getTupleValues().forEach(value -> value.setTuple(securityFilterRoleTuple));

        return securityFilterRoleTuple;
    }
}
