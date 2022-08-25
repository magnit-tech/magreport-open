package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class DataSourceTypeResponseMapperTest {
    @InjectMocks
    private DataSourceTypeResponseMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";

    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Test
    void from() {

        var response = mapper.from(getDataSourceType());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(CREATE_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());
    }

    @Test
    void fromList() {
        var responses = mapper.from(Collections.singletonList(getDataSourceType()));

        assertFalse(responses.isEmpty());
        var response = responses.get(0);

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(CREATE_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());
    }

    private DataSourceType getDataSourceType() {
        return new DataSourceType()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}
