package ru.magnit.magreportbackend.mapper.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecuritySourceAddRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ExternalAuthMergerTest {

    private final Long ID = 1L;
    private final String NAME = "ASM Security";
    private final String DESCRIPTION = "Desc";
    private final Long ROLE_TYPE_ID = 1L;

    private final String UPDATED_NAME = "New name";
    private final String UPDATED_DESCRIPTION = "New Desc";
    private final Long UPDATED_ROLE_TYPE_ID = 2L;

    @Mock
    private ExternalAuthSourceMapper sourceMapper;

    @InjectMocks
    private ExternalAuthMerger merger;

    @Test
    void merge() {
        ExternalAuth externalAuth = spy(getExternalAuth());
        AsmSecurityAddRequest request = spy(getAsmSecurityAddRequest());

        when(sourceMapper.from(anyList())).thenReturn(Collections.singletonList(spy(new ExternalAuthSource())));

        ExternalAuth result = merger.merge(externalAuth, request);

        assertEquals(result, externalAuth);
        assertEquals(ID, result.getId());
        assertEquals(UPDATED_NAME, result.getName());
        assertEquals(UPDATED_DESCRIPTION, result.getDescription());
        assertEquals(UPDATED_ROLE_TYPE_ID, result.getRoleType().getId());
        assertNotNull(result.getSources());
        assertEquals(1, result.getSources().size());

        verify(request).getId();
        verify(request).getName();
        verify(request).getDescription();
        verify(request).getRoleTypeId();
        verify(request).getSecuritySources();
        verify(externalAuth).setId(ID);
        verify(externalAuth).setName(UPDATED_NAME);
        verify(externalAuth).setDescription(UPDATED_DESCRIPTION);
        verify(externalAuth).setRoleType(any());
        verify(externalAuth).setSources(anyList());
        verify(sourceMapper).from(anyList());
        externalAuth.getSources().forEach(source -> {
            verify(source).setExternalAuth(externalAuth);
            verifyNoMoreInteractions(source);
        });

        verifyNoMoreInteractions(sourceMapper, request);
    }

    private ExternalAuth getExternalAuth() {
        return new ExternalAuth()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setRoleType(new RoleType(ROLE_TYPE_ID))
                .setSources(Collections.singletonList(new ExternalAuthSource()));
    }

    private AsmSecurityAddRequest getAsmSecurityAddRequest() {
        return new AsmSecurityAddRequest()
                .setId(ID)
                .setName(UPDATED_NAME)
                .setDescription(UPDATED_DESCRIPTION)
                .setRoleTypeId(UPDATED_ROLE_TYPE_ID)
                .setSecuritySources(Collections.singletonList(new AsmSecuritySourceAddRequest()));
    }
}