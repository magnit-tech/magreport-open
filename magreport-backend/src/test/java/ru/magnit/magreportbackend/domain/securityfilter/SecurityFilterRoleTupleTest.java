package ru.magnit.magreportbackend.domain.securityfilter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityFilterRoleTupleTest extends BaseEntityTest {
    private static final long ID = 1L;
    private static final long SECURITY_FILTER_ROLE_ID = 2L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2L);

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(5);
    }

    @Test
    void testNoArgsConstructor() {
        var result = getObject();

        assertEquals(ID, result.getId());
        assertEquals(SECURITY_FILTER_ROLE_ID, result.getSecurityFilterRole().getId());

        assertEquals(CREATED_DATE_TIME, result.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, result.getModifiedDateTime());

        var testFilter = new SecurityFilterRoleTuple(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static SecurityFilterRoleTuple getObject() {

        return new SecurityFilterRoleTuple()
                .setId(ID)
                .setSecurityFilterRole(new SecurityFilterRole(SECURITY_FILTER_ROLE_ID))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME)
                .setTupleValues(Collections.singletonList(new SecurityFilterRoleTupleValue()));
    }
}