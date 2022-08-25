package ru.magnit.magreportbackend.mapper.folderreport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class FolderReportPermissionsResponseMapperTest {

    @Mock
    private FolderResponseMapper folderResponseMapper;

    @Mock
    private RolePermissionResponseMapper rolePermissionResponseMapper;

    @InjectMocks
    private FolderReportPermissionsResponseMapper mapper;


    @Test
    void from() {
        Mockito.when(folderResponseMapper.shallowMap(Mockito.any(Folder.class))).thenReturn(new FolderResponse());
        Mockito.when(rolePermissionResponseMapper.from(Mockito.anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(new Folder());

        Assertions.assertNotNull(response.folder());
        Assertions.assertTrue(response.rolePermissions().isEmpty());

        Mockito.verifyNoMoreInteractions(folderResponseMapper, rolePermissionResponseMapper);
    }

    @Test
    void fromList() {
        Mockito.when(folderResponseMapper.shallowMap(Mockito.any(Folder.class))).thenReturn(new FolderResponse());
        Mockito.when(rolePermissionResponseMapper.from(Mockito.anyList())).thenReturn(Collections.emptyList());

        var responses = mapper.from(Collections.singletonList(new Folder()));

        Assertions.assertEquals(1, responses.size());
        var response = responses.get(0);

        Assertions.assertNotNull(response.folder());
        Assertions.assertTrue(response.rolePermissions().isEmpty());

        Mockito.verifyNoMoreInteractions(folderResponseMapper, rolePermissionResponseMapper);

    }

}
