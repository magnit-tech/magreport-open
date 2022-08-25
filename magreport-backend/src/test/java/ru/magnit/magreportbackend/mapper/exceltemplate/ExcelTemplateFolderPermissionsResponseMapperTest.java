package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateFolderPermissionsResponseMapperTest {

    @Mock
    private ExcelTemplateFolderResponseMapper excelTemplateFolderResponseMapper;

    @Mock
    private RolePermissionResponseMapperExcelTemplate rolePermissionResponseMapperExcelTemplate;

    @InjectMocks
    private ExcelTemplateFolderPermissionsResponseMapper mapper;

    @Test
    void from() {

        when(excelTemplateFolderResponseMapper.shallowMap(any(ExcelTemplateFolder.class))).thenReturn(new ExcelTemplateFolderResponse());
        when(rolePermissionResponseMapperExcelTemplate.from(anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(new ExcelTemplateFolder());

        assertNotNull(response.folder());
        assertEquals(0, response.rolePermissions().size());


        verify(excelTemplateFolderResponseMapper).shallowMap(any(ExcelTemplateFolder.class));
        verify(rolePermissionResponseMapperExcelTemplate).from(anyList());
        verifyNoMoreInteractions(excelTemplateFolderResponseMapper, rolePermissionResponseMapperExcelTemplate);
    }

    @Test
    void fromList() {
        when(excelTemplateFolderResponseMapper.shallowMap(any(ExcelTemplateFolder.class))).thenReturn(new ExcelTemplateFolderResponse());
        when(rolePermissionResponseMapperExcelTemplate.from(anyList())).thenReturn(Collections.emptyList());

        var responses = mapper.from(Collections.singletonList(new ExcelTemplateFolder()));

        assertEquals(1, responses.size());

        var response = responses.get(0);

        assertNotNull(response.folder());
        assertEquals(0, response.rolePermissions().size());


        verify(excelTemplateFolderResponseMapper).shallowMap(any(ExcelTemplateFolder.class));
        verify(rolePermissionResponseMapperExcelTemplate).from(anyList());
        verifyNoMoreInteractions(excelTemplateFolderResponseMapper, rolePermissionResponseMapperExcelTemplate);
    }
}
