package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataSetFolderMapperTest {

    private final Long ID = 1L;
    private final String NAME = "name";
    private final String DESCRIPTION = "description";

    @InjectMocks
    private DataSetFolderMapper mapper;

    @Test
    void from() {
        DataSetFolder folder = mapper.from(getRequest());

        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());
        assertNotNull(folder.getParentFolder());

        List<DataSetFolder> folders = mapper.from(Collections.singletonList(getRequest()));

        assertNotEquals(0, folders.size());
        folder = folders.get(0);

        assertEquals(NAME, folder.getName());
        assertEquals(DESCRIPTION, folder.getDescription());
        assertNotNull(folder.getParentFolder());

    }

    private FolderAddRequest getRequest() {
        return new FolderAddRequest()
            .setParentId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }
}