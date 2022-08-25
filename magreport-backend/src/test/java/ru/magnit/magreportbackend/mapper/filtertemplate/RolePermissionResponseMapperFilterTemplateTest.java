package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRole;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class RolePermissionResponseMapperFilterTemplateTest {


    @Mock
    private RoleResponseMapper roleResponseMapper;

    @InjectMocks
    RolePermissionResponseMapperFilterTemplate mapper;

    private final static Long ID = 1L;


    @Test
    void from() {

        Mockito.when(roleResponseMapper.from(Mockito.any(Role.class))).thenReturn(new RoleResponse());

        var response = mapper.from(getFilterTemplateFolderRole());

        Assertions.assertNotNull(response.role());
        Assertions.assertEquals(1, response.permissions().size());

        Mockito.verify(roleResponseMapper).from(Mockito.any(Role.class));
        Mockito.verifyNoMoreInteractions(roleResponseMapper);
    }

    private FilterTemplateFolderRole getFilterTemplateFolderRole() {
        return new FilterTemplateFolderRole()
                .setId(ID)
                .setRole(new Role().setId(ID))
                .setPermissions(
                        Collections.singletonList(
                                new FilterTemplateFolderRolePermission()
                                        .setId(ID)
                                        .setAuthority(new FolderAuthority().setId(ID))
                        )
                );
    }
}
