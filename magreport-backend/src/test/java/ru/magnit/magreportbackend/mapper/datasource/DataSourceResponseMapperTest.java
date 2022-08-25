package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceResponseMapperTest {
    private final long ID = 1L;
    private final String NAME = "DataSource";
    private final String DESCRIPTION = "DataSource description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now();
    private final String URL = "url";
    private final String USER = "user";

    @InjectMocks
    private DataSourceResponseMapper mapper;

    @Mock
    private Mapper<DataSourceTypeResponse, DataSourceType> dataSourceTypeResponseMapper;

    @Test
    void from() {
        when(dataSourceTypeResponseMapper.from((DataSourceType) any())).thenReturn(new DataSourceTypeResponse());

        DataSourceResponse response = mapper.from(getDataSource());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(CREATED_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());
        assertEquals(URL, response.url());
        assertEquals(USER, response.userName());
        assertNotNull(response.type());

        List<DataSourceResponse> responses = mapper.from(Collections.singletonList(getDataSource()));

        assertNotEquals(0, responses.size());
        response = responses.get(0);

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(CREATED_TIME, response.created());
        assertEquals(MODIFIED_TIME, response.modified());
        assertEquals(URL, response.url());
        assertEquals(USER, response.userName());
    }

    private DataSource getDataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setId(ID);
        dataSource.setName(NAME);
        dataSource.setDescription(DESCRIPTION);
        dataSource.setCreatedDateTime(CREATED_TIME);
        dataSource.setModifiedDateTime(MODIFIED_TIME);
        dataSource.setUrl(URL);
        dataSource.setUserName(USER);
        dataSource.setUser(new User());
        dataSource.setType(new DataSourceType());

        return dataSource;
    }
}