package ru.magnit.magreportbackend.mapper.folderreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderRolePermission;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum.READ;

@ExtendWith(MockitoExtension.class)
class FolderRolePermissionMapperTest {

    private final FolderAuthorityEnum SOURCE = READ;

    @InjectMocks
    private FolderRolePermissionMapper mapper;

    @Test
    void from() {
        List<FolderRolePermission> permissions = mapper.from(Collections.singletonList(SOURCE));
        assertNotEquals(0, permissions.size());

        FolderRolePermission permission = permissions.get(0);
        assertNotNull(permission.getAuthority());
        assertEquals(SOURCE.ordinal(), permission.getAuthority().getId());

        assertNull(mapper.from(SOURCE));
    }


}