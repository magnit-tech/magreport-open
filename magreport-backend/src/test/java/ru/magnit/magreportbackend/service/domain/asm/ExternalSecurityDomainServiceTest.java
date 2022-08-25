package ru.magnit.magreportbackend.service.domain.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.mapper.asm.AsmSecurityResponseMapper;
import ru.magnit.magreportbackend.mapper.asm.ExternalAuthMapper;
import ru.magnit.magreportbackend.mapper.asm.ExternalAuthMerger;
import ru.magnit.magreportbackend.repository.ExternalAuthRepository;
import ru.magnit.magreportbackend.service.domain.ExternalSecurityDomainService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalSecurityDomainServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "External Auth";
    private static final String DESCRIPTION = "External Auth for test";
    private static final String EDITED_NAME ="Edited External Auth";
    private static final String EDITED_DESCRIPTION = "Edited Description";
    private static final LocalDateTime CREATED
            = LocalDateTime.of(2020, 1, 1, 12, 34);
    private static final LocalDateTime MODIFIED
            = LocalDateTime.of(2020, 1, 1, 12, 34);
    private static final String USERNAME = "user";

    @Mock
    private ExternalAuthRepository authRepository;


    @InjectMocks
    private ExternalSecurityDomainService domainService;

    @Mock
    private AsmSecurityResponseMapper responseMapper;

    @Mock
    private ExternalAuthMapper authMapper;

    @Mock
    ExternalAuthMerger authMerger;

    @Test
    void addAsmSecurity() {
        Long id = 1L;

        ExternalAuth externalAuth = spy(getExternalAuth(id));
        AsmSecurityAddRequest request = spy(getAsmSecurityAddRequest());

        when(authMapper.from(request)).thenReturn(externalAuth);
        when(authRepository.save(externalAuth)).thenReturn(externalAuth);

                final var result = domainService.addAsmSecurity(new UserView().setId(1L), request);

        assertEquals(id, result);

        verify(authMapper).from(request);
        verify(authRepository).save(externalAuth);
        verify(externalAuth).getId();
    }

    @Test
    void editAsmSecurity() {
        Long id = 1L;

        AsmSecurityAddRequest request = spy(getAsmSecurityAddRequestForEdit());

        ExternalAuth externalAuth = getExternalAuth(id);
        ExternalAuth editedExternalAuth = getEditedExternalAuth(id);
        AsmSecurityResponse response = getAsmSecurityResponseForEdit(id);

        when(authRepository.getReferenceById(id)).thenReturn(externalAuth);
        when(authMerger.merge(externalAuth, request)).thenReturn(editedExternalAuth);
        when(authRepository.saveAndFlush(editedExternalAuth)).thenReturn(editedExternalAuth);
        when(responseMapper.from(any(ExternalAuth.class))).thenReturn(response);

        final var result = domainService.editAsmSecurity(request);

        assertEquals(ID, result.id());
        assertEquals(EDITED_NAME, result.name());
        assertEquals(EDITED_DESCRIPTION, result.description());

        verify(authRepository).getReferenceById(id);
        verify(authRepository).saveAndFlush(editedExternalAuth);
        verify(request).getId();
        verify(authMerger).merge(externalAuth, request);
        verify(responseMapper).from(editedExternalAuth);
        verifyNoMoreInteractions(authRepository, request, authMerger, responseMapper);
    }

    @Test
    void getAsmSecurity() {
        Long id = 1L;

        when(authRepository.getReferenceById(anyLong())).thenReturn(getExternalAuth(id));
        when(responseMapper.from(any(ExternalAuth.class))).thenReturn(getAsmSecurityResponse(id));

        final var result = domainService.getAsmSecurity(id);

        verify(authRepository).getReferenceById(id);
        verify(responseMapper).from(any(ExternalAuth.class));

        assertEquals(id, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(CREATED, result.created());
        assertEquals(MODIFIED, result.modified());
        assertNotNull(result.sources());
    }

    @Test
    void getAllAsmSecurity() {
        Long firstId = 1L;
        Long secondId = 2L;

        final var externalAuthList = Arrays.asList(
                getExternalAuth(firstId), getExternalAuth(secondId)
        );
        final var asmSecurityResponseList = Arrays.asList(
                getAsmSecurityResponse(firstId), getAsmSecurityResponse(secondId)
        );

        when(authRepository.findAll()).thenReturn(externalAuthList);
        when(responseMapper.from(externalAuthList)).thenReturn(asmSecurityResponseList);

        final var result = domainService.getAllAsmSecurity();

        verify(authRepository).findAll();
        verify(responseMapper).from(externalAuthList);

        assertEquals(asmSecurityResponseList.size(), result.size());
    }

    @Test
    void deleteAsmSecurity() {

        Long id = 1L;

        domainService.deleteAsmSecurity(id);
        verify(authRepository).deleteById(id);
    }

    private ExternalAuth getExternalAuth(Long id) {
        return new ExternalAuth(id)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(CREATED)
                .setModifiedDateTime(MODIFIED);
    }

    private ExternalAuth getEditedExternalAuth(Long id) {
        return getExternalAuth(id)
                .setName(EDITED_NAME)
                .setDescription(EDITED_DESCRIPTION);
    }

    private AsmSecurityResponse getAsmSecurityResponse(Long id) {
        return new AsmSecurityResponse(
                id,
                NAME,
                DESCRIPTION,
                Collections.emptyList(),
                "Creator",
                new RoleTypeResponse(),
                CREATED,
                MODIFIED
        );
    }

    private AsmSecurityResponse getAsmSecurityResponseForEdit(Long id) {
        return new AsmSecurityResponse(
                id,
                EDITED_NAME,
                EDITED_DESCRIPTION,
                Collections.emptyList(),
                USERNAME,
                new RoleTypeResponse(),
                CREATED,
                MODIFIED
        );
    }

    private AsmSecurityAddRequest getAsmSecurityAddRequest() {
        return new AsmSecurityAddRequest()
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setRoleTypeId(0L)
                .setSecuritySources(Collections.emptyList());
    }

    private AsmSecurityAddRequest getAsmSecurityAddRequestForEdit() {
        return getAsmSecurityAddRequest()
                .setId(ID)
                .setName(EDITED_NAME)
                .setDescription(EDITED_DESCRIPTION);
    }
}
