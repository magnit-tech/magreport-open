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
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityAddRequest;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecurityRequest;
import ru.magnit.magreportbackend.dto.response.asm.AsmSecurityResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.service.ExternalSecurityService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static ru.magnit.magreportbackend.controller.ExternalSecurityController.AMS_SECURITIES_ADD;
import static ru.magnit.magreportbackend.controller.ExternalSecurityController.AMS_SECURITIES_DELETE;
import static ru.magnit.magreportbackend.controller.ExternalSecurityController.AMS_SECURITIES_EDIT;
import static ru.magnit.magreportbackend.controller.ExternalSecurityController.AMS_SECURITIES_GET;
import static ru.magnit.magreportbackend.controller.ExternalSecurityController.AMS_SECURITIES_GET_ALL;

@ExtendWith(MockitoExtension.class)
class ExternalSecurityControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "External security";
    private static final String DESCRIPTION = "External security for tests";
    private static final String EDITED_NAME = "Edited External Security";
    private static final String EDITED_DESCRIPTION = "Edited description";
    private static final LocalDateTime CREATED = LocalDateTime.of(2020, 1, 1, 13, 45);
    private static final LocalDateTime MODIFIED = LocalDateTime.of(2020, 1, 1, 13, 45);
    private static final String USERNAME = "user";


    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @Mock
    private ExternalSecurityService service;

    @InjectMocks
    private ExternalSecurityController controller;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("TestUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mvc = standaloneSetup(this.controller).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void addAsmSecurity() throws Exception {
        Long id = 1L;
        AsmSecurityAddRequest request = getAsmSecurityAddRequest();
        when(service.addAsmSecurity(any())).thenReturn(getAsmSecurityResponse(id));

        mvc
                .perform(post(AMS_SECURITIES_ADD)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.name", is(NAME)));

        verify(service).addAsmSecurity(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void editAsmSecurity() throws Exception {
        Long id = 1L;
        AsmSecurityAddRequest request = getAsmSecurityAddRequestForEdit();

        when(service.editAsmSecurity(any())).thenReturn(getAsmSecurityResponseForEdit(id));

        mvc
                .perform(post(AMS_SECURITIES_EDIT)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(EDITED_NAME)))
                .andExpect(jsonPath("$.data.description", is(EDITED_DESCRIPTION)));

        verify(service).editAsmSecurity(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAsmSecurity() throws Exception {
        Long id = 1L;
        AsmSecurityRequest request = getAsmSecurityRequest(id);
        when(service.getAsmSecurity(any())).thenReturn(getAsmSecurityResponse(id));

        mvc
                .perform(post(AMS_SECURITIES_GET)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.name", is(NAME)));

        verify(service).getAsmSecurity(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllAsmSecurity() throws Exception {
        Long id = 1L;
        when(service.getAllAsmSecurity()).thenReturn(Collections.singletonList(getAsmSecurityResponse(id)));

        mvc
                .perform(post(AMS_SECURITIES_GET_ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(service).getAllAsmSecurity();
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteAsmSecurity() throws Exception {
        Long id = 1L;
        AsmSecurityRequest request = getAsmSecurityRequest(id);

        mvc
                .perform(post(AMS_SECURITIES_DELETE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("External security source successfully deleted.")))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(service).deleteAsmSecurity(any());
        verifyNoMoreInteractions(service);
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

    private AsmSecurityRequest getAsmSecurityRequest(Long id) {
        return new AsmSecurityRequest(id);
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