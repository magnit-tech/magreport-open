package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceFolderResponseMapperTest {
    private final long ID = 1L;
    private final long PARENT_ID = 2L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private DataSourceFolderResponseMapper mapper;

    @Mock
    private DataSourceResponseMapper dataSourceResponseMapper;

    @Test
    void from() {
        when(dataSourceResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSourceResponse()));

        DataSourceFolderResponse response = mapper.from(getFolder());

        assertEquals(ID, response.getId());
        assertEquals(PARENT_ID, response.getParentId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertNotNull(response.getDataSources());
    }

    private DataSourceFolder getFolder() {
        DataSourceFolder folder = new DataSourceFolder();
        folder.setId(ID);
        folder.setName(NAME);
        folder.setDescription(DESCRIPTION);
        folder.setCreatedDateTime(CREATED_TIME);
        folder.setModifiedDateTime(MODIFIED_TIME);
        folder.setParentFolder(new DataSourceFolder(PARENT_ID));
        folder.setChildFolders(Collections.emptyList());
        folder.setDataSources(Collections.emptyList());

        return folder;
    }

    @Test
    void testFrom() {
        when(dataSourceResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new DataSourceResponse()));

        List<DataSourceFolderResponse> responses = mapper.from(Collections.singletonList(getFolder()));

        assertNotEquals(0, responses.size());
        DataSourceFolderResponse response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertNotNull(response.getDataSources());
    }

    @Test
    void shallowCopy() {

        DataSourceFolderResponse response = mapper.shallowMap(getFolder());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertNotNull(response.getDataSources());
        assertNotNull(response.getChildFolders());
    }
}