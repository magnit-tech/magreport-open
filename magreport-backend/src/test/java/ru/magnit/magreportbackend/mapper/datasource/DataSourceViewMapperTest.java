package ru.magnit.magreportbackend.mapper.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class DataSourceViewMapperTest {
    private final long ID = 1L;
    private final String URL = "url";
    private final String USER = "user";
    private final String PASSWORD = "pass";

    @InjectMocks
    private DataSourceViewMapper mapper;

    @Test
    void from() {
        DataSourceData view = mapper.from(getDataSource());

        assertEquals(ID, view.id());
        assertEquals(URL, view.url());
        assertEquals(USER, view.userName());
        assertEquals(PASSWORD, view.password());

        List<DataSourceData> views = mapper.from(Collections.singletonList(getDataSource()));

        assertNotEquals(0, views.size());
        view = views.get(0);

        assertEquals(ID, view.id());
        assertEquals(URL, view.url());
        assertEquals(USER, view.userName());
        assertEquals(PASSWORD, view.password());
    }

    private DataSource getDataSource() {

        return new DataSource()
                .setId(ID)
                .setType(new DataSourceType(ID))
                .setUrl(URL)
                .setUserName(USER)
                .setPassword(PASSWORD);
    }
}