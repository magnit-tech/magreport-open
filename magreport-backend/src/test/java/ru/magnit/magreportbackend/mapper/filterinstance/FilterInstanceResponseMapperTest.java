package ru.magnit.magreportbackend.mapper.filterinstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFieldResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTypeResponse;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTypeResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInstanceResponseMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterInstanceResponseMapper mapper;

    @Mock
    private FilterInstanceFieldResponseMapper filterInstanceFieldResponseMapper;

    @Mock
    private FilterTypeResponseMapper filterTypeResponseMapper;

    @Test
    void from() {

        when(filterInstanceFieldResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new FilterInstanceFieldResponse()));
        given(filterTypeResponseMapper.from(ArgumentMatchers.<FilterType>any())).willReturn(new FilterTypeResponse(null, null, null, null, null));

        FilterInstanceResponse response = mapper.from(getFilterInstance());

        assertEquals(ID, response.getId());
        assertEquals(ID, response.getTemplateId());
        assertEquals(ID, response.getDataSetId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFields());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        List<FilterInstanceResponse> responses = mapper.from(Collections.singletonList(getFilterInstance()));
        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(ID, response.getTemplateId());
        assertEquals(ID, response.getDataSetId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getFields());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
    }

    private FilterInstance getFilterInstance() {
        return new FilterInstance()
                .setId(ID)
                .setFolder(new FilterInstanceFolder(ID))
                .setFilterTemplate(new FilterTemplate(ID))
                .setDataSet(new DataSet(ID))
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFields(Collections.singletonList(new FilterInstanceField()))
                .setUser(new User())
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}