package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserInfo;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.UserEditRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.request.user.UserStatusSetRequest;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.exception.InvalidAuthenticationException;
import ru.magnit.magreportbackend.mapper.auth.GrantedAuthorityMapper;
import ru.magnit.magreportbackend.service.domain.LdapService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;
import ru.magnit.magreportbackend.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum.DOMAIN;
import static ru.magnit.magreportbackend.domain.user.UserStatusEnum.ACTIVE;
import static ru.magnit.magreportbackend.domain.user.UserStatusEnum.LOGGED_OFF;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainService userDomainService;
    private final LdapService ldapService;
    private final GrantedAuthorityMapper grantedAuthorityMapper;

    @Value("${magreport.update-user-status.enable}")
    private boolean updateUserInfo;

    @Transactional
    public List<GrantedAuthority> getUserAuthorities(String userName) {
        return grantedAuthorityMapper.from(userDomainService.getUserRoles(userName, null));
    }

    @Transactional
    public List<String> getUserRoles(String userName) {
        return userDomainService.getUserRoles(userName, null)
                .stream()
                .map(RoleView::getName)
                .toList();
    }

    public String getCurrentUserName() {
        return userDomainService.getCurrentUserName();
    }

    public UserView loginUser(String userName, List<String> groups) {
        try {
            final var fullName = ldapService.getUserFullName(userName);
            final var email = ldapService.getUserEmail(userName);
            final var userInfo = new UserInfo()
                    .setLoginName(userName)
                    .setFirstName(StringUtils.getFirstName(fullName))
                    .setPatronymic(StringUtils.getPatronymic(fullName))
                    .setLastName(StringUtils.getLastName(fullName))
                    .setEmail(email);

            var user = userDomainService.getOrCreateUserByName(userInfo);
            checkUserStatus(user);
            var domainRoles = userDomainService.getDomainGroupRoles(groups);
            var currentRoles = userDomainService.getUserRoles(user.getName(), DOMAIN);

            userDomainService.removeDeletedRoles(user, domainRoles, currentRoles);
            userDomainService.addInsertedRoles(user, domainRoles, currentRoles);
            return user;
        } catch (Exception ex) {
            log.error("Error trying to login user: " + userName);
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public List<UserResponse> getAllUsers() {
        return userDomainService.showAllUsers();
    }

    public List<UserResponse> getAllActualUsers() {
        return userDomainService.getNotArchiveUsers();
    }

    public UserStatusEnum getUserStatus(String userName) {

        final var user = userDomainService.getUserByName(userName);

        return user == null ? null : user.getStatus();
    }

    public void logoffUsers(List<String> userNames) {
        final var users = new LinkedList<String>();
        if (userNames == null || userNames.isEmpty())
            users.add(getCurrentUserName());
        else
            users.addAll(userNames);

        users.forEach(user -> userDomainService.setUserStatus(Collections.singletonList(user), LOGGED_OFF));
    }

    public List<String> getAllStatuses() {

        return Arrays.stream(UserStatusEnum.values()).map(Enum::name).toList();
    }

    public void setUserStatus(UserStatusSetRequest request) {

        userDomainService.setUserStatus(request.getUserNames(), request.getStatus());
    }

    public UserResponse getUserResponse(UserRequest request) {

        return userDomainService.getUserResponse(request.getUserName());
    }

    public void clearRoles(String userName, List<String> roleNames) {

        final var userRolesIds = userDomainService.getUserRolesIds(userName, roleNames);
        userDomainService.deleteUserRoles(userRolesIds);
    }

    public void setUserRoles(String userName, List<String> roleNames) {

        userDomainService.setUserRoles(userName, roleNames);
    }

    public void logoffAllUsers() {
        userDomainService.setAllUsersStatus(LOGGED_OFF);
    }

    public List<UserResponse> getUsersByRole(RoleRequest request) {
        return userDomainService.getActualUsersByRole(request.getId());
    }

    @Scheduled(cron = "${magreport.update-user-status.schedule}")
    public void checkStatusUsers() {
        if (!updateUserInfo) return;

        var users = userDomainService.getNotArchiveUsers();
        List<String> setArchiveStatus = new ArrayList<>();

        var logins = users.stream().map(UserResponse::getName).toList();

        var ldapResults = ldapService.getUserInfo(logins);

        users.forEach(user -> {

            if (!ldapResults.containsKey(user.getName())) setArchiveStatus.add(user.getName());
            else {
                var ldapEmail = ldapResults.get(user.getName()).getL();
                var ldapFullName = ldapResults.get(user.getName()).getR();

                updateUserInfo(user, ldapEmail, ldapFullName);
            }
        });

        userDomainService.setUserStatus(setArchiveStatus, UserStatusEnum.ARCHIVE);
    }


    private void updateUserInfo(UserResponse user, String ldapEmail, String ldapFullName) {

        var updateUser = false;

        UserEditRequest request = new UserEditRequest();
        request.setId(user.getId());
        request.setLastName(user.getLastName());
        request.setFirstName(user.getFirstName());
        request.setPatronymic(user.getPatronymic());
        request.setEmail(user.getEmail());
        request.setDescription(user.getDescription());

        ldapFullName = ldapFullName.replace("displayName: ", "");
        var fullUserName = String.join(" ", Arrays.asList(user.getLastName(), user.getFirstName(), user.getPatronymic()));
        if (!ldapFullName.equals(fullUserName) && !ldapFullName.isBlank()) {
            var newFullName = ldapFullName.split(" ");
            if (newFullName.length == 3) {
                request.setLastName(newFullName[0]);
                request.setFirstName(newFullName[1]);
                request.setPatronymic(newFullName[2]);
            }
            else {
                request.setLastName(ldapFullName);
                request.setFirstName("");
                request.setPatronymic("");
            }

            updateUser = true;
        }

        if (!ldapEmail.equals(user.getEmail())) {
            request.setEmail(ldapEmail);
            updateUser = true;
        }

        if (updateUser) userDomainService.editUser(request);
    }

    private void checkUserStatus (UserView user) {

        if (user.getStatus() == UserStatusEnum.DISABLED) {
            throw new InvalidAuthenticationException("User is blocked");
        } else if (user.getStatus() == LOGGED_OFF || user.getStatus() == UserStatusEnum.ARCHIVE) {
            userDomainService.setUserStatus(Collections.singletonList(user.getName()), ACTIVE);
        }
    }

}
