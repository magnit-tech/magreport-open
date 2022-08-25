package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SecurityFilterFolderRolePermissionMapperTest {

    @InjectMocks
    private SecurityFilterFolderRolePermissionMapper mapper;

    @Test
    void from() {

        val result = mapper.from(getSource());

        assertEquals(FolderAuthorityEnum.READ.ordinal(), result.getAuthority().getId());
    }

    private FolderAuthorityEnum getSource() {

        return FolderAuthorityEnum.READ;
    }
}