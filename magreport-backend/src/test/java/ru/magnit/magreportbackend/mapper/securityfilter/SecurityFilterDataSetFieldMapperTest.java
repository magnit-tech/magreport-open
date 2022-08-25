package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.securityfilter.FieldMapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterDataSetFieldMapperTest {

    private static final Long FILTER_INSTANCE_FIELD_ID = 1L;
    private static final Long DATASET_FIELD_ID = 2L;
    @InjectMocks
    private SecurityFilterDataSetFieldMapper mapper;
    @Test
    void from() {

        final var source = spy(getFieldMapping());

        final var result = mapper.from(source);

        assertEquals(DATASET_FIELD_ID, result.getDataSetField().getId());
        assertEquals(FILTER_INSTANCE_FIELD_ID, result.getFilterInstanceField().getId());

        verify(source).getFilterInstanceFieldId();
        verify(source).getDataSetFieldId();

        verifyNoMoreInteractions(source);
    }

    private FieldMapping getFieldMapping() {
        return new FieldMapping()
                .setFilterInstanceFieldId(FILTER_INSTANCE_FIELD_ID)
                .setDataSetFieldId(DATASET_FIELD_ID);
    }
}