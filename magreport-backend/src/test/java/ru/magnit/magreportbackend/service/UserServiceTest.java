package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.request.user.UserStatusSetRequest;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.auth.GrantedAuthorityMapper;
import ru.magnit.magreportbackend.service.domain.LdapService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;
import ru.magnit.magreportbackend.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final String NAME = "Test";
    private final UserStatusEnum STATUS = UserStatusEnum.ACTIVE;
    private final List<String> USER_NAMES = Collections.singletonList(NAME);

    @InjectMocks
    private UserService service;

    @Mock
    private UserDomainService domainService;

    @Mock
    private LdapService ldapService;

    @Mock
    private GrantedAuthorityMapper grantedAuthorityMapper;

    @Test
    void getUserAuthorities() {
        when(domainService.getUserRoles(any(), any())).thenReturn(Collections.singletonList(new RoleView()));
        when(grantedAuthorityMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getGrantedAuthority(new RoleView().setName(NAME))));

        List<GrantedAuthority> grantedAuthorities = service.getUserAuthorities(NAME);
        assertNotNull(grantedAuthorities);
        assertNotEquals(0, grantedAuthorities.size());


        verify(domainService).getUserRoles(any(), any());
        verify(grantedAuthorityMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(grantedAuthorityMapper, domainService);
    }

    public GrantedAuthority getGrantedAuthority(RoleView source) {
        return source::getName;
    }

    @Test
    void getUserRoles() {
        when(domainService.getUserRoles(any(), any())).thenReturn(Collections.singletonList(new RoleView().setName(NAME)));

        List<String> roles = service.getUserRoles(NAME);
        assertNotNull(roles);
        assertNotEquals(0, roles.size());
    }

    @Test
    void getCurrentUserName() {
        when(domainService.getCurrentUserName()).thenReturn(NAME);

        final var result = service.getCurrentUserName();

        assertEquals(NAME, result);

        verify(domainService).getCurrentUserName();
        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void loginUser() {

        when(ldapService.getUserFullName(any())).thenReturn("Ivanov Ivan Ivanovich");
        when(ldapService.getUserEmail(any())).thenReturn("ivanov_ii@magnit.ru");
        when(domainService.getOrCreateUserByName(any())).thenReturn(new UserView().setName("Ivanov_II"));

        service.loginUser(NAME, Collections.emptyList());

        verify(ldapService).getUserFullName(any());
        verify(ldapService).getUserEmail(any());
    }

    @Test
    void getAllUsers() {
        when(domainService.showAllUsers()).thenReturn(Collections.singletonList(new UserResponse()));

        final var result = service.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(domainService).showAllUsers();

        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void getUserStatus() {

        //user exists
        when(domainService.getUserByName(any())).thenReturn(new UserView().setStatus(STATUS));

        var result = service.getUserStatus(NAME);

        assertNotNull(result);
        assertEquals(STATUS, result);

        verify(domainService).getUserByName(any());

        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);

        Mockito.reset(domainService);


        //user not exists
        when(domainService.getUserByName(any())).thenReturn(null);

        result = service.getUserStatus(NAME);

        assertNull(result);

        verify(domainService).getUserByName(any());

        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void logoffUsers() {

        // usernames provided
        List<String> userNames = Collections.singletonList(NAME);
        service.logoffUsers(userNames);

        verify(domainService).setUserStatus(Collections.singletonList(NAME), UserStatusEnum.LOGGED_OFF);

        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);

        Mockito.reset(domainService);

        //usernames is empty or null
        userNames = Collections.emptyList();
        String myUserName = "myUserName";

        when(service.getCurrentUserName()).thenReturn(myUserName);
        service.logoffUsers(userNames);

        verify(domainService).setUserStatus(Collections.singletonList(myUserName), UserStatusEnum.LOGGED_OFF);

        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void getAllStatuses() {

        final var result = service.getAllStatuses();

        assertNotNull(result);
        assertEquals(UserStatusEnum.values().length, result.size());
        assertTrue(result.contains(UserStatusEnum.ACTIVE.name()));
        assertTrue(result.contains(UserStatusEnum.DISABLED.name()));
        assertTrue(result.contains(UserStatusEnum.LOGGED_OFF.name()));

        verifyNoInteractions(domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void setUserStatus() {
        final var request = spy(getUserStatusSetRequest());
        service.setUserStatus(request);

        verify(request).getUserNames();
        verify(request).getStatus();
        verify(domainService).setUserStatus(anyList(), any());

        verifyNoMoreInteractions(request, domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void getUserResponse() {
        final var request = spy(getUserRequest());

        when(domainService.getUserResponse(any())).thenReturn(new UserResponse());

        final var result = service.getUserResponse(request);

        assertNotNull(result);

        verify(request).getUserName();
        verify(domainService).getUserResponse(any());

        verifyNoMoreInteractions(request, domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void clearRoles() {
        service.clearRoles(NAME, Collections.singletonList("role"));

        verify(domainService).getUserRolesIds(any(), anyList());
        verify(domainService).deleteUserRoles(anyList());

        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void setUserRoles() {
        service.setUserRoles(NAME, Collections.singletonList("role"));

        verify(domainService).setUserRoles(any(), anyList());

        verifyNoMoreInteractions(domainService, ldapService, grantedAuthorityMapper);
    }

    @Test
    void logoffAllUsers() {
        service.logoffAllUsers();

        verify(domainService).setAllUsersStatus(UserStatusEnum.LOGGED_OFF);
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getAllActualUsers() {

        when(domainService.getNotArchiveUsers()).thenReturn(Collections.singletonList(new UserResponse()));

        var response = service.getAllActualUsers();

        assertEquals(1, response.size());

        verify(domainService).getNotArchiveUsers();
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getUsersByRole() {

        when(domainService.getActualUsersByRole(any())).thenReturn(Collections.singletonList(new UserResponse()));

        var response = service.getUsersByRole(new RoleRequest().setId(1L));

        assertEquals(1, response.size());

        verify(domainService).getActualUsersByRole(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void checkStatusUsersOff() {

        ReflectionTestUtils.setField(service, "updateUserInfo", false);
        service.checkStatusUsers();
        verifyNoInteractions(domainService, ldapService);
    }

    @Test
    void checkStatusUsersOn() {
        ReflectionTestUtils.setField(service, "updateUserInfo", true);

        when(domainService.getNotArchiveUsers()).thenReturn(Collections.singletonList(new UserResponse().setName("NAME")));
        when(ldapService.getUserInfo(any())).thenReturn(getLdapResponse());

        service.checkStatusUsers();

        verify(domainService).getNotArchiveUsers();
        verify(ldapService).getUserInfo(any());
        verify(domainService).setUserStatus(anyList(), any());
        verify(domainService).editUser(any());
        verifyNoMoreInteractions(domainService, ldapService);

    }

    private UserRequest getUserRequest() {
        return new UserRequest()
                .setUserName(NAME);
    }

    private UserStatusSetRequest getUserStatusSetRequest() {
        return new UserStatusSetRequest()
                .setStatus(STATUS)
                .setUserNames(USER_NAMES);
    }

    private Map<String, Pair<String, String>> getLdapResponse() {

        var response = new HashMap<String, Pair<String, String>>();
        var value = new Pair<>("mail", "AAAAA BBBBB CCCCC");
        response.put("NAME", value);
        return response;
    }
}