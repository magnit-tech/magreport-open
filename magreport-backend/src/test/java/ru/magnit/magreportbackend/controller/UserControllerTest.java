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
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.request.user.UserStatusSetRequest;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
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
import static ru.magnit.magreportbackend.controller.UserController.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final String EMAIL = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserController controller;

    @Mock
    private UserService service;

    @BeforeEach
    public void initMock() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("TestUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mvc = standaloneSetup(this.controller).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsers() throws Exception {
        when(service.getAllUsers()).thenReturn(Collections.singletonList(getUserResponse()));

        mvc
            .perform(post(USERS_GET)
                .accept(APPLICATION_JSON_VALUE)
                .content("")
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data", hasSize(1)));

        verify(service).getAllUsers();
        verifyNoMoreInteractions(service);
    }

    @Test
    void logOffUsers() throws Exception {

        mvc
                .perform(post(USERS_LOGOFF)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new ArrayList<String>()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Users successfully logged off.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).logoffUsers(anyList());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllStatuses() throws Exception {

        when(service.getAllStatuses()).thenReturn(Collections.singletonList(NAME));

        mvc
                .perform(post(USERS_GET_ALL_STATUSES)
                        .accept(APPLICATION_JSON_VALUE)
                        .content("")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0]", is(NAME)));


        verify(service).getAllStatuses();
        verifyNoMoreInteractions(service);
    }

    @Test
    void setStatus() throws Exception {

        mvc
                .perform(post(USERS_SET_STATUS)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getUserStatusSetRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Status successfully changed")))
                .andExpect(jsonPath("$.data", is(nullValue())));


        verify(service).setUserStatus(any());
        verifyNoMoreInteractions(service);

    }

    @Test
    void getUserInfo() throws Exception {

        when(service.getUserResponse(any())).thenReturn(getUserResponse());

        mvc
                .perform(post(USERS_GET_ONE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getUserRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)));


        verify(service).getUserResponse(any());
        verifyNoMoreInteractions(service);

    }

    @Test
    void getWhoAmI() throws Exception {

        when(service.getCurrentUserName()).thenReturn(NAME);
        when(service.getUserResponse(any())).thenReturn(getUserResponse());

        mvc
                .perform(post(USERS_WHO_AM_I)
                        .accept(APPLICATION_JSON_VALUE)
                        .content("")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)));


        verify(service).getCurrentUserName();
        verify(service).getUserResponse(any());
        verifyNoMoreInteractions(service);
    }

    private UserResponse getUserResponse() {
        return new UserResponse()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setEmail(EMAIL)
            .setCreated(CREATED_TIME)
            .setModified(MODIFIED_TIME);
    }

    private UserStatusSetRequest getUserStatusSetRequest() {
        return new UserStatusSetRequest()
                .setUserNames(Collections.singletonList(NAME))
                .setStatus(UserStatusEnum.ACTIVE);
    }

    private UserRequest getUserRequest() {
        return new UserRequest()
                .setUserName(NAME);
    }
}