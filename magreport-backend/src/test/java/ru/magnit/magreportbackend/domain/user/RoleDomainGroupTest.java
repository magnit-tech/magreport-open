package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleDomainGroupTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(5);
    }

    @Test
    void testNoArgsConstructor() {
        var roleDomainGroups = getRoleDomainGroups();

        assertEquals(ID, roleDomainGroups.getId());
        assertEquals(NOW, roleDomainGroups.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), roleDomainGroups.getModifiedDateTime());

        assertNotNull(roleDomainGroups.getRole());
        assertNotNull(roleDomainGroups.getDomainGroup());

        var testingRole = new RoleDomainGroup(-ID);
        assertEquals(-ID, testingRole.getId());
    }

    private static RoleDomainGroup getRoleDomainGroups() {
        return new RoleDomainGroup()
                .setId(ID)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setRole(new Role())
                .setDomainGroup(new DomainGroup());
    }
}