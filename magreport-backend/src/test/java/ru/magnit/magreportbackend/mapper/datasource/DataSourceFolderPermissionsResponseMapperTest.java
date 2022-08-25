package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceFolderPermissionsResponseMapperTest {
    @Mock
    private DataSourceFolderResponseMapper dataSourceFolderResponseMapper;

    @Mock
    private RolePermissionResponseMapperDataSource rolePermissionResponseMapperDataSource;

    @InjectMocks
    private DataSourceFolderPermissionsResponseMapper mapper;

    @Test
    void from() {
        when(dataSourceFolderResponseMapper.shallowMap(any(DataSourceFolder.class))).thenReturn(new DataSourceFolderResponse());
        when(rolePermissionResponseMapperDataSource.from(anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(new DataSourceFolder());

        assertNotNull(response.folder());
        assertEquals(0, response.rolePermissions().size());
    }

    @Test
    void fromList() {
        when(dataSourceFolderResponseMapper.shallowMap(any(DataSourceFolder.class))).thenReturn(new DataSourceFolderResponse());
        when(rolePermissionResponseMapperDataSource.from(anyList())).thenReturn(Collections.emptyList());

        var responses = mapper.from(Collections.singletonList(new DataSourceFolder()));

        assertEquals(1, responses.size());

        var response = responses.get(0);

        assertNotNull(response.folder());
        assertEquals(0, response.rolePermissions().size());
    }
}


