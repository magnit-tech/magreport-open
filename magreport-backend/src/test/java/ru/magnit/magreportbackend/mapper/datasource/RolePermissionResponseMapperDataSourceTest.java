package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRole;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RolePermissionResponseMapperDataSourceTest {
    @Mock
    private RoleResponseMapper roleResponseMapper;

    @InjectMocks
    private RolePermissionResponseMapperDataSource mapper;

    private final static Long ID = 1L;

    @Test
    void from() {

        when(roleResponseMapper.from(any(Role.class))).thenReturn(new RoleResponse());

        var response = mapper.from(getDataSourceFolderRole());

        assertNotNull(response.role());
        assertEquals(1, response.permissions().size());


        verifyNoMoreInteractions( roleResponseMapper);
    }

    @Test
    void fromList() {

        when(roleResponseMapper.from(any(Role.class))).thenReturn(new RoleResponse());

        var responses = mapper.from(Collections.singletonList(getDataSourceFolderRole()));

        assertEquals(1,responses.size());
        var response = responses.get(0);

        assertNotNull(response.role());
        assertEquals(1, response.permissions().size());

        verifyNoMoreInteractions(roleResponseMapper);
    }

    private DataSourceFolderRole getDataSourceFolderRole() {
        return new DataSourceFolderRole()
                .setRole(new Role())
                .setPermissions(Collections.singletonList(
                        new DataSourceFolderRolePermission()
                                .setAuthority(
                                        new FolderAuthority()
                                        .setId(ID)
                                )
                ));
    }
}
