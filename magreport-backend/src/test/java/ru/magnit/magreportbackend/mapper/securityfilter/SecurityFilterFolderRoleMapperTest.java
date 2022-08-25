package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRole;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRolePermission;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class SecurityFilterFolderRoleMapperTest {

    private static final long ROLE_ID = 1L;
    private static final long ID = 2L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @InjectMocks
    private SecurityFilterFolderRoleMapper mapper;

    @Mock
    private SecurityFilterFolderRolePermissionMapper permissionMapper;

    @Test
    void from() {
        given(permissionMapper.from(ArgumentMatchers.<List<FolderAuthorityEnum>>any())).willReturn(getFolderAuthorities());

        val result = mapper.from(getSource());

        assertEquals(ROLE_ID, result.getRole().getId());
        assertEquals(ID, result.getPermissions().get(0).getId());
        assertEquals(CREATED_DATE_TIME, result.getPermissions().get(0).getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, result.getPermissions().get(0).getModifiedDateTime());

        assertNotNull(result.getPermissions().get(0).getFolderRole());
        assertNotNull(result.getPermissions().get(0).getAuthority());

        verify(permissionMapper).from(ArgumentMatchers.<List<FolderAuthorityEnum>>any());
        verifyNoMoreInteractions(permissionMapper);
    }

    private List<SecurityFilterFolderRolePermission> getFolderAuthorities() {

        return Collections.singletonList(
                new SecurityFilterFolderRolePermission()
                        .setId(ID)
                        .setFolderRole(new SecurityFilterFolderRole())
                        .setAuthority(new FolderAuthority())
                        .setCreatedDateTime(CREATED_DATE_TIME)
                        .setModifiedDateTime(MODIFIED_DATE_TIME));
    }

    private RoleAddPermissionRequest getSource() {
        return new RoleAddPermissionRequest()
                .setRoleId(ROLE_ID)
                .setPermissions(Collections.singletonList(FolderAuthorityEnum.WRITE));
    }
}