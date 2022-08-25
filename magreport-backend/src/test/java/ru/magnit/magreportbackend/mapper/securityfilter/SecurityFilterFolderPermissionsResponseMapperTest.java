package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SecurityFilterFolderPermissionsResponseMapperTest {

    @Mock
    private SecurityFilterFolderResponseMapper folderResponseMapper;

    @Mock
    private RolePermissionResponseMapperSecurityFilter responseMapper;

    @InjectMocks
    private SecurityFilterFolderPermissionsResponseMapper mapper;

    @Test
    void from() {

        final var source = spy(new SecurityFilterFolder());

        when(folderResponseMapper.shallowMap(any(SecurityFilterFolder.class))).thenReturn(new SecurityFilterFolderResponse());
        when(responseMapper.from(anyList())).thenReturn(Collections.singletonList(new RolePermissionResponse(null, null)));

        final var result = mapper.from(source);

        assertNotNull(result.folder());
        assertNotNull(result.rolePermissions());
        assertEquals(1, result.rolePermissions().size());

        verify(source).getFolderRoles();
        verify(folderResponseMapper).shallowMap(any(SecurityFilterFolder.class));
        verify(responseMapper).from(anyList());
        verifyNoMoreInteractions(source, folderResponseMapper, responseMapper);
    }
}