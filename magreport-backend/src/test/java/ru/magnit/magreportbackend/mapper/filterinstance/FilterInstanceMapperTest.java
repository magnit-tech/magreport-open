package ru.magnit.magreportbackend.mapper.filterinstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceFieldAddRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInstanceMapperTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";

    @InjectMocks
    private FilterInstanceMapper mapper;

    @Mock
    private FilterInstanceFieldMapper filterInstanceFieldMapper;

    @Test
    void from() {
        when(filterInstanceFieldMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(new FilterInstanceField()));

        FilterInstance instance = mapper.from(getRequest());

        assertEquals(NAME, instance.getName());
        assertEquals(DESCRIPTION, instance.getDescription());
        assertNotNull(instance.getFolder());
        assertEquals(ID, instance.getFolder().getId());
        assertNotNull(instance.getDataSet());
        assertEquals(ID, instance.getDataSet().getId());
        assertNotNull(instance.getFilterTemplate());
        assertEquals(ID, instance.getFilterTemplate().getId());
        assertNotNull(instance.getFields());

        List<FilterInstance> instances = mapper.from(Collections.singletonList(getRequest()));
        assertNotEquals(0, instances.size());
        instance = instances.get(0);

        assertEquals(NAME, instance.getName());
        assertEquals(DESCRIPTION, instance.getDescription());
        assertNotNull(instance.getFolder());
        assertEquals(ID, instance.getFolder().getId());
        assertNotNull(instance.getDataSet());
        assertEquals(ID, instance.getDataSet().getId());
        assertNotNull(instance.getFilterTemplate());
        assertEquals(ID, instance.getFilterTemplate().getId());
        assertNotNull(instance.getFields());
    }

    private FilterInstanceAddRequest getRequest() {
        return new FilterInstanceAddRequest()
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setFolderId(ID)
            .setDataSetId(ID)
            .setTemplateId(ID)
            .setFields(Collections.singletonList(new FilterInstanceFieldAddRequest()));
    }
}