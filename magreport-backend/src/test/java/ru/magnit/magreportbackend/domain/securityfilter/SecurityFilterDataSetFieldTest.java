package ru.magnit.magreportbackend.domain.securityfilter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityFilterDataSetFieldTest extends BaseEntityTest {


    private static final long ID = 1L;
    private static final long SECURITY_FILTER_ID = 2L;
    private static final long FILTER_INSTANCE_FIELD_ID = 3L;
    private static final long DATASET_FIELD_ID = 4L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusMinutes(2);

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(6);
    }

    @Test
    void testNoArgsConstructor() {
        var result = getFilter();

        assertEquals(ID, result.getId());
        assertEquals(SECURITY_FILTER_ID, result.getSecurityFilter().getId());
        assertEquals(FILTER_INSTANCE_FIELD_ID, result.getFilterInstanceField().getId());
        assertEquals(DATASET_FIELD_ID, result.getDataSetField().getId());

        assertEquals(CREATED_DATE_TIME, result.getCreatedDateTime());
        assertEquals(MODIFIED_DATE_TIME, result.getModifiedDateTime());

        var testFilter = new SecurityFilter(-ID);
        assertEquals(-ID, testFilter.getId());
    }

    private static SecurityFilterDataSetField getFilter() {

        return new SecurityFilterDataSetField()
                .setId(ID)
                .setSecurityFilter(new SecurityFilter(SECURITY_FILTER_ID))
                .setFilterInstanceField(new FilterInstanceField(FILTER_INSTANCE_FIELD_ID))
                .setDataSetField(new DataSetField(DATASET_FIELD_ID))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}