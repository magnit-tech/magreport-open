package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.user.DomainGroup;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.RoleDomainGroup;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserRole;
import ru.magnit.magreportbackend.domain.user.UserRoleType;
import ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleDomainGroupSetRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleUsersSetRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleDomainGroupResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleUsersResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.auth.RoleMapper;
import ru.magnit.magreportbackend.mapper.auth.RoleResponseMapper;
import ru.magnit.magreportbackend.mapper.auth.RoleTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.auth.RoleUsersResponseMapper;
import ru.magnit.magreportbackend.mapper.role.FolderNodeResponseRoleTypeMapper;
import ru.magnit.magreportbackend.repository.DomainGroupRepository;
import ru.magnit.magreportbackend.repository.RoleDomainGroupRepository;
import ru.magnit.magreportbackend.repository.RoleRepository;
import ru.magnit.magreportbackend.repository.RoleTypeRepository;
import ru.magnit.magreportbackend.repository.UserRepository;
import ru.magnit.magreportbackend.repository.UserRoleRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleDomainService {

    private final RoleRepository repository;
    private final RoleTypeRepository roleTypeRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final DomainGroupRepository domainGroupRepository;
    private final RoleDomainGroupRepository roleDomainGroupRepository;

    private final RoleMapper roleMapper;
    private final RoleResponseMapper roleResponseMapper;
    private final RoleTypeResponseMapper roleTypeResponseMapper;
    private final RoleUsersResponseMapper roleUsersResponseMapper;
    private final FolderNodeResponseRoleTypeMapper folderNodeResponseRoleTypeMapper;


    @Transactional
    public Long saveRole(RoleAddRequest request) {
        if (repository.existsByName(request.getName())) return null;

        var role = roleMapper.from(request);

        role = repository.save(role);

        return role.getId();
    }

    @Transactional
    public Long updateRole(RoleAddRequest request) {
        if (request.getId() == null || !repository.existsById(request.getId()))
            throw new InvalidParametersException("Role with id:" + request.getId() + " does not exists.");

        var role = roleMapper.from(request);

        role = repository.save(role);

        return role.getId();
    }

    @Transactional
    public RoleResponse getRole(Long roleId) {
        return roleResponseMapper.from(repository.getReferenceById(roleId));
    }

    @Transactional
    public RoleTypeResponse getRoleType(Long roleId) {
        if (roleId == null) {
            return new RoleTypeResponse()
                .setName("root")
                .setChildTypes(roleTypeResponseMapper.shallowMap(roleTypeRepository.findAll()));

        } else {
            return roleTypeResponseMapper.from(roleTypeRepository.getReferenceById(roleId));
        }
    }

    @Transactional
    public void deleteRole(Long roleId) {
        repository.deleteById(roleId);
    }

    @Transactional
    public RoleUsersResponse getRoleUsers(Long roleId) {
        var role = repository.getReferenceById(roleId);

        return roleUsersResponseMapper.from(role);
    }

    @Transactional
    public void setRoleUsers(RoleUsersSetRequest request) {
        var role = repository.getReferenceById(request.getId());
        var users = userRepository.getAllByIdIn(request.getUsers());

        role.setUserRoles(users
            .stream()
            .map(user ->
                new UserRole()
                    .setUser(user)
                    .setRole(role)
                    .setUserRoleType(new UserRoleType(UserRoleTypeEnum.MANUAL)))
            .toList());

        repository.save(role);
    }

    @Transactional
    public void clearUsersFromRole(Long roleId) {
        userRoleRepository.deleteByRoleId(roleId);
    }

    @Transactional
    public boolean isRoleExists(Long id) {
        return repository.existsById(id);
    }

    @Transactional
    public void clearUsersFromRole(Long id, List<Long> users) {
        userRoleRepository.deleteByRoleIdAndUserIdIn(id, users);
    }

    @Transactional
    public void addUserToRole(Long id, List<Long> users) {
        var role = repository.getReferenceById(id);

        var currentUsersInRole = role.getUserRoles()
            .stream()
            .map(UserRole::getUser)
            .map(User::getId)
            .collect(Collectors.toSet());

        users.stream()
            .filter(userId -> !currentUsersInRole.contains(userId))
            .forEach(userId -> {
                var userRole = new UserRole()
                    .setRole(role)
                    .setUser(new User(userId))
                    .setUserRoleType(new UserRoleType(UserRoleTypeEnum.MANUAL));
                userRoleRepository.save(userRole);
            });
    }

    @Transactional
    public void addRoleDomainGroups(RoleDomainGroupSetRequest request) {
        final var role = repository.getReferenceById(request.getId());
        final var domainGroups = domainGroupRepository
            .getAllByNameIn(request.getDomainGroups())
            .stream()
            .collect(Collectors.toMap(DomainGroup::getName, DomainGroup::getId));

        final var existingGroupNames = role.getRoleDomainGroups()
            .stream()
            .map(RoleDomainGroup::getDomainGroup)
            .map(EntityWithName::getName)
            .collect(Collectors.toSet());

        final var newRoleDomainGroups = request.getDomainGroups()
            .stream()
            .filter(o -> !existingGroupNames.contains(o))
            .map(o -> new RoleDomainGroup().setRole(new Role(role.getId())).setDomainGroup(new DomainGroup(domainGroups.get(o))))
            .toList();

        role.getRoleDomainGroups().addAll(newRoleDomainGroups);
        repository.save(role);
    }

    @Transactional
    public void addDomainGroups(List<String> domainGroups) {
        if (domainGroups == null || domainGroups.isEmpty()) return;

        final var existingGroupNames = domainGroupRepository
            .getAllByNameIn(domainGroups)
            .stream()
            .map(EntityWithName::getName)
            .collect(Collectors.toSet());

        final var newDomainGroups = domainGroups
            .stream()
            .filter(o -> !existingGroupNames.contains(o))
            .map(groupName -> new DomainGroup().setName(groupName))
            .toList();

        if (!newDomainGroups.isEmpty())
            domainGroupRepository.saveAll(newDomainGroups);
    }

    @Transactional
    public RoleDomainGroupResponse getRoleDomainGroups(Long roleId) {
        final var role = repository.getReferenceById(roleId);

        final var groupNames = role.getRoleDomainGroups()
            .stream()
            .map(RoleDomainGroup::getDomainGroup)
            .map(EntityWithName::getName)
            .toList();

        return new RoleDomainGroupResponse(
            roleResponseMapper.from(role),
            groupNames
        );
    }

    @Transactional
    public void deleteRoleDomainGroups(RoleDomainGroupSetRequest request) {
        final var role = repository.getReferenceById(request.getId());

        final var deleteList = role.getRoleDomainGroups()
            .stream()
            .filter(o -> request.getDomainGroups().contains(o.getDomainGroup().getName()))
            .map(BaseEntity::getId)
            .toList();

        if (!deleteList.isEmpty())
            roleDomainGroupRepository.delete(deleteList);
    }

    @Transactional
    public List<RoleTypeResponse> getAllRoleTypes() {
        return roleTypeResponseMapper.from(roleTypeRepository.findAll());
    }

    @Transactional
    public List<RoleResponse> getAllRoles() {
        final var roles = repository.findAll();

        return roleResponseMapper.from(roles);
    }

    @Transactional
    public void deleteRole(String roleName) {
        repository.deleteAllByName(roleName);
    }

    @Transactional
    public RoleResponse getRoleByName(String roleName) {
        final var role = repository.getByName(roleName);

        return role == null ? null : roleResponseMapper.from(role);
    }

    @Transactional
    public FolderSearchResponse searchRole(FolderSearchRequest request) {
        var result = new FolderSearchResponse(new LinkedList<>(), new LinkedList<>());

        if (request.getRootFolderId() == null) {
            final var roleTypes = roleTypeRepository.findAll();
            roleTypes.forEach(roleType -> searchRole(roleType, request, result));
            return result;
        } else {
            final var roleType = roleTypeRepository.getReferenceById(request.getRootFolderId());
            return searchRole(roleType, request, result);
        }
    }

    private FolderSearchResponse searchRole(RoleType roleType, FolderSearchRequest request, FolderSearchResponse result) {
        if (roleType == null) return result;

        final var roles = roleType.getRoles()
            .stream()
            .filter(role -> request.getLikenessType().check(request.getSearchString(), role.getName()))
            .toList();

        roles.forEach(role -> result.objects().add(mapRoles(roleType, role)));

        if (request.getLikenessType().check(request.getSearchString(), roleType.getName()))
            result.folders().add(mapFolder(roleType));

        return result;
    }

    private RoleSearchResultResponse mapRoles(RoleType path, Role role) {
        return new RoleSearchResultResponse(Collections.singletonList(folderNodeResponseRoleTypeMapper.from(path)), roleResponseMapper.from(role));

    }

    private FolderSearchResultResponse mapFolder(RoleType path) {
        return new FolderSearchResultResponse(
            Collections.singletonList(folderNodeResponseRoleTypeMapper.from(path)), folderNodeResponseRoleTypeMapper.from(path));
    }

}
