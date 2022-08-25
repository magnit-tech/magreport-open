package ru.magnit.magreportbackend.domain.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "Test role name";
    private static final String DESCRIPTION = "Test role description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(10);
    }

    @Test
    void testNoArgsConstructor() {
        var role = getRole();

        assertEquals(ID, role.getId());
        assertEquals(NAME, role.getName());
        assertEquals(DESCRIPTION, role.getDescription());
        assertEquals(NOW, role.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), role.getModifiedDateTime());

        assertNotNull(role.getRoleType());
        assertNotNull(role.getRoleDomainGroups());
        assertNotNull(role.getUserRoles());
        assertNotNull(role.getFolderRoles());
        assertNotNull(role.getSecurityFilterRoles());

        var testingRole = new Role(-ID);
        assertEquals(-ID, testingRole.getId());
    }

    private static Role getRole() {
        return new Role()
                .setId(ID)
                .setRoleType(new RoleType(RoleTypeEnum.SYSTEM))
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setRoleDomainGroups(Collections.singletonList(new RoleDomainGroup()))
                .setUserRoles(Collections.singletonList(new UserRole()))
                .setFolderRoles(Collections.singletonList(new FolderRole()))
                .setSecurityFilterRoles(Collections.singletonList(new SecurityFilterRole()));
    }
}
