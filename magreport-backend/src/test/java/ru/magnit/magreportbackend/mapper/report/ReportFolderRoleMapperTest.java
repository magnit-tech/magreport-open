package ru.magnit.magreportbackend.mapper.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.report.ReportFolderRole;
import ru.magnit.magreportbackend.domain.report.ReportFolderRolePermission;
import ru.magnit.magreportbackend.dto.inner.folderreport.FolderRoleView;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
class ReportFolderRoleMapperTest {

    private static final long FOLDER_ID = 1L;
    private static final long ROLE_ID = 2L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @InjectMocks
    private ReportFolderRoleMapper mapper;

    @Mock
    private ReportFolderRolePermissionMapper permissionMapper;

    @Test
    void from() {
        given(permissionMapper.from(ArgumentMatchers.<List<FolderAuthorityEnum>>any())).willReturn(Collections.singletonList(getPermission()));

        var result = mapper.from(getFolderRoleView());

        assertEquals(ROLE_ID, result.getRole().getId());
        assertEquals(FOLDER_ID, result.getFolder().getId());
        assertEquals(FolderAuthorityEnum.READ, FolderAuthorityEnum.getById(result.getPermissions().get(0).getAuthority().getId()));

        verify(permissionMapper).from(ArgumentMatchers.<List<FolderAuthorityEnum>>any());
        verifyNoMoreInteractions(permissionMapper);
    }

    private ReportFolderRolePermission getPermission() {

        return new ReportFolderRolePermission()
                .setId(1L)
                .setAuthority(new FolderAuthority(1L))
                .setFolderRole(new ReportFolderRole(1L))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }

    private FolderRoleView getFolderRoleView() {
        return new FolderRoleView()
                .setFolderId(FOLDER_ID)
                .setRoleId(ROLE_ID)
                .setPermissions(Collections.singletonList(FolderAuthorityEnum.READ));
    }
}