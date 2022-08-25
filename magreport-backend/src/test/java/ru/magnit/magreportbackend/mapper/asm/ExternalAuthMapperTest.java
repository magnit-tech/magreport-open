package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecuritySourceAddRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAuthMapperTest {

    private static final Long ASM_SECURITY_ID = 1L;
    private static final String ASM_SECURITY_NAME = "Test asm security";
    private static final String ASM_SECURITY_DESCRIPTION = "Test asm security description";

    @Mock
    private ExternalAuthSourceMapper sourceMapper;

    @InjectMocks
    private ExternalAuthMapper mapper;

    @Test
    void from() {
        AsmSecurityAddRequest request = spy(getAsmSecurityAddRequest());

        when(sourceMapper.from(anyList())).thenReturn(Collections.singletonList(mock(ExternalAuthSource.class)));

        final var result = mapper.from(request);

        assertEquals(ASM_SECURITY_NAME, result.getName());
        assertEquals(ASM_SECURITY_DESCRIPTION, result.getDescription());
        assertNotNull(result.getSources());
        assertEquals(1, result.getSources().size());

        result.getSources().forEach(source -> verify(source).setExternalAuth(result));
        result.getSources().forEach(Mockito::verifyNoMoreInteractions);
        verify(request).getId();
        verify(request).getName();
        verify(request).getDescription();
        verify(request).getRoleTypeId();
        verify(request).getSecuritySources();
        verifyNoMoreInteractions(request);
    }

    private AsmSecurityAddRequest getAsmSecurityAddRequest() {
        return new AsmSecurityAddRequest()
                .setName(ASM_SECURITY_NAME)
                .setDescription(ASM_SECURITY_DESCRIPTION)
                .setSecuritySources(Collections.singletonList(new AsmSecuritySourceAddRequest()));
    }
}