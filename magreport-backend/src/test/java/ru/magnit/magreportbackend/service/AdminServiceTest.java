package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.service.domain.AdminDomainService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminDomainService domainService;

    @InjectMocks
    private AdminService service;

    @Test
    void getActiveLog() {

        when(domainService.getMainLog()).thenReturn(new byte[2]);

        var response = service.getMainActiveLog();

        assertNotNull(response);

        verify(domainService).getMainLog();
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getOlapActiveLog() {

        when(domainService.getOlapLog()).thenReturn(new byte[2]);

        var response = service.getOlapActiveLog();

        assertNotNull(response);

        verify(domainService).getOlapLog();
        verifyNoMoreInteractions(domainService);
    }
}
