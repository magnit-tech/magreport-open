package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterRoleTupleResponseMapperTest {

    @Mock
    private SecurityFilterRoleTupleValueResponseMapper securityFilterRoleTupleValueResponseMapper;

    @InjectMocks
    private SecurityFilterRoleTupleResponseMapper mapper;

    @Test
    void from() {

        Mockito.when(securityFilterRoleTupleValueResponseMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(new TupleValue()));
        var response = mapper.from(new SecurityFilterRoleTuple());

        Assertions.assertEquals(1, response.getValues().size());

        Mockito.verify(securityFilterRoleTupleValueResponseMapper).from(Mockito.anyList());
        Mockito.verifyNoMoreInteractions(securityFilterRoleTupleValueResponseMapper);

    }


}
