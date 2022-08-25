package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterDataSetAddRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterDataSetMapperTest {

    private static final Long DATASET_ID = 1L;

    @InjectMocks
    private SecurityFilterDataSetMapper mapper;

    @Test
    void from() {

        final var source = spy(getSecurityFilterDataSetAddRequest());

        final var result = mapper.from(source);

        assertEquals(DATASET_ID, result.getDataSet().getId());

        verify(source).getDataSetId();
        verifyNoMoreInteractions(source);
    }

    private SecurityFilterDataSetAddRequest getSecurityFilterDataSetAddRequest() {
        return new SecurityFilterDataSetAddRequest()
                .setDataSetId(DATASET_ID);
    }
}