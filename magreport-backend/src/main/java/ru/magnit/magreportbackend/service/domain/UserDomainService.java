package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserRole;
import ru.magnit.magreportbackend.domain.user.UserRoleType;
import ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum;
import ru.magnit.magreportbackend.domain.user.UserStatus;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.inner.UserInfo;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.user.UserEditRequest;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.auth.RoleViewMapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.auth.UserViewMapper;
import ru.magnit.magreportbackend.repository.RoleRepository;
import ru.magnit.magreportbackend.repository.UserRepository;
import ru.magnit.magreportbackend.repository.UserRoleRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum.DOMAIN;
import static ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum.MANUAL;
import static ru.magnit.magreportbackend.domain.user.UserStatusEnum.ACTIVE;
import static ru.magnit.magreportbackend.domain.user.UserStatusEnum.ARCHIVE;


@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserViewMapper userViewMapper;
    private final RoleViewMapper roleViewMapper;
    private final UserResponseMapper userResponseMapper;

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
    public UserView getCurrentUser() {
        return userViewMapper.from(userRepository.getUserByName(getCurrentUserName()));
    }

    @Transactional
    public UserView getOrCreateUserByName(UserInfo userInfo) {
        User user;

        if (!userRepository.existsByName(userInfo.getLoginName())) {
            user = new User();
            user.setName(userInfo.getLoginName());
            user.setUserStatus(new UserStatus(ACTIVE));
        } else {
            user = userRepository.getUserByName(userInfo.getLoginName());
        }
        user.setFirstName(userInfo.getFirstName());
        user.setPatronymic(userInfo.getPatronymic());
        user.setLastName(userInfo.getLastName());
        user.setEmail(userInfo.getEmail());

        user = userRepository.save(user);

        return userViewMapper.from(user);
    }

    @Transactional
    public UserView getUserByName(String userName) {
        final var user = userRepository.getUserByName(userName);
        if (user == null) return null;
        return userViewMapper.from(user);
    }

    @Transactional
    public void addInsertedRoles(UserView user, List<RoleView> domainRoles, List<RoleView> currentRoles) {
        var insertedRoles = new ArrayList<>(domainRoles);
        insertedRoles.removeAll(currentRoles);

        insertedRoles
                .forEach(role -> {
                    var userRoleType = new UserRole()
                            .setUser(new User(user.getId()))
                            .setRole(new Role(role.getId()))
                            .setUserRoleType(new UserRoleType((long) DOMAIN.ordinal()));

                    userRoleRepository.save(userRoleType);
                });
    }

    @Transactional
    public void removeDeletedRoles(UserView user, List<RoleView> domainRoles, List<RoleView> currentRoles) {
        var deletedRoles = new ArrayList<>(currentRoles);
        deletedRoles.removeAll(domainRoles);

        deletedRoles
                .forEach(role ->
                        userRoleRepository.deleteUserRoleByUserIdAndRoleIdAndUserRoleTypeId(
                                user.getId(),
                                role.getId(),
                                (long) DOMAIN.ordinal())
                );
    }

    @Transactional
    public List<RoleView> getDomainGroupRoles(List<String> groups) {
        List<RoleView> result = Collections.emptyList();
        var roles = roleRepository.getRolesByDomainGroupsList(groups);
        if (roles != null) {
            result = roles
                    .stream()
                    .map(roleViewMapper::from)
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Transactional
    public List<RoleView> getUserRoles(String userName, UserRoleTypeEnum userRoleType) {

        List<RoleView> result = Collections.emptyList();
        var user = userRepository.getUserByName(userName);
        if (user != null) {
            result = user.getUserRoles()
                    .stream()
                    .filter(userRole -> userRoleType == null || userRole.getUserRoleType().getId() == userRoleType.ordinal())
                    .map(UserRole::getRole)
                    .map(roleViewMapper::from)
                    .toList();
        }
        return result;
    }

    @Transactional
    public List<RoleView> getCurrentUserRoles(UserRoleTypeEnum userRoleType) {
        return getUserRoles(getCurrentUserName(), userRoleType);
    }

    @Transactional
    public List<UserResponse> showAllUsers() {
        return userResponseMapper.from(userRepository.findAll());
    }

    @Transactional
    public void setUserStatus(List<String> userNames, UserStatusEnum status) {
        userNames.forEach(userName -> {
            final var user = userRepository.getUserByName(userName);
            user.setUserStatus(new UserStatus(status));
            userRepository.save(user);
        });
    }

    @Transactional
    public UserResponse getUserResponse(String userName) {
        final var user = userRepository.getUserByName(userName);
        return userResponseMapper.from(user);
    }

    @Transactional
    public void clearRoles(String userName, List<String> roleNames) {
        final var user = userRepository.getUserByName(userName);

        if (user == null) return;

        user.getUserRoles()
                .stream()
                .filter(userRole -> roleNames.contains(userRole.getRole().getName()))
                .forEach(userRole -> userRoleRepository.deleteById(userRole.getId()));
    }

    @Transactional
    public void setUserRoles(String userName, List<String> roleNames) {
        final var roles = roleRepository.findAllByNameIn(roleNames);
        final var user = userRepository.getUserByName(userName);

        roles.forEach(role -> {
            var userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRole.setUserRoleType(new UserRoleType(MANUAL));
            userRoleRepository.save(userRole);
        });
    }

    @Transactional
    public List<Long> getUserRolesIds(String userName, List<String> roleNames) {
        final var user = userRepository.getUserByName(userName);

        if (user == null) return Collections.emptyList();

        return user.getUserRoles()
                .stream()
                .filter(userRole -> roleNames.contains(userRole.getRole().getName()))
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserRoles(List<Long> userRolesIds) {
        if (userRolesIds == null || userRolesIds.isEmpty()) return;
        userRoleRepository.deleteAllByIdIn(userRolesIds);
    }

    @Transactional
    public void setAllUsersStatus(UserStatusEnum userStatus) {
        userRepository.setStatusToAllUsers(new UserStatus(userStatus.getId()));
    }

    @Transactional
    public List<UserResponse> getActualUsersByRole(Long roleId) {
        var users = userRepository.getUsersByRole(roleId, ARCHIVE.getId());
        return userResponseMapper.from(users);
    }

    @Transactional
    public UserResponse editUser(UserEditRequest request) {

        var user = userRepository.getReferenceById(request.getId());

        user
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setPatronymic(request.getPatronymic())
                .setEmail(request.getEmail())
                .setDescription(request.getDescription())
                .setModifiedDateTime(LocalDateTime.now());

        user = userRepository.save(user);
        return userResponseMapper.from(user);
    }

    @Transactional
    public List<UserResponse> getNotArchiveUsers(){
      return  userRepository
                .getUserByStatusIsNotEquals(ARCHIVE.getId())
                .stream()
                .map(userResponseMapper::from)
                .toList();
    }


}
