package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldFilterInstanceField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFieldResponse;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFieldResponseMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsmFilterInstanceFieldResponseMapperTest {

    @Mock
    private FilterInstanceFieldResponseMapper filterInstanceFieldMapper;

    @InjectMocks
    private AsmFilterInstanceFieldResponseMapper mapper;

    private final LocalDateTime creationTime = LocalDateTime.now();
    private final LocalDateTime modificationTime = LocalDateTime.now().plusMinutes(1L);
    private static final long FILTER_INSTANCE_ID = 5L;


    @Test
    void from() {
        when(filterInstanceFieldMapper.from(any(FilterInstanceField.class))).thenReturn(getFilterInstanceFieldResponse());

        final var result = mapper.from(getFilterInstanceField());

        assertEquals(FILTER_INSTANCE_ID, result.id());
        assertEquals(creationTime, result.created());
        assertEquals(modificationTime, result.modified());
        assertEquals(getFilterInstanceFieldResponse(), result.filterInstanceField());
    }

    private ExternalAuthSourceFieldFilterInstanceField getFilterInstanceField() {
        return new ExternalAuthSourceFieldFilterInstanceField()
            .setId(FILTER_INSTANCE_ID)
            .setFilterInstanceField(getInstanceField())
            .setSourceField(getSourceField())
            .setCreatedDateTime(creationTime)
            .setModifiedDateTime(modificationTime);
    }

    private ExternalAuthSourceField getSourceField() {
        return new ExternalAuthSourceField();
    }

    private FilterInstanceField getInstanceField() {
        return new FilterInstanceField();
    }

    private FilterInstanceFieldResponse getFilterInstanceFieldResponse() {
        return new FilterInstanceFieldResponse(
            1L,
            FilterFieldTypeEnum.ID_FIELD,
            3L,
            4L,
            0L,
            "Field 1",
            "Field 1 description",
            creationTime,
            modificationTime,
            true
        );
    }
}