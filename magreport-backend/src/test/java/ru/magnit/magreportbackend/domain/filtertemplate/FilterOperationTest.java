package ru.magnit.magreportbackend.domain.filtertemplate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterOperationTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(5);
    }

    @Test
    void testNoArgsConstructor() {
        var operation = getOperation();

        assertEquals(ID, operation.getId());
        assertEquals(NOW, operation.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), operation.getModifiedDateTime());
        assertNotNull(operation.getFilterTemplate());
        assertNotNull(operation.getType());

        var testOperation = new FilterOperation(-ID);
        assertEquals(-ID, testOperation.getId());
    }

    private static FilterOperation getOperation() {
        return new FilterOperation()
            .setId(ID)
            .setCreatedDateTime(NOW)
            .setModifiedDateTime(NOW.plusMinutes(2))
            .setFilterTemplate(new FilterTemplate())
            .setType(new FilterOperationType());
    }
}