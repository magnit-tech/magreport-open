package ru.magnit.magreportbackend.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import ru.magnit.magreportbackend.service.AdminService;


import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static ru.magnit.magreportbackend.controller.AdminController.ADMIN_LOGS;


@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService service;

    @InjectMocks
    private AdminController controller;

    private MockMvc mvc;

    @BeforeEach
    public void initMock() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("TestUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mvc = standaloneSetup(this.controller).build();
    }

    @Test
    void getActiveLog() throws Exception {
        when(service.getMainActiveLog()).thenReturn(getLog());

        mvc
                .perform(post(ADMIN_LOGS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_OCTET_STREAM_VALUE))
                ;

        verify(service).getMainActiveLog();
        verifyNoMoreInteractions(service);
    }

    private byte[] getLog() {
        return new byte[0];
    }
}