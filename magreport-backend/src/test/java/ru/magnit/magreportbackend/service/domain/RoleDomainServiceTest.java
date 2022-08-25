package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.user.DomainGroup;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.RoleDomainGroup;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserRole;
import ru.magnit.magreportbackend.dto.request.filterinstance.LikenessType;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleDomainGroupSetRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleUsersSetRequest;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleDomainServiceTest {

    @Mock
    private RoleRepository repository;

    @Mock
    private RoleTypeRepository roleTypeRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DomainGroupRepository domainGroupRepository;

    @Mock
    private RoleDomainGroupRepository roleDomainGroupRepository;

    @Mock
    private RoleMapper roleMapper;
    @Mock
    private RoleResponseMapper roleResponseMapper;
    @Mock
    private RoleTypeResponseMapper roleTypeResponseMapper;
    @Mock
    private RoleUsersResponseMapper roleUsersResponseMapper;

    @Mock
    private FolderNodeResponseRoleTypeMapper folderNodeResponseRoleTypeMapper;

    @InjectMocks
    private RoleDomainService domainService;

    private final static Long ID = 1L;
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static LocalDateTime NOW = LocalDateTime.now();

    @Test
    void saveRole() {

        when(repository.existsByName(any())).thenReturn(true);
        assertNull(domainService.saveRole(getRoleAddRequest()));

        when(repository.existsByName(any())).thenReturn(false);
        when(roleMapper.from(any(RoleAddRequest.class))).thenReturn(get_role());
        when(repository.save(any())).thenReturn(get_role());

        assertNotNull(domainService.saveRole(getRoleAddRequest()));

        verify(repository, times(2)).existsByName(any());
        verify(roleMapper).from(any(RoleAddRequest.class));
        verify(repository).save(any());

        verifyNoMoreInteractions(repository, roleMapper);
    }

    @Test
    void updateRole() {
        var request = getRoleAddRequest().setId(null);
        assertThrows(InvalidParametersException.class, () -> domainService.updateRole(request));

        when(repository.existsById(any())).thenReturn(true);
        when(roleMapper.from(any(RoleAddRequest.class))).thenReturn(get_role());
        when(repository.save(any())).thenReturn(get_role());

        assertNotNull(domainService.updateRole(getRoleAddRequest()));

        verify(repository).existsById(any());
        verify(roleMapper).from(any(RoleAddRequest.class));
        verify(repository).save(any());
        verifyNoMoreInteractions(repository, roleMapper);
    }

    @Test
    void getRole() {
        when(roleResponseMapper.from(any(Role.class))).thenReturn(getRoleResponse());
        when(repository.getReferenceById(any())).thenReturn(get_role());

        assertNotNull(domainService.getRole(ID));

        verify(roleResponseMapper).from(any(Role.class));
        verify(repository).getReferenceById(any());

        verifyNoMoreInteractions(repository, roleResponseMapper);
    }

    @Test
    void getRoleType() {

        when(roleTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(roleTypeResponseMapper.shallowMap(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getRoleType(null));

        when(roleTypeRepository.getReferenceById(any())).thenReturn(new RoleType());
        when(roleTypeResponseMapper.from(any(RoleType.class))).thenReturn(new RoleTypeResponse());

        assertNotNull(domainService.getRoleType(ID));

        verify(roleTypeRepository).findAll();
        verify(roleTypeRepository).getReferenceById(any());
        verify(roleTypeResponseMapper).shallowMap(anyList());
        verify(roleTypeResponseMapper).from(any(RoleType.class));

        verifyNoMoreInteractions(roleTypeRepository, roleTypeResponseMapper);

    }

    @Test
    void deleteRole() {

        domainService.deleteRole(ID);

        verify(repository).deleteById(any());
        verifyNoMoreInteractions(repository);

        domainService.deleteRole("Role");

        verify(repository).deleteAllByName(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getRoleUsers() {

        when(repository.getReferenceById(any())).thenReturn(get_role());
        when(roleUsersResponseMapper.from(any(Role.class))).thenReturn(new RoleUsersResponse());

        domainService.getRoleUsers(ID);

        verify(repository).getReferenceById(any());
        verify(roleUsersResponseMapper).from(any(Role.class));

        verifyNoMoreInteractions(repository, roleUsersResponseMapper);
    }

    @Test
    void setRoleUsers() {

        when(repository.getReferenceById(any())).thenReturn(get_role());
        when(userRepository.getAllByIdIn(anyList())).thenReturn(Collections.singletonList(new User()));

        domainService.setRoleUsers(getRoleUsersSetRequest());

        verify(repository).getReferenceById(any());
        verify(repository).save(any());
        verify(userRepository).getAllByIdIn(anyList());

        verifyNoMoreInteractions(repository, userRepository);
    }

    @Test
    void clearUsersFromRole() {

        domainService.clearUsersFromRole(ID);

        verify(userRoleRepository).deleteByRoleId(any());
        verifyNoMoreInteractions(userRoleRepository);

        domainService.clearUsersFromRole(ID, Collections.emptyList());

        verify(userRoleRepository).deleteByRoleIdAndUserIdIn(any(), anyList());
        verifyNoMoreInteractions(userRoleRepository);
    }

    @Test
    void isRoleExists() {

        when(repository.existsById(any())).thenReturn(true);

        assertTrue(domainService.isRoleExists(ID));

        verify(repository).existsById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void addUserToRole() {

        when(repository.getReferenceById(any())).thenReturn(get_role());

        domainService.addUserToRole(ID, Collections.singletonList(2L));

        verify(repository).getReferenceById(any());
        verify(userRoleRepository).save(any());
    }

    @Test
    void addRoleDomainGroups() {

        when(repository.getReferenceById(any())).thenReturn(get_role());
        when(domainGroupRepository.getAllByNameIn(anyList())).thenReturn(Collections.singletonList(getDomainGroup()));

        domainService.addRoleDomainGroups(getRoleDomainGroupSetRequest());

        verify(repository).getReferenceById(any());
        verify(domainGroupRepository).getAllByNameIn(anyList());
        verify(repository).save(any());

        verifyNoMoreInteractions(repository,domainGroupRepository);
    }

    @Test
    void addDomainGroups() {
        when(domainGroupRepository.getAllByNameIn(anyList())).thenReturn(Collections.singletonList(getDomainGroup()));

        domainService.addDomainGroups(Collections.singletonList("1"));

        verify(domainGroupRepository).getAllByNameIn(anyList());
        verify(domainGroupRepository).saveAll(any());

        verifyNoMoreInteractions(domainGroupRepository);
    }

    @Test
    void getRoleDomainGroups() {

        when(repository.getReferenceById(any())).thenReturn(get_role());

        assertNotNull(domainService.getRoleDomainGroups(ID));

        verify(repository).getReferenceById(any());
        verify(roleResponseMapper).from(any(Role.class));

        verifyNoMoreInteractions(repository,roleResponseMapper);
    }

    @Test
    void deleteRoleDomainGroups() {
        when(repository.getReferenceById(any())).thenReturn(get_role());

        domainService.deleteRoleDomainGroups(getRoleDomainGroupSetRequest());

        verify(repository).getReferenceById(any());
    }

    @Test
    void getAllRoleTypes() {

        when(roleTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(roleTypeResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getAllRoleTypes());

        verify(roleTypeRepository).findAll();
        verify(roleTypeResponseMapper).from(anyList());

        verifyNoMoreInteractions(roleTypeRepository,roleTypeResponseMapper);
    }

    @Test
    void getAllRoles() {

        when(repository.findAll()).thenReturn(Collections.singletonList(get_role()));
        when(roleResponseMapper.from(anyList())).thenReturn(Collections.singletonList(getRoleResponse()));

        assertNotNull(domainService.getAllRoles());

        verify(repository).findAll();
        verify(roleResponseMapper).from(anyList());

        verifyNoMoreInteractions(repository,roleResponseMapper);
    }

    @Test
    void getRoleByName() {

        when(repository.getByName(any())).thenReturn(get_role());
        when(roleResponseMapper.from(any(Role.class))).thenReturn(getRoleResponse());

        assertNotNull(domainService.getRoleByName(""));

        verify(repository).getByName(any());
        verify(roleResponseMapper).from(any(Role.class));

        verifyNoMoreInteractions(repository,roleResponseMapper);

        Mockito.reset(repository);
        when(repository.getByName(any())).thenReturn(null);

        assertNull(domainService.getRoleByName(""));

        verify(repository).getByName(any());
        verifyNoMoreInteractions(repository,roleResponseMapper);
    }

    @Test
    void searchRole() {

        when(roleTypeRepository.getReferenceById(any())).thenReturn(new RoleType()
                .setName(NAME)
                .setRoles(Collections.singletonList(get_role())));


       assertNotNull(domainService.searchRole(getFolderSearchRequest()));
       assertNotNull(domainService.searchRole(getFolderSearchRequest().setRootFolderId(null)));

       verify(folderNodeResponseRoleTypeMapper,times(3)).from(any(RoleType.class));
       verify(roleTypeRepository).getReferenceById(any());
       verify(roleTypeRepository).findAll();


       verifyNoMoreInteractions(roleTypeRepository,folderNodeResponseRoleTypeMapper);

    }

    private RoleAddRequest getRoleAddRequest() {
        return new RoleAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setTypeId(ID);
    }

    private Role get_role() {
        return new Role()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW)
                .setUserRoles(Collections.singletonList(
                        new UserRole()
                                .setId(ID)
                                .setUser(new User().setId(ID))))
                .setRoleDomainGroups(new ArrayList<>(Collections.singletonList(
                        new RoleDomainGroup()
                                .setDomainGroup(new DomainGroup().setName("group1"))
                )));
    }

    private RoleResponse getRoleResponse() {
        return new RoleResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreated(NOW)
                .setModified(NOW)
                .setTypeId(ID);
    }

    private RoleUsersSetRequest getRoleUsersSetRequest() {
        return new RoleUsersSetRequest()
                .setId(ID)
                .setUsers(Collections.singletonList(ID));

    }

    private DomainGroup getDomainGroup() {
        return new DomainGroup()
                .setId(ID)
                .setName(NAME)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW);
    }

    private RoleDomainGroupSetRequest getRoleDomainGroupSetRequest() {
        return new RoleDomainGroupSetRequest()
                .setId(ID)
                .setDomainGroups(
                        Arrays.asList("group1","group2", "group3"));
    }

    private FolderSearchRequest getFolderSearchRequest() {
        return new FolderSearchRequest()
                .setRootFolderId(0L)
                .setSearchString("")
                .setRecursive(true)
                .setLikenessType(LikenessType.CONTAINS);
    }

}
