package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;

@ExtendWith(MockitoExtension.class)
class FolderNodeResponseSecurityFilterFolderMapperTest {

    @InjectMocks
    private FolderNodeResponseSecurityFilterFolderMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";

    @Test
    void from() {

        var response = mapper.from(getSecurityFilterFolder());

        Assertions.assertEquals(ID,response.id());
        Assertions.assertEquals(NAME, response.name());
        Assertions.assertEquals(DESCRIPTION, response.description());
        Assertions.assertNull(response.parentId());


    }


    private SecurityFilterFolder getSecurityFilterFolder() {
        return new SecurityFilterFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }
}
