package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RolePermissionResponseMapperDataSetTest {

    @Mock
    private RoleResponseMapper roleResponseMapper;
    @InjectMocks
    private RolePermissionResponseMapperDataSet mapper;

    private final static Long ID = 1L;
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void from() {
        when(roleResponseMapper.from(any(Role.class))).thenReturn(new RoleResponse());

        var response = mapper.from(getDataSetFolderRole());

        assertNotNull(response.role());
        assertEquals(1, response.permissions().size());


        verify(roleResponseMapper).from(any(Role.class));
        verifyNoMoreInteractions(roleResponseMapper);

    }

    private DataSetFolderRole getDataSetFolderRole() {
        return new DataSetFolderRole()
                .setId(ID)
                .setRole(new Role())
                .setFolder(new DataSetFolder())
                .setPermissions(Collections.singletonList(
                        new DataSetFolderRolePermission()
                                .setId(ID)
                                .setAuthority(new FolderAuthority().setId(ID))))
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}
