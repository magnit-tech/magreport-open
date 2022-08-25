package ru.magnit.magreportbackend.mapper.filterinstance;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInstanceFolderResponseMapperTest {

    private static final long PARENT_ID = 2L;
    private static final Long ID = 1L;
    private static final String NAME = "Test";
    private static final String DESCRIPTION = "description";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterInstanceFolderResponseMapper mapper;

    @Mock
    private FilterInstanceResponseMapper filterInstanceResponseMapper;

    @Test
    void from() {
        when(filterInstanceResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new FilterInstanceResponse()));

        FilterInstanceFolderResponse response = mapper.from(getFolder());

        assertEquals(ID, response.getId());
        assertEquals(PARENT_ID, response.getParentId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<FilterInstanceFolderResponse> responses = mapper.from(Collections.singletonList(getFolder()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFilterInstances());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    private FilterInstanceFolder getFolder() {

        return new FilterInstanceFolder()
                .setId(ID)
                .setParentFolder(new FilterInstanceFolder(PARENT_ID))
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFilterInstances(Collections.singletonList(new FilterInstance()))
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}