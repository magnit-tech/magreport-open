package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSetFolderRoleMapperTest {
    @Mock
    private DataSetFolderRolePermissionMapper dataSetFolderRolePermissionMapper;

    @InjectMocks
    private DataSetFolderRoleMapper mapper;

    private final static Long ID = 1L;

    @Test
    void from() {
        when(dataSetFolderRolePermissionMapper.from(anyList())).thenReturn(Collections.singletonList(new DataSetFolderRolePermission()));

        var response = mapper.from(getRoleAddPermissionRequest());

        assertNotNull(response.getRole());
        assertEquals(1, response.getPermissions().size());

        verify(dataSetFolderRolePermissionMapper).from(anyList());
    }

    private RoleAddPermissionRequest getRoleAddPermissionRequest() {
        return new RoleAddPermissionRequest()
                .setRoleId(ID)
                .setPermissions(Collections.singletonList(FolderAuthorityEnum.WRITE));
    }


}
