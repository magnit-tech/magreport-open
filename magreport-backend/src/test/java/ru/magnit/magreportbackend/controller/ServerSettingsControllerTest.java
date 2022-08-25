package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.magnit.magreportbackend.service.SettingsService;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.magnit.magreportbackend.controller.ServerSettingsController.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class ServerSettingsControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private DateTimeFormatter formatter;

    @InjectMocks
    private ServerSettingsController controller;

    @Mock
    private SettingsService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("somebody");
    }


    @Test
    void getServerDateTime() throws Exception {

        mockMvc
                .perform(post(SERVER_SETTINGS_GET_DATE_TIME)
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", matchesRegex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,9})?([+-]\\d{2}:\\d{2}|Z)")));
        verifyNoInteractions(service);
    }
}
