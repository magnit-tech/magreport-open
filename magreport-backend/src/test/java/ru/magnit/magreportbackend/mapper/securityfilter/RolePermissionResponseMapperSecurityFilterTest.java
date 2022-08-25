package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRole;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRolePermission;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RolePermissionResponseMapperSecurityFilterTest {

    @Mock
    private RoleResponseMapper roleResponseMapper;

    @InjectMocks
    private RolePermissionResponseMapperSecurityFilter mapper;

    @Test
    void from() {
        final var role = new Role(1L);
        final var authority_enum = FolderAuthorityEnum.READ;
        final var authority = spy(new FolderAuthority(authority_enum));
        final var permission = spy(new SecurityFilterFolderRolePermission().setAuthority(authority));
        final var permissions = Collections.singletonList(permission);

        final var source = spy(new SecurityFilterFolderRole()
                .setRole(role)
                .setPermissions(permissions));

        when(roleResponseMapper.from(any(Role.class))).thenReturn(new RoleResponse());

        final var result = mapper.from(source);

        assertNotNull(result.role());

        verify(source).getRole();
        verify(source).getPermissions();
        verify(permission).getAuthority();
        verify(authority).getId();
        verify(roleResponseMapper).from(any(Role.class));

        verifyNoMoreInteractions(source, roleResponseMapper, authority, permission);
    }
}