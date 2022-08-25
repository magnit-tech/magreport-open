package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserRoleTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Type test";
    private static final String DESCRIPTION = "Type test description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var userRoleType = getUserRoleType();

        assertEquals(ID, userRoleType.getId());
        assertEquals(NAME, userRoleType.getName());
        assertEquals(DESCRIPTION, userRoleType.getDescription());

        assertEquals(NOW, userRoleType.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), userRoleType.getModifiedDateTime());
        assertNotNull(userRoleType.getUserRoles());

        var testingType = new UserRoleType((long) UserRoleTypeEnum.DOMAIN.ordinal());
        assertEquals(ID, testingType.getId());
    }

    private static UserRoleType getUserRoleType() {
        return new UserRoleType()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setUserRoles(Collections.singletonList(new UserRole()));
    }
}