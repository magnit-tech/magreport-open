package ru.magnit.magreportbackend.domain.folderreport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FolderAuthorityTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "FolderAuthority";
    private static final String DESCRIPTION = "FolderAuthority description";
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var testObject = getFolderAuthority();
        var testObject2 = new ExternalAuth(-ID);

        assertEquals(ID, testObject.getId());
        assertEquals(NAME, testObject.getName());
        assertEquals(DESCRIPTION, testObject.getDescription());

        assertEquals(CREATED_DATE_TIME, testObject.getCreatedDateTime());
        assertEquals(CREATED_DATE_TIME.plusMinutes(2), testObject.getModifiedDateTime());
        assertNotNull(testObject.getPermissions());

        assertEquals(-ID, testObject2.getId());
    }

    private static FolderAuthority getFolderAuthority() {
        return new FolderAuthority()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setPermissions(Collections.singletonList(new FolderRolePermission()))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(CREATED_DATE_TIME.plusMinutes(2));
    }
}