package ru.magnit.magreportbackend.domain.datasource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataSourceTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "DataSource";
    private static final String DESCRIPTION = "DataSource description";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final short POOL_SIZE = 5;

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(13);
    }

    @Test
    void testNoArgsConstructor() {
        var dataSource = getDataSource();

        assertEquals(ID, dataSource.getId());
        assertEquals(NAME, dataSource.getName());
        assertEquals(DESCRIPTION, dataSource.getDescription());
        assertEquals(NOW, dataSource.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), dataSource.getModifiedDateTime());

        assertNotNull(dataSource.getType());
        assertNotNull(dataSource.getUser());
        assertNotNull(dataSource.getFolder());
        assertEquals(URL, dataSource.getUrl());
        assertEquals(USER, dataSource.getUserName());
        assertEquals(PASSWORD, dataSource.getPassword());
        assertEquals(POOL_SIZE, dataSource.getPoolSize());

        var testDataSource = new DataSource(-ID);
        assertEquals(-ID, testDataSource.getId());
    }

    private static DataSource getDataSource() {
        return new DataSource()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setType(new DataSourceType())
                .setFolder(new DataSourceFolder())
                .setUrl(URL)
                .setUserName(USER)
                .setPassword(PASSWORD)
                .setUser(new User())
                .setPoolSize(POOL_SIZE);
    }
}