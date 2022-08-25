package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class FilterTemplateFolderRoleMapperTest {

    @Mock
    private FilterTemplateFolderRolePermissionMapper filterTemplateFolderRolePermissionMapper;

    @InjectMocks
    private FilterTemplateFolderRoleMapper mapper;

    private final static Long ID = 1L;

    @Test
    void from() {

        Mockito.when(filterTemplateFolderRolePermissionMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(new FilterTemplateFolderRolePermission()));

        var response = mapper.from(getRoleAddPermissionRequest());

        Assertions.assertNotNull(response.getRole());
        Assertions.assertEquals(1, response.getPermissions().size());
        Assertions.assertNull(response.getFolder());


        Mockito.verify(filterTemplateFolderRolePermissionMapper).from(Mockito.anyList());
        Mockito.verifyNoMoreInteractions(filterTemplateFolderRolePermissionMapper);
    }

    private RoleAddPermissionRequest getRoleAddPermissionRequest() {
        return new RoleAddPermissionRequest()
                .setRoleId(ID)
                .setPermissions(Collections.singletonList(FolderAuthorityEnum.WRITE));
    }
}
