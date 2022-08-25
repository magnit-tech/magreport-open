package ru.magnit.magreportbackend.mapper.dataset;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataSetFolderRolePermissionMapperTest {

    @InjectMocks
    private DataSetFolderRolePermissionMapper mapper;

    @Test
    void from() {
        var response = mapper.from(FolderAuthorityEnum.WRITE);
        assertNotNull(response.getAuthority().getId());
    }

}

