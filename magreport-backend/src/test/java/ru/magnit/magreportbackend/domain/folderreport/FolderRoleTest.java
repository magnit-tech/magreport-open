package ru.magnit.magreportbackend.domain.folderreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.user.Role;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FolderRoleTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getFolderRole();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertNotNull(testObject.getRole());
        assertNotNull(testObject.getFolder());

        assertNotNull(testObject.getPermissions());
        assertEquals(NOW, testObject.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), testObject.getModifiedDateTime());

        assertEquals(-ID, testObject2.getId());
    }

    private static FolderRole getFolderRole() {
        return new FolderRole()
            .setId(ID)
            .setRole(new Role())
            .setFolder(new Folder())
            .setPermissions(Collections.singletonList(new FolderRolePermission()))
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2));
    }

}