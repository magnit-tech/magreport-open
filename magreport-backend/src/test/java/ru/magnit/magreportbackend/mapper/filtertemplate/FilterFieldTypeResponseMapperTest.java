package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterFieldTypeResponseMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String DESCRIPTION = "description";
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();

    @InjectMocks
    private FilterFieldTypeResponseMapper mapper;

    @Test
    void from() {

        final var source = spy(getFilterFieldType());

        final var result = mapper.from(source);

        assertEquals(ID, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(CREATED_DATE, result.created());
        assertEquals(MODIFIED_DATE, result.modified());

        verify(source).getId();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();
        verifyNoMoreInteractions(source);
    }

    private FilterFieldType getFilterFieldType() {
        return new FilterFieldType()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATED_DATE)
                .setModifiedDateTime(MODIFIED_DATE);
    }
}