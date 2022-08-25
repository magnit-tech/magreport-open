package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetDependenciesResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataSourceDependenciesResponseMapperTest {
    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String USER = "USER";
    private final static String PASSWORD = "*******";
    private final static String URL = "URL";
    private final static short POOL = 5;
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Mock
    private UserResponseMapper userResponseMapper;

    @Mock
    private DataSetDependenciesResponseMapper dataSetDependenciesResponseMapper;

    @InjectMocks
    private DataSourceDependenciesResponseMapper mapper;

    @Test
    void from() {
        when(userResponseMapper.from(any(User.class))).thenReturn(new UserResponse());
        when(dataSetDependenciesResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(getDataSource());

        assertEquals(ID, response.getId());
        assertEquals(DataSourceTypeEnum.TERADATA, response.getType());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(URL, response.getUrl());
        assertEquals(USER, response.getUserName());
        assertEquals(new UserResponse(), response.getCreator());
        assertEquals(0, response.getDataSets().size());
        assertEquals(CREATE_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(userResponseMapper).from(any(User.class));
        verify(dataSetDependenciesResponseMapper).from(anyList());
        verifyNoMoreInteractions(userResponseMapper, dataSetDependenciesResponseMapper);
    }

    @Test
    void fromList() {
        when(userResponseMapper.from(any(User.class))).thenReturn(new UserResponse());
        when(dataSetDependenciesResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        var responses = mapper.from(Collections.singletonList(getDataSource()));

        assertEquals(1, responses.size());

        var response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(DataSourceTypeEnum.TERADATA, response.getType());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(URL, response.getUrl());
        assertEquals(USER, response.getUserName());
        assertEquals(new UserResponse(), response.getCreator());
        assertEquals(0, response.getDataSets().size());
        assertEquals(CREATE_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(userResponseMapper).from(any(User.class));
        verify(dataSetDependenciesResponseMapper).from(anyList());
        verifyNoMoreInteractions(userResponseMapper, dataSetDependenciesResponseMapper);
    }

    private DataSource getDataSource() {
        return new DataSource()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setUserName(USER)
            .setPassword(PASSWORD)
            .setUrl(URL)
            .setUser(new User())
            .setFolder(new DataSourceFolder())
            .setType(new DataSourceType().setId(ID))
            .setPoolSize(POOL)
            .setDataSets(Collections.emptyList())
            .setCreatedDateTime(CREATE_TIME)
            .setModifiedDateTime(MODIFIED_TIME);
    }

}
