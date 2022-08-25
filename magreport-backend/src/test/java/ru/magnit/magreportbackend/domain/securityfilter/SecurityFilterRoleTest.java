package ru.magnit.magreportbackend.domain.securityfilter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.user.Role;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityFilterRoleTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final long SECURITY_FILTER_ID = 2L;
    private static final long ROLE_ID = 3L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2L);

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var result = getObject();

        assertEquals(ID, result.getId());
        assertEquals(SECURITY_FILTER_ID, result.getSecurityFilter().getId());
        assertEquals(ROLE_ID, result.getRole().getId());

        assertEquals(CREATED_DATE_TIME, result.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, result.getModifiedDateTime());

        var testFilter = new SecurityFilterRole(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static SecurityFilterRole getObject() {

        return new SecurityFilterRole()
                .setId(ID)
                .setSecurityFilter(new SecurityFilter(SECURITY_FILTER_ID))
                .setRole(new Role(ROLE_ID))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME)
                .setFilterRoleTuples(Collections.singletonList(new SecurityFilterRoleTuple()));
    }
}