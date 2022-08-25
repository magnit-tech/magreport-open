package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRole;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class RolePermissionResponseMapperExcelTemplateTest {

    @Mock
    private RoleResponseMapper roleResponseMapper;

    @InjectMocks
    private RolePermissionResponseMapperExcelTemplate mapper;

    private final static Long ID = 1L;


    @Test
    void from() {

        when(roleResponseMapper.from(any(Role.class))).thenReturn(new RoleResponse());

        var response = mapper.from(getExcelTemplateFolderRole());

        assertNotNull(response.role());
        assertEquals(1, response.permissions().size());

        verifyNoMoreInteractions(roleResponseMapper);
    }

    @Test
    void fromList() {
        when(roleResponseMapper.from(any(Role.class))).thenReturn(new RoleResponse());

        var responses = mapper.from(Collections.singletonList(getExcelTemplateFolderRole()));

        assertEquals(1, responses.size());
        var response = responses.get(0);

        assertNotNull(response.role());
        assertEquals(1, response.permissions().size());


        verifyNoMoreInteractions(roleResponseMapper);
    }

    private ExcelTemplateFolderRole getExcelTemplateFolderRole() {
        return new ExcelTemplateFolderRole()
                .setRole(new Role())
                .setPermissions(Collections.singletonList(
                        new ExcelTemplateFolderRolePermission()
                                .setAuthority(
                                        new FolderAuthority().setId(ID)
                                )
                ));

    }
}
