package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterTemplateFolderRolePermissionMapperTest {

    @InjectMocks
    private FilterTemplateFolderRolePermissionMapper mapper;

    @Test
    void from() {

        final var source = FolderAuthorityEnum.READ;

        final var result = mapper.from(source);

        assertNotNull(result.getAuthority());
    }
}