package ru.magnit.magreportbackend.mapper.folderreport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.folderreport.FolderRoleView;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum.READ;

@ExtendWith(MockitoExtension.class)
class FolderRoleViewMapperTest {

    private final Long ID = 1L;
    private final FolderAuthorityEnum auth = READ;

    @InjectMocks
    private FolderRoleViewMapper mapper;

    @Test
    void from() {
        List<FolderRoleView> views = mapper.from(getRequest());
        assertNotEquals(0, views.size());

        FolderRoleView view = views.get(0);

        assertEquals(ID, view.getFolderId());
        assertEquals(ID, view.getRoleId());
        assertNotNull(view.getPermissions());
        assertEquals(auth, view.getPermissions().get(0));
    }

    private FolderPermissionSetRequest getRequest() {
        return new FolderPermissionSetRequest()
            .setFolderId(ID)
            .setRoles(Collections.singletonList(new RoleAddPermissionRequest().setRoleId(ID).setPermissions(Collections.singletonList(auth))));
    }
}