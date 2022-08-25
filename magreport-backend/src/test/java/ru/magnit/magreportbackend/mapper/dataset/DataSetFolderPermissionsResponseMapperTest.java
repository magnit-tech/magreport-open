package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.permission.RolePermissionResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSetFolderPermissionsResponseMapperTest {
    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();
    @Mock
    private DataSetFolderResponseMapper dataSetFolderResponseMapper;
    @Mock
    private RolePermissionResponseMapperDataSet rolePermissionResponseMapperDataSet;
    @InjectMocks
    private DataSetFolderPermissionsResponseMapper mapper;

    @Test
    void from() {
        when(dataSetFolderResponseMapper.shallowMap(any(DataSetFolder.class))).thenReturn(getDataSetFolderResponse());
        when(rolePermissionResponseMapperDataSet.from(anyList())).thenReturn(Collections.singletonList(getRolePermissionResponse()));

        var response = mapper.from(new DataSetFolder());

        assertNotNull(response.folder());
        assertEquals(1, response.rolePermissions().size());

        verify(dataSetFolderResponseMapper).shallowMap(any(DataSetFolder.class));
        verify(rolePermissionResponseMapperDataSet).from(anyList());
        verifyNoMoreInteractions(dataSetFolderResponseMapper, rolePermissionResponseMapperDataSet);
    }

    private DataSetFolderResponse getDataSetFolderResponse() {
        return new DataSetFolderResponse()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setParentId(2L)
            .setChildFolders(Collections.emptyList())
            .setDataSets(Collections.emptyList())
            .setAuthority(FolderAuthorityEnum.WRITE)
            .setCreated(CREATE_TIME)
            .setModified(MODIFIED_TIME);
    }

    private RolePermissionResponse getRolePermissionResponse() {
        return new RolePermissionResponse(
            new RoleResponse(),
            Collections.singletonList(FolderAuthorityEnum.WRITE)
        );
    }


}
