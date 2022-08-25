package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterRoleTupleValueMapperTest {

    @InjectMocks
    private SecurityFilterRoleTupleValueMapper mapper;

    private final static Long ID = 1L;
    private final static String VALUE = "Value";

    @Test
    void from() {

        var response = mapper.from(getTupleValue());

        Assertions.assertNotNull(response.getField());
        Assertions.assertEquals(VALUE,response.getValue());

    }

    @Test
    void fromList() {

        var responses = mapper.from(Collections.singletonList(getTupleValue()));

        Assertions.assertEquals(1, responses.size());

        var response = responses.get(0);
        Assertions.assertNotNull(response.getField());
        Assertions.assertEquals(VALUE,response.getValue());
    }

    private TupleValue getTupleValue() {
        return new TupleValue()
                .setFieldId(ID)
                .setValue(VALUE);
    }

}
