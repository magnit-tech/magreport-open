package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.service.security.CryptoService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceMapperTest {

    private final Long ID = 1L;
    private final String NAME = "name";
    private final String DESCRIPTION = "description";
    private final String USER_NAME = "user";
    private final String PASSWORD = "pass";
    private final String ENCODE = "encode";
    private final String URL = "URL";

    @InjectMocks
    private DataSourceMapper mapper;

    @Mock
    private CryptoService cryptoService;

    @Test
    void from() {
        when(cryptoService.encode(anyString())).thenReturn(ENCODE);

        DataSource dataSource = mapper.from(getDataSourceAddRequest());

        assertEquals(NAME, dataSource.getName());
        assertEquals(DESCRIPTION, dataSource.getDescription());
        assertNotNull(dataSource.getFolder());
        assertNotNull(dataSource.getType());
        assertEquals(URL, dataSource.getUrl());
        assertEquals(USER_NAME, dataSource.getUserName());
        assertEquals(ENCODE, dataSource.getPassword());

        List<DataSource> dataSources = mapper.from(Collections.singletonList(getDataSourceAddRequest()));

        assertNotEquals(0, dataSources.size());
        dataSource = dataSources.get(0);

        assertEquals(NAME, dataSource.getName());
        assertEquals(DESCRIPTION, dataSource.getDescription());
        assertNotNull(dataSource.getFolder());
        assertNotNull(dataSource.getType());
        assertEquals(URL, dataSource.getUrl());
        assertEquals(USER_NAME, dataSource.getUserName());
        assertEquals(ENCODE, dataSource.getPassword());
    }

    private DataSourceAddRequest getDataSourceAddRequest() {
        return new DataSourceAddRequest()
            .setId(ID)
            .setFolderId(ID)
            .setTypeId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setUrl(URL)
            .setUserName(USER_NAME)
            .setPassword(PASSWORD);
    }
}