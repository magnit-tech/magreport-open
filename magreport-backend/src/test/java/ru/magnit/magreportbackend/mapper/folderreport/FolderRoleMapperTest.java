package ru.magnit.magreportbackend.mapper.folderreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;
import ru.magnit.magreportbackend.domain.folderreport.FolderRolePermission;
import ru.magnit.magreportbackend.dto.inner.folderreport.FolderRoleView;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum.READ;

@ExtendWith(MockitoExtension.class)
class FolderRoleMapperTest {

    private final Long ID = 1L;
    private final FolderAuthorityEnum PERMISSION = READ;

    @InjectMocks
    private FolderRoleMapper mapper;

    @Mock
    private FolderRolePermissionMapper folderRolePermissionMapper;

    @Test
    void from() {
        when(folderRolePermissionMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new FolderRolePermission()));

        FolderRole folderRole = mapper.from(getView());

        assertNotNull(folderRole.getRole());
        assertEquals(ID, folderRole.getRole().getId());
        assertNotNull(folderRole.getFolder());
        assertEquals(ID, folderRole.getFolder().getId());

        List<FolderRole> folderRoles = mapper.from(Collections.singletonList(getView()));
        assertNotEquals(0, folderRoles.size());
        folderRole = folderRoles.get(0);

        assertNotNull(folderRole.getRole());
        assertEquals(ID, folderRole.getRole().getId());
        assertNotNull(folderRole.getFolder());
        assertEquals(ID, folderRole.getFolder().getId());
    }

    private FolderRoleView getView() {
        return new FolderRoleView()
                .setFolderId(ID)
                .setRoleId(ID)
                .setPermissions(Collections.singletonList(PERMISSION));
    }
}