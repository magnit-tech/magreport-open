package ru.magnit.magreportbackend.domain.folderreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FolderRolePermissionTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(5);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getFolderRolePermission();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertNotNull(testObject.getAuthority());
        assertNotNull(testObject.getFolderRole());
        assertEquals(NOW, testObject.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), testObject.getModifiedDateTime());

        assertEquals(-ID, testObject2.getId());
    }

    private static FolderRolePermission getFolderRolePermission() {
        return new FolderRolePermission()
            .setId(ID)
            .setFolderRole(new FolderRole())
            .setAuthority(new FolderAuthority())
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2));
    }
}