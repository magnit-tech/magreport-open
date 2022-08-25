package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserRoleTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var userRole = getUserRole();

        assertEquals(ID, userRole.getId());
        assertEquals(NOW, userRole.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), userRole.getModifiedDateTime());

        assertNotNull(userRole.getRole());
        assertNotNull(userRole.getUser());
        assertNotNull(userRole.getUserRoleType());

        var testUserRole = new UserRole(-ID);
        assertEquals(-ID, testUserRole.getId());
    }

    private static UserRole getUserRole() {
        return new UserRole()
            .setId(ID)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setRole(new Role())
            .setUser(new User())
            .setUserRoleType(new UserRoleType());
    }
}