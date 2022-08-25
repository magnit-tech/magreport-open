package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterRoleTupleMapperTest {

    @Mock
    private SecurityFilterRoleTupleValueMapper securityFilterRoleTupleValueMapper;

    @InjectMocks
    private SecurityFilterRoleTupleMapper mapper;

    private final static Long ID = 1L;
    private final static String VALUE = "Value";

    @Test
    void from() {
        Mockito.when(securityFilterRoleTupleValueMapper.from(Mockito.anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(getTuple());

        Assertions.assertNotNull(response.getTupleValues());


        Mockito.verify(securityFilterRoleTupleValueMapper).from(Mockito.anyList());
        Mockito.verifyNoMoreInteractions(securityFilterRoleTupleValueMapper);

    }

    @Test
    void fromList() {

        Mockito.when(securityFilterRoleTupleValueMapper.from(Mockito.anyList())).thenReturn(Collections.emptyList());

        var responses = mapper.from(Collections.singletonList(getTuple()));
        Assertions.assertEquals(1, responses.size());

        var response = responses.get(0);
        Assertions.assertNotNull(response.getTupleValues());

        Mockito.verify(securityFilterRoleTupleValueMapper).from(Mockito.anyList());
        Mockito.verifyNoMoreInteractions(securityFilterRoleTupleValueMapper);

    }

    private Tuple getTuple() {
        return new Tuple()
                .setValues(
                        Collections.singletonList(
                                new TupleValue()
                                        .setFieldId(ID)
                                        .setValue(VALUE)
                        )
                );
    }

}
