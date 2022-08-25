package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FolderNodeResponseDataSetFolderMapperTest {

    @InjectMocks
    private FolderNodeResponseDataSetFolderMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";


    @Test
    void from() {

        var response = mapper.from(getDataSetFolder());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertNull(response.parentId());

    }

    private DataSetFolder getDataSetFolder() {
        return new DataSetFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }
}
