package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class FilterTemplateFolderMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";

    @InjectMocks
    private FilterTemplateFolderMapper mapper;

    @Test
    void from() {
        FilterTemplateFolder folder = mapper.from(getRequest());

        assertNotNull(folder.getParentFolder());
        assertEquals(ID, folder.getParentFolder().getId());
        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());

        List<FilterTemplateFolder> folders = mapper.from(Collections.singletonList(getRequest()));
        assertNotEquals(0, folders.size());
        folder = folders.get(0);

        assertNotNull(folder.getParentFolder());
        assertEquals(ID, folder.getParentFolder().getId());
        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());
    }

    private FolderAddRequest getRequest() {
        return new FolderAddRequest()
            .setParentId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}