package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataSetFolderResponseMapperTest {

    private static final long PARENT_ID = 2L;
    private static final long ID = 1L;
    private static final String NAME = "Test folder";
    private static final String DESCRIPTION = "Folder description";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private DataSetFolderResponseMapper mapper;

    @Mock
    private DataSetResponseMapper dataSetResponseMapper;

    @Test
    void from() {
        when(dataSetResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSetResponse()));

        DataSetFolderResponse response = mapper.from(getFolder());

        assertEquals(ID, response.getId());
        assertEquals(PARENT_ID, response.getParentId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertNotNull(response.getDataSets());

        List<DataSetFolderResponse> responses = mapper.from(Collections.singletonList(getFolder()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertNotNull(response.getDataSets());
    }

    @Test
    void shallowMap() {
        var response = mapper.shallowMap(getFolder());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertNotNull(response.getDataSets());
    }

    private DataSetFolder getFolder() {

        return new DataSetFolder()
                .setId(ID)
                .setParentFolder(new DataSetFolder(PARENT_ID))
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}