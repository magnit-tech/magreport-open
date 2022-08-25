package ru.magnit.magreportbackend.domain.datasource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataSourceTypeTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "type";
    private static final String DESCRIPTION = "type description";
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var type = getType();

        assertEquals(ID, type.getId());
        assertEquals(NAME, type.getName());
        assertEquals(DESCRIPTION, type.getDescription());
        assertEquals(NOW, type.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), type.getModifiedDateTime());

        assertNotNull(type.getDataSources());

        DataSourceType testType = new DataSourceType(-ID);
        assertEquals(-ID, testType.getId());
    }

    private static DataSourceType getType() {
        return new DataSourceType()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setDataSources(Collections.singletonList(new DataSource()));
    }
}