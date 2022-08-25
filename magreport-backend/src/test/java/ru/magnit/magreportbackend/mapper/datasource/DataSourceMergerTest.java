package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.service.security.CryptoService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceMergerTest {
    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    private DataSourceMerger merger;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String USER = "USER";
    private final static String PASSWORD = "*******";
    private final static String URL = "URL";
    private final static short POOL = 5;
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Test
    void merge() {
        when(cryptoService.encode(anyString())).thenReturn(PASSWORD);

        var response = merger.merge(getDataSource(), getDataSourceAddRequest());

        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(new DataSourceFolder().setId(ID), response.getFolder());
        assertEquals(new DataSourceType().setId(ID), response.getType());
        assertEquals(URL, response.getUrl());
        assertEquals(USER, response.getUserName());
        assertEquals(PASSWORD, response.getPassword());
        assertEquals(POOL, response.getPoolSize());

        verify(cryptoService).encode(anyString());
        verifyNoMoreInteractions(cryptoService);
    }


    private DataSource getDataSource() {
        return new DataSource()
                .setId(ID)
                .setName("")
                .setDescription("")
                .setUserName("")
                .setPassword("")
                .setUrl("")
                .setUser(new User())
                .setFolder(new DataSourceFolder())
                .setType(new DataSourceType().setId(ID))
                .setPoolSize((short) 0)
                .setDataSets(Collections.emptyList())
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private DataSourceAddRequest getDataSourceAddRequest() {
        return new DataSourceAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setUserName(USER)
                .setPassword(PASSWORD)
                .setUrl(URL)
                .setPoolSize(POOL)
                .setFolderId(ID)
                .setTypeId(ID);
    }
}
