package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTypeResponse;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class FilterTemplateResponseMapperTest {

    @Mock
    private FilterTypeResponseMapper filterTypeResponseMapper;

    @Mock
    private FilterTemplateFieldResponseMapper filterTemplateFieldResponseMapper;

    @InjectMocks
    private FilterTemplateResponseMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String USER = "USER";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void from() {

        Mockito.when(filterTypeResponseMapper.from(Mockito.any(FilterType.class))).thenReturn(getFilterTypeResponse());
        Mockito.when(filterTemplateFieldResponseMapper.from(Mockito.anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(getFilterTemplate());

        Assertions.assertEquals(ID, response.getId());
        Assertions.assertEquals(NAME, response.getName());
        Assertions.assertEquals(DESCRIPTION, response.getDescription());
        Assertions.assertEquals(getFilterTypeResponse(), response.getType());
        Assertions.assertEquals(USER, response.getUserName());
        Assertions.assertEquals(CREATE_TIME, response.getCreated());
        Assertions.assertEquals(MODIFIED_TIME, response.getModified());

        Assertions.assertTrue(response.getSupportedOperations().isEmpty());
        Assertions.assertTrue(response.getFields().isEmpty());

        Mockito.verifyNoMoreInteractions(filterTypeResponseMapper, filterTemplateFieldResponseMapper);

    }

    private FilterTemplate getFilterTemplate() {
        return new FilterTemplate()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setType(new FilterType())
                .setUser(new User().setName(USER))
                .setFolder(new FilterTemplateFolder())
                .setFields(Collections.emptyList())
                .setFilterInstances(Collections.emptyList())
                .setOperations(Collections.emptyList())
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);

    }

    private FilterTypeResponse getFilterTypeResponse() {
        return new FilterTypeResponse(
                ID,
                NAME,
                DESCRIPTION,
                CREATE_TIME,
                MODIFIED_TIME
        );
    }

}
