package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityRequest;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.service.domain.ExternalAuthRoleFilterRefreshService;
import ru.magnit.magreportbackend.service.domain.ExternalAuthRoleRefreshService;
import ru.magnit.magreportbackend.service.domain.ExternalAuthUserRoleRefreshService;
import ru.magnit.magreportbackend.service.domain.ExternalSecurityDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalSecurityServiceTest {

    private static final Long ASM_SECURITY_ID = 1L;
    private static final String ASM_SECURITY_NAME = "ASM Security";
    private static final String ASM_SECURITY_DESCRIPTION = "ASM Security for test";
    private static final LocalDateTime ASM_SECURITY_CREATED_DATETIME
            = LocalDateTime.of(2020, 1, 1, 12, 34);
    private static final LocalDateTime ASM_SECURITY_MODIFIED_DATETIME
            = LocalDateTime.of(2020, 1, 1, 12, 34);


    @Mock
    private ExternalSecurityDomainService domainService;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private ExternalAuthRoleRefreshService roleRefreshService;

    @Mock
    private ExternalAuthUserRoleRefreshService userRoleRefreshService;

    @Mock
    private ExternalAuthRoleFilterRefreshService roleFilterRefreshService;


    @InjectMocks
    private ExternalSecurityService service;

    @Test
    void addAsmSecurity() {
        Long id = 1L;

        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(1L));
        when(domainService.addAsmSecurity(any(), any())).thenReturn(id);
        when(domainService.getAsmSecurity(id)).thenReturn(getAsmSecurityResponse(id));

        final var result = service.addAsmSecurity(getAsmSecurityAddRequest());

        assertEquals(id, result.id());
        assertEquals(ASM_SECURITY_NAME, result.name());
        assertEquals(ASM_SECURITY_DESCRIPTION, result.description());
    }

    @Test
    void editAsmSecurity() {
        Long id = 1L;

        AsmSecurityAddRequest request = spy(getAsmSecurityAddRequest());
        AsmSecurityResponse response = getAsmSecurityResponse(id);

        when(domainService.getAsmSecurity(id)).thenReturn(response);

        AsmSecurityResponse result = service.editAsmSecurity(request);

        assertEquals(ASM_SECURITY_ID, result.id());
        assertEquals(ASM_SECURITY_NAME, result.name());
        assertEquals(ASM_SECURITY_DESCRIPTION, result.description());
        assertEquals(ASM_SECURITY_CREATED_DATETIME, result.created());
        assertEquals(ASM_SECURITY_MODIFIED_DATETIME, result.modified());

        verify(domainService).editAsmSecurity(request);
        verify(domainService).getAsmSecurity(id);
        verify(request).getId();
        verifyNoMoreInteractions(domainService, request);
    }

    @Test
    void getAsmSecurity() {
        Long id = 1L;

        when(domainService.getAsmSecurity(id)).thenReturn(getAsmSecurityResponse(id));

        final var result = service.getAsmSecurity(getAsmSecurityRequest(id));
        assertEquals(id, result.id());
        assertEquals(ASM_SECURITY_NAME, result.name());
        assertEquals(ASM_SECURITY_DESCRIPTION, result.description());
        assertEquals(ASM_SECURITY_CREATED_DATETIME, result.created());
        assertEquals(ASM_SECURITY_MODIFIED_DATETIME, result.modified());
    }

    @Test
    void getAllAsmSecurity() {
        Long firstId = 1L;
        Long secondId = 2L;

        when(domainService.getAllAsmSecurity()).thenReturn(Arrays.asList(
                getAsmSecurityResponse(firstId), getAsmSecurityResponse(secondId)));

        final var result = service.getAllAsmSecurity();

        assertEquals(2, result.size());
        assertEquals(firstId, result.get(0).id());
        assertEquals(secondId, result.get(1).id());
    }

    @Test
    void deleteAsmSecurity() {
        Long id = 1L;
        service.deleteAsmSecurity(getAsmSecurityRequest(id));

        verify(domainService).deleteAsmSecurity(id);
    }

    @Test
    void refreshAmsFilters() {

        when(domainService.getAllAmsFilters(any())).thenReturn(Collections.singletonList(new ExternalAuthSecurityView()));

        service.refreshAmsFilters(Collections.emptyList());

        verify(roleRefreshService).refreshRoles(any());
        verify(userRoleRefreshService).refreshUserRoles(any());
        verify(roleFilterRefreshService).refreshSecurityFilters(any());
        verify(domainService).getAllAmsFilters(any());

        verifyNoMoreInteractions(roleFilterRefreshService, userRoleRefreshService, roleFilterRefreshService, domainService);
    }

    @Test
    void scheduledRefresher() {

        when(domainService.getAllAsmSecurity()).thenReturn(Collections.singletonList(getAsmSecurityResponse(1L)));
        when(domainService.getAllAmsFilters(any())).thenReturn(Collections.singletonList(new ExternalAuthSecurityView()));

        ReflectionTestUtils.setField(service, "profile", "local");
        ReflectionTestUtils.invokeMethod(service, "scheduledRefresher");


        ReflectionTestUtils.setField(service, "profile", "prod");
        ReflectionTestUtils.invokeMethod(service, "scheduledRefresher");

        verify(domainService).getAllAsmSecurity();
        verify(roleRefreshService).refreshRoles(any());
        verify(userRoleRefreshService).refreshUserRoles(any());
        verify(roleFilterRefreshService).refreshSecurityFilters(any());
        verify(domainService).getAllAmsFilters(any());

        verifyNoMoreInteractions(roleFilterRefreshService, userRoleRefreshService, roleFilterRefreshService, domainService);

    }

    private AsmSecurityRequest getAsmSecurityRequest(Long id) {
        return new AsmSecurityRequest(id);
    }

    private AsmSecurityResponse getAsmSecurityResponse(Long id) {
        return new AsmSecurityResponse(
                id,
                ASM_SECURITY_NAME,
                ASM_SECURITY_DESCRIPTION,
                Collections.emptyList(),
                "Creator",
                new RoleTypeResponse(),
                ASM_SECURITY_CREATED_DATETIME,
                ASM_SECURITY_MODIFIED_DATETIME
        );
    }

    private AsmSecurityAddRequest getAsmSecurityAddRequest() {
        return new AsmSecurityAddRequest(
                ASM_SECURITY_ID,
                ASM_SECURITY_NAME,
                ASM_SECURITY_DESCRIPTION,
                0L,
                Collections.emptyList()
        );
    }
}