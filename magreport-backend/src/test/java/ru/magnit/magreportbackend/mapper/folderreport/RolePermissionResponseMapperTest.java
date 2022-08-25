package ru.magnit.magreportbackend.mapper.folderreport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;
import ru.magnit.magreportbackend.domain.folderreport.FolderRolePermission;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class RolePermissionResponseMapperTest {

    @Mock
    private RoleResponseMapper roleResponseMapper;

    @InjectMocks
    private RolePermissionResponseMapper mapper;

    private final static Long ID = 1L;

    @Test
    void from() {
        Mockito.when(roleResponseMapper.from(Mockito.any(Role.class))).thenReturn(new RoleResponse());

        var response = mapper.from(getFolderRole());

        Assertions.assertNotNull(response.role());
        Assertions.assertEquals(1, response.permissions().size());


        Mockito.verifyNoMoreInteractions(roleResponseMapper);

    }

    @Test
    void fromList() {

        Mockito.when(roleResponseMapper.from(Mockito.any(Role.class))).thenReturn(new RoleResponse());

        var responses = mapper.from(Collections.singletonList(getFolderRole()));

        Assertions.assertEquals(1, responses.size());

        var response = responses.get(0);

        Assertions.assertNotNull(response.role());
        Assertions.assertEquals(1, response.permissions().size());

        Mockito.verifyNoMoreInteractions( roleResponseMapper);
    }

    private FolderRole getFolderRole() {
        return new FolderRole()
                .setRole(new Role())
                .setPermissions(Collections.singletonList(
                        new FolderRolePermission()
                                .setAuthority(new FolderAuthority().setId(ID))
                ));
    }


}
