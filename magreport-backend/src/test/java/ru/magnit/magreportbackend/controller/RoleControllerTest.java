package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.magnit.magreportbackend.dto.request.user.DomainGroupADRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleDomainGroupSetRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleTypeRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleUsersSetRequest;
import ru.magnit.magreportbackend.dto.response.user.RoleDomainGroupResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleUsersResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.RoleService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.magnit.magreportbackend.controller.RoleController.AD_GET_DOMAIN_GROUPS;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_ADD;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_ADD_DOMAIN_GROUPS;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_ADD_USERS;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_DELETE;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_DELETE_DOMAIN_GROUPS;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_DELETE_USERS;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_EDIT;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_GET;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_GET_ALL;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_GET_ALL_TYPES;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_GET_DOMAIN_GROUPS;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_GET_TYPE;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_GET_USERS;
import static ru.magnit.magreportbackend.controller.RoleController.ROLE_SET_USERS;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private static final String USER_NAME = "Test";
    private static final Long PARENT_ID = 10L;
    private static final Long ID = 1L;
    private static final Long TYPE_ID = 5L;
    private static final String NAME = "Name";
    private static final String DESCRIPTION = "Description";
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();


    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private DateTimeFormatter formatter;

    @Mock
    private RoleService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RoleController controller;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
    }

    @Test
    void addRole() throws Exception {
        when(service.addRole(any())).thenReturn(getRoleResponse());

        mockMvc
                .perform(post(ROLE_ADD)
                        .content(mapper.writeValueAsString(getRoleAddRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.modified", is(formatter.format(MODIFIED_DATE))));

        verify(service).addRole(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getRoleType() throws Exception {
        when(service.getRoleType(any())).thenReturn(getRoleTypeResponse());

        mockMvc
                .perform(post(ROLE_GET_TYPE)
                        .content(mapper.writeValueAsString(getRoleTypeRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.parentId", is(PARENT_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.modified", is(formatter.format(MODIFIED_DATE))))
                .andExpect(jsonPath("$.data.roles", hasSize(1)))
                .andExpect(jsonPath("$.data.childTypes", hasSize(1)));

        verify(service).getRoleType(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllRoleTypes() throws Exception {
        when(service.getAllRoleTypes()).thenReturn(Collections.singletonList(getRoleTypeResponse()));

        mockMvc
                .perform(post(ROLE_GET_ALL_TYPES)
                        .content("")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(service).getAllRoleTypes();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getRole() throws Exception {
        when(service.getRole(any())).thenReturn(getRoleResponse());

        mockMvc
                .perform(post(ROLE_GET)
                        .content(mapper.writeValueAsString(getRoleRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.modified", is(formatter.format(MODIFIED_DATE))));

        verify(service).getRole(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllRoles() throws Exception {
        when(service.getAllRoles()).thenReturn(Collections.singletonList(getRoleResponse()));

        mockMvc
                .perform(post(ROLE_GET_ALL)
                        .content("")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(service).getAllRoles();
        verifyNoMoreInteractions(service);
    }

    @Test
    void editRole() throws Exception {
        when(service.editRole(any())).thenReturn(getRoleResponse());

        mockMvc
                .perform(post(ROLE_EDIT)
                        .content(mapper.writeValueAsString(getRoleAddRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.typeId", is(TYPE_ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.created", is(formatter.format(CREATED_DATE))))
                .andExpect(jsonPath("$.data.modified", is(formatter.format(MODIFIED_DATE))));

        verify(service).editRole(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteRole() throws Exception {
        mockMvc
                .perform(post(ROLE_DELETE)
                        .content(mapper.writeValueAsString(getRoleRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Role with id= was successfully deleted.")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).deleteRole(any(RoleRequest.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void getRoleUsers() throws Exception {
        when(service.getRoleUsers(any())).thenReturn(getRoleUsersResponse());

        mockMvc
                .perform(post(ROLE_GET_USERS)
                        .content(mapper.writeValueAsString(getRoleRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.users", hasSize(1)));

        verify(service).getRoleUsers(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void setRoleUsers() throws Exception {
        mockMvc
                .perform(post(ROLE_SET_USERS)
                        .content(mapper.writeValueAsString(getRoleUsersSetRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Role was successfully added to users")))
                .andExpect(jsonPath("$.data", is(nullValue())));

        verify(service).setRoleUsers(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addRoleUsers() throws Exception {
        when(service.addRoleUsers(any())).thenReturn(getRoleUsersResponse());

        mockMvc
                .perform(post(ROLE_ADD_USERS)
                        .content(mapper.writeValueAsString(getRoleRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.users", hasSize(1)));

        verify(service).addRoleUsers(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addRoleDomainGroups() throws Exception {
        when(service.addRoleDomainGroups(any())).thenReturn(getRoleDomainGroupResponse());

        mockMvc
                .perform(post(ROLE_ADD_DOMAIN_GROUPS)
                        .content(mapper.writeValueAsString(getRoleDomainGroupSetRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.role.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.domainGroups", hasSize(1)));

        verify(service).addRoleDomainGroups(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteRoleDomainGroups() throws Exception {
        when(service.deleteRoleDomainGroups(any())).thenReturn(getRoleDomainGroupResponse());

        mockMvc
                .perform(post(ROLE_DELETE_DOMAIN_GROUPS)
                        .content(mapper.writeValueAsString(getRoleDomainGroupSetRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.role.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.domainGroups", hasSize(1)));

        verify(service).deleteRoleDomainGroups(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getRoleDomainGroups() throws Exception {
        when(service.getRoleDomainGroups(any())).thenReturn(getRoleDomainGroupResponse());

        mockMvc
                .perform(post(ROLE_GET_DOMAIN_GROUPS)
                        .content(mapper.writeValueAsString(getRoleRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.role.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.domainGroups", hasSize(1)));

        verify(service).getRoleDomainGroups(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getADDomainGroups() throws Exception {
        when(service.getADDomainGroups(any())).thenReturn(Collections.singletonList("Group"));

        mockMvc
                .perform(post(AD_GET_DOMAIN_GROUPS)
                        .content(mapper.writeValueAsString(getDomainGroupADRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0]", is("Group")));

        verify(service).getADDomainGroups(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteRoleUsers() throws Exception {
        when(service.deleteRoleUsers(any())).thenReturn(getRoleUsersResponse());

        mockMvc
                .perform(post(ROLE_DELETE_USERS)
                        .content(mapper.writeValueAsString(getRoleUsersSetRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.users", hasSize(1)));

        verify(service).deleteRoleUsers(any());
        verifyNoMoreInteractions(service);
    }




    /* helpers */

    private RoleRequest getRoleRequest() {
        return new RoleRequest()
                .setId(ID);
    }
    private RoleAddRequest getRoleAddRequest() {
        return new RoleAddRequest()
                .setId(ID)
                .setTypeId(TYPE_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private RoleResponse getRoleResponse() {
        return new RoleResponse()
                .setId(ID)
                .setTypeId(TYPE_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreated(CREATED_DATE)
                .setModified(MODIFIED_DATE);
    }
    private RoleTypeRequest getRoleTypeRequest() {
        return new RoleTypeRequest()
                .setId(ID);
    }

    private RoleTypeResponse getRoleTypeResponse() {
        return new RoleTypeResponse()
                .setParentId(PARENT_ID)
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreated(CREATED_DATE)
                .setModified(MODIFIED_DATE)
                .setChildTypes(Collections.singletonList(new RoleTypeResponse()))
                .setRoles(Collections.singletonList(getRoleResponse()));
    }
    private RoleUsersResponse getRoleUsersResponse() {
        return new RoleUsersResponse()
                .setId(ID)
                .setUsers(Collections.singletonList(new UserResponse()));
    }

    private RoleUsersSetRequest getRoleUsersSetRequest() {
        return new RoleUsersSetRequest()
                .setId(ID)
                .setUsers(Collections.singletonList(ID));
    }
    private RoleDomainGroupSetRequest getRoleDomainGroupSetRequest() {
        return new RoleDomainGroupSetRequest()
                .setId(ID)
                .setDomainGroups(Collections.singletonList("Group"));
    }

    private RoleDomainGroupResponse getRoleDomainGroupResponse() {
        return new RoleDomainGroupResponse(
                getRoleResponse(),
                Collections.singletonList("Group")
        );
    }

    private DomainGroupADRequest getDomainGroupADRequest() {
        return new DomainGroupADRequest();
    }
}