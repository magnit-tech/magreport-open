package ru.magnit.magreportbackend.mapper.exceltemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class ExcelTemplateFolderRolePermissionMapperTest {

    @InjectMocks
    private ExcelTemplateFolderRolePermissionMapper mapper;

    @Test
    void from() {
        assertNotNull(mapper.from(FolderAuthorityEnum.WRITE));
    }

    @Test
    void fromList() {
        var responses = mapper.from(Collections.singletonList(FolderAuthorityEnum.WRITE));
        assertEquals(1, responses.size());
        assertNotNull(responses.get(0));
    }

}
