package ru.magnit.magreportbackend.domain.securityfilter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityFilterRoleTupleValueTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final long SECURITY_FILTER_ID = 2L;
    private static final long FIELD_ID = 3L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2L);
    private static final String VALUE = "Test value";

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var result = getObject();

        assertEquals(ID, result.getId());
        assertEquals(SECURITY_FILTER_ID, result.getTuple().getId());
        assertEquals(FIELD_ID, result.getField().getId());
        assertEquals(VALUE, result.getValue());

        assertEquals(CREATED_DATE_TIME, result.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, result.getModifiedDateTime());

        var testFilter = new SecurityFilterRoleTupleValue(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static SecurityFilterRoleTupleValue getObject() {

        return new SecurityFilterRoleTupleValue()
                .setId(ID)
                .setTuple(new SecurityFilterRoleTuple(SECURITY_FILTER_ID))
                .setField(new FilterInstanceField(FIELD_ID))
                .setValue(VALUE)
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}