package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecuritySecurityFilterAddRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalAuthSecurityFilterMapperTest {

    private static final Long ID = 2L;
    private static final Long SECURITY_FILTER_ID = 1L;

    @InjectMocks
    private ExternalAuthSecurityFilterMapper mapper;

    @Test
    void from() {
        final var source = spy(getAsmSecuritySecurityFilterAddRequest());
        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(SECURITY_FILTER_ID, result.getSecurityFilter().getId());
        assertNull(result.getSource());

        verify(source).getId();
        verify(source).getSecurityFilterId();
        verifyNoMoreInteractions(source);
    }

    private AsmSecuritySecurityFilterAddRequest getAsmSecuritySecurityFilterAddRequest() {
        return new AsmSecuritySecurityFilterAddRequest()
                .setId(ID)
                .setSecurityFilterId(SECURITY_FILTER_ID);
    }
}