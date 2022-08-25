package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceFolderRoleMapperTest {
    @Mock
    private DataSourceFolderRolePermissionMapper dataSourceFolderRolePermissionMapper;

    @InjectMocks
    private DataSourceFolderRoleMapper mapper;

    @Test
    void from() {

        when(dataSourceFolderRolePermissionMapper.from(anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(getRoleAddPermissionRequest());

        assertEquals(new Role(1L), response.getRole());
        assertTrue(response.getPermissions().isEmpty());

        verify(dataSourceFolderRolePermissionMapper).from(anyList());
        verifyNoMoreInteractions(dataSourceFolderRolePermissionMapper);
    }

    @Test
    void fromList() {
        when(dataSourceFolderRolePermissionMapper.from(anyList())).thenReturn(Collections.emptyList());

        var responses = mapper.from(Collections.singletonList(getRoleAddPermissionRequest()));

        assertEquals(1, responses.size());

        var response = responses.get(0);

        assertEquals(new Role(1L), response.getRole());
        assertTrue(response.getPermissions().isEmpty());

        verify(dataSourceFolderRolePermissionMapper).from(anyList());
        verifyNoMoreInteractions(dataSourceFolderRolePermissionMapper);
    }

    private RoleAddPermissionRequest getRoleAddPermissionRequest() {
        return new RoleAddPermissionRequest()
            .setRoleId(1L)
            .setPermissions(Collections.emptyList());
    }
}
