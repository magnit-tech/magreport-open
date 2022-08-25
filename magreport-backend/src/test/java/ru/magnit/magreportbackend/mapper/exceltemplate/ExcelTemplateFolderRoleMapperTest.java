package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRolePermission;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateFolderRoleMapperTest {

    @Mock
    private ExcelTemplateFolderRolePermissionMapper excelTemplateFolderRolePermissionMapper;

    @InjectMocks
    private ExcelTemplateFolderRoleMapper mapper;

    @Test
    void from() {

        when(excelTemplateFolderRolePermissionMapper.from(anyList())).thenReturn(Collections.singletonList(new ExcelTemplateFolderRolePermission()));

        var response = mapper.from(getRoleAddPermissionRequest());

        assertNotNull(response.getRole());
        assertFalse(response.getPermissions().isEmpty());


        verify(excelTemplateFolderRolePermissionMapper).from(anyList());
        verifyNoMoreInteractions(excelTemplateFolderRolePermissionMapper);
    }

    @Test
    void fromList() {
        when(excelTemplateFolderRolePermissionMapper.from(anyList())).thenReturn(Collections.singletonList(new ExcelTemplateFolderRolePermission()));

        var responses = mapper.from(Collections.singletonList(getRoleAddPermissionRequest()));

        assertEquals(1, responses.size());
        var response = responses.get(0);

        assertNotNull(response.getRole());
        assertFalse(response.getPermissions().isEmpty());


        verify(excelTemplateFolderRolePermissionMapper).from(anyList());
        verifyNoMoreInteractions(excelTemplateFolderRolePermissionMapper);
    }

    private RoleAddPermissionRequest getRoleAddPermissionRequest() {
        return new RoleAddPermissionRequest()
                .setRoleId(1L)
                .setPermissions(Collections.emptyList());
    }
}
