package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FilterTemplateFolderResponseMapperTest {

    private static final long PARENT_ID = 2L;
    private static final long ID = 1L;
    private static final String NAME = "Test";
    private static final String DESCRIPTION = "description";
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterTemplateFolderResponseMapper mapper;

    @Mock
    private FilterTemplateResponseMapper filterTemplateResponseMapper;

    @Test
    void from() {
        when(filterTemplateResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new FilterTemplateResponse()));

        FilterTemplateFolderResponse response = mapper.from(getFolder());

        assertEquals(ID, response.getId());
        assertEquals(PARENT_ID, response.getParentId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<FilterTemplateFolderResponse> responses = mapper.from(Collections.singletonList(getFolder()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    @Test
    void shallowMap() {

        FilterTemplateFolderResponse response = mapper.shallowMap(getFolder());

        assertEquals(ID, response.getId());
        assertEquals(PARENT_ID, response.getParentId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

    }

    private FilterTemplateFolder getFolder() {

        return new FilterTemplateFolder()
                .setId(ID)
                .setParentFolder(new FilterTemplateFolder(PARENT_ID))
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFilterTemplates(Collections.emptyList())
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}