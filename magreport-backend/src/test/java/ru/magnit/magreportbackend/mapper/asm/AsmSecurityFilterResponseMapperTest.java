package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AsmSecurityFilterResponseMapperTest {

    private static final Long ID = 1L;
    private static final SecurityFilter SECURITY_FILTER = new SecurityFilter();
    private static final LocalDateTime CREATED_TIME = LocalDateTime.now().minusDays(5);
    private static final LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Mock
    SecurityFilterResponseMapper securityFilterMapper;

    @InjectMocks
    AsmSecurityFilterResponseMapper mapper;

    @Test
    void from() {
        SecurityFilterResponse securityFilterResponse = any();
        when(securityFilterMapper.from(SECURITY_FILTER)).thenReturn(securityFilterResponse);

        ExternalAuthSecurityFilter source = spy(getExternalAuthSecurityFilter());

        AsmSecurityFilterResponse result = mapper.from(source);

        assertEquals(ID, result.id());
        assertEquals(securityFilterResponse, result.securityFilter());
        assertEquals(CREATED_TIME, result.created());
        assertEquals(MODIFIED_TIME, result.modified());

        verify(securityFilterMapper).from(SECURITY_FILTER);
        verify(source).getId();
        verify(source).getSecurityFilter();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();
        verifyNoMoreInteractions(source, securityFilterMapper);
    }

    private ExternalAuthSecurityFilter getExternalAuthSecurityFilter() {
        return new ExternalAuthSecurityFilter()
                .setId(ID)
                .setCreatedDateTime(CREATED_TIME)
                .setModifiedDateTime(MODIFIED_TIME)
                .setSecurityFilter(SECURITY_FILTER);
    }
}