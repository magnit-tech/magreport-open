package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRole;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceCheckRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceCloner;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceDependenciesResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderCloner;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderRoleCloner;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceMerger;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceViewMapper;
import ru.magnit.magreportbackend.mapper.datasource.FolderNodeResponseDataSourceFolderMapper;
import ru.magnit.magreportbackend.repository.DataSourceFolderRepository;
import ru.magnit.magreportbackend.repository.DataSourceFolderRoleRepository;
import ru.magnit.magreportbackend.repository.DataSourceRepository;
import ru.magnit.magreportbackend.repository.DataSourceTypeRepository;

import javax.transaction.Transactional;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSourceDomainService {

    private final DataSourceTypeRepository dataSourceTypeRepository;
    private final DataSourceFolderRepository folderRepository;
    private final DataSourceRepository dataSourceRepository;
    private final DataSourceFolderRoleRepository dataSourceFolderRoleRepository;

    private final DataSourceTypeResponseMapper dataSourceTypeResponseMapper;
    private final DataSourceFolderResponseMapper dataSourceFolderResponseMapper;
    private final DataSourceResponseMapper dataSourceResponseMapper;
    private final DataSourceFolderMapper dataSourceFolderMapper;
    private final DataSourceMapper dataSourceMapper;
    private final DataSourceMerger dataSourceMerger;
    private final DataSourceViewMapper dataSourceViewMapper;
    private final DataSourceFolderRolePermissionMapper dataSourceFolderRolePermissionMapper;
    private final DataSourceDependenciesResponseMapper dataSourceDependenciesResponseMapper;
    private final FolderNodeResponseDataSourceFolderMapper folderNodeResponseDataSourceFolderMapper;
    private final DataSourceCloner dataSourceCloner;
    private final DataSourceFolderCloner dataSourceFolderCloner;
    private final DataSourceFolderRoleCloner dataSourceFolderRoleCloner;

    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Transactional
    public DataSourceFolderResponse getFolder(Long id) {

        if (id != null) checkFolderExists(id);

        if (id == null) {
            return new DataSourceFolderResponse()
                    .setName("root")
                    .setChildFolders(dataSourceFolderResponseMapper.shallowMap(folderRepository.getAllByParentFolderIsNull()))
                    .setCreated(LocalDateTime.now())
                    .setModified(LocalDateTime.now());
        } else {
            var folder = folderRepository.getReferenceById(id);

            return dataSourceFolderResponseMapper.from(folder);
        }
    }

    @Transactional
    public DataSourceFolderResponse addFolder(FolderAddRequest request) {
        var folder = dataSourceFolderMapper.from(request);
        folder = folderRepository.saveAndFlush(folder);

        if (folderRepository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return dataSourceFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<DataSourceFolderResponse> getChildFolders(Long parentId) {
        var folders = folderRepository.getAllByParentFolderId(parentId);

        return dataSourceFolderResponseMapper.from(folders);
    }

    @Transactional
    public DataSourceFolderResponse renameFolder(FolderRenameRequest request) {
        checkFolderExists(request.getId());
        var folder = folderRepository.getReferenceById(request.getId());

        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folder = folderRepository.saveAndFlush(folder);

        return dataSourceFolderResponseMapper.from(folder);
    }

    @Transactional
    public void deleteFolder(Long id) {

        checkFolderExists(id);
        checkFolderEmpty(id);

        folderRepository.deleteById(id);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = dataSourceFolderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(DataSourceFolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.DATASOURCE
        )).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new DataSourceFolderRole()
                    .setFolder(new DataSourceFolder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(dataSourceFolderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
            return res;
        }).toList();

        dataSourceFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        dataSourceFolderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds, roleId);
    }

    @Transactional
    public Long addDataSource(UserView currentUser, DataSourceAddRequest request) {

        checkFolderExists(request.getFolderId());

        var dataSource = dataSourceMapper.from(request);
        dataSource.setUser(new User(currentUser.getId()));
        dataSource = dataSourceRepository.save(dataSource);

        return dataSource.getId();
    }

    @Transactional
    public DataSourceResponse editDataSource(DataSourceAddRequest request) {
        checkDataSourceExists(request.getId());
        var dataSource = dataSourceRepository.getReferenceById(request.getId());
        dataSource = dataSourceMerger.merge(dataSource, request);

        dataSource = dataSourceRepository.saveAndFlush(dataSource);

        return dataSourceResponseMapper.from(dataSource);
    }

    @Transactional
    public void deleteDataSource(Long id) {

        checkDataSourceExists(id);

        dataSourceRepository.deleteById(id);
    }

    @Transactional
    public DataSourceResponse getDataSource(Long id) {
        checkDataSourceExists(id);

        var dataSource = dataSourceRepository.getReferenceById(id);

        return dataSourceResponseMapper.from(dataSource);
    }

    @Transactional
    public DataSourceData getDataSourceView(Long id) {
        checkDataSourceExists(id);

        return dataSourceViewMapper.from(dataSourceRepository.getReferenceById(id));
    }

    @Transactional
    public List<DataSourceTypeResponse> getDataSourceTypes() {
        var types = dataSourceTypeRepository.findAll();

        return dataSourceTypeResponseMapper.from(types);
    }

    @Transactional
    public DataSourceDependenciesResponse getDataSourceDependencies(Long id) {
        final var dataSource = dataSourceRepository.getReferenceById(id);

        return dataSourceDependenciesResponseMapper.from(dataSource);
    }

    @Transactional
    public DataSourceFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        final var folder = folderRepository.getReferenceById(request.getId());
        final var parentFolder = request.getParentId() == null ? null : new DataSourceFolder(request.getParentId());
        folder.setParentFolder(parentFolder);

        folderRepository.save(folder);

        if (folderRepository.checkRingPath(request.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid new parent id folder: path is looped");

        return dataSourceFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(folderRepository.getReferenceById(folderId));
    }

    @Transactional
    public void changeDataSourceParentFolder(ChangeParentFolderRequest request) {
        final var dataSources = dataSourceRepository.getAllByIdIn(request.getObjIds());
        final var newFolder = new DataSourceFolder(request.getDestFolderId());
        dataSources.forEach(dataSource -> dataSource.setFolder(newFolder));
        dataSourceRepository.saveAll(dataSources);
    }

    @Transactional
    public List<Long> getFolderIds(List<Long> objIds) {
        return dataSourceRepository.getAllByIdIn(objIds)
                .stream()
                .map(dataSource -> dataSource.getFolder().getId())
                .distinct()
                .toList();
    }

    @Transactional
    public void copyDataSource(ChangeParentFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var folder = new DataSourceFolder(request.getDestFolderId());
        final var dataSources = dataSourceCloner.clone(dataSourceRepository.getAllByIdIn(request.getObjIds()));

        dataSources.forEach(dataSource -> {
            dataSource.setUser(user);
            dataSource.setFolder(folder);
        });

        dataSourceRepository.saveAll(dataSources);
    }

    @Transactional
    public List<Long> copyDataSetFolder(CopyFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var destFolder = request.getDestFolderId() == null ?
                null :
                folderRepository.getReferenceById(request.getDestFolderId());

        var destFolderRoles = destFolder == null ?
                new ArrayList<DataSourceFolderRole>() :
                dataSourceFolderRoleCloner.clone(destFolder.getFolderRoles());

        var folderCopyIds = new ArrayList<Long>();

        request.getFolderIds().forEach(f -> {
            final var originalFolder = folderRepository.getReferenceById(f);
            final DataSourceFolder folderCopy;
            if (request.isInheritParentRights() && request.isInheritRightsRecursive())
                folderCopy = copyFolder(originalFolder, destFolder, user, destFolderRoles);
            else
                folderCopy = copyFolder(originalFolder, destFolder, user, null);

            if (Boolean.TRUE.equals(request.isInheritParentRights())) {
                folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(),destFolderRoles));
                folderCopy.getFolderRoles().forEach(fr -> fr.setFolder(folderCopy));
            }

            Long newFolderId = folderRepository.save(folderCopy).getId();
            folderCopyIds.add(newFolderId);
        });

        return folderCopyIds;
    }

    private DataSourceFolder copyFolder(DataSourceFolder originalFolder, DataSourceFolder parentFolder, User currentUser, List<DataSourceFolderRole> destParentFolderRoles) {

        var folderCopy = dataSourceFolderCloner.clone(originalFolder);

        folderCopy.setParentFolder(parentFolder);
        folderCopy.getDataSources().forEach(d -> d.setUser(currentUser));

        var copyChildFolders = new ArrayList<DataSourceFolder>();
        originalFolder.getChildFolders().forEach(f -> copyChildFolders.add(copyFolder(f, folderCopy, currentUser, destParentFolderRoles)));
        folderCopy.setChildFolders(copyChildFolders);

        if (destParentFolderRoles != null) {
            folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(), destParentFolderRoles));
            folderCopy.getFolderRoles().forEach(f -> f.setFolder(folderCopy));
        }

        return folderCopy;
    }

    private List<DataSourceFolderRole> mergeFolderRoles(List<DataSourceFolderRole> current, List<DataSourceFolderRole> dest) {
        Map<Long, DataSourceFolderRole> folderRoles = new HashMap<>();

        current.forEach(folderRole -> folderRoles.put(folderRole.getRole().getId(), folderRole));

        dest.forEach(folderRole -> {

            if (folderRoles.containsKey(folderRole.getRole().getId())) {
                var currentFolderRole = folderRoles.get(folderRole.getRole().getId());

                var currentPermissionLevel = currentFolderRole.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Comparator.comparingLong(Long::longValue)).orElse(0L);
                var destPermissionLevel = folderRole.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Comparator.comparingLong(Long::longValue)).orElse(0L);

                if (currentPermissionLevel < destPermissionLevel) {
                    currentFolderRole.setPermissions(folderRole.getPermissions());
                    currentFolderRole.getPermissions().forEach(f -> f.setFolderRole(currentFolderRole));
                }

                folderRoles.put(currentFolderRole.getRole().getId(), currentFolderRole);
            } else
                folderRoles.put(folderRole.getRole().getId(), folderRole);

        });

        return new ArrayList<>(folderRoles.values());
    }

    private List<FolderNodeResponse> getPath(DataSourceFolder folder) {
        var result = new LinkedList<FolderNodeResponse>();

        result.add(folderNodeResponseDataSourceFolderMapper.from(folder));

        while (folder.getParentFolder() != null) {
            folder = folder.getParentFolder();
            result.addFirst(folderNodeResponseDataSourceFolderMapper.from(folder));
        }

        return result;
    }

    private void checkDataSourceExists(Long id) {

        if (!isDataSourceExists(id))
            throw new InvalidParametersException("DataSource with id: " + id + " is not exists.");

    }

    private boolean isDataSourceExists(Long id) {

        return dataSourceRepository.existsById(id);
    }

    private void checkFolderEmpty(Long id) {
        if (!isFolderEmpty(id))
            throw new InvalidParametersException("DataSource FOLDER folder with id: " + id + " is not empty.");
    }

    private boolean isFolderEmpty(Long id) {

        return !folderRepository.existsByParentFolderId(id) &&
                !dataSourceRepository.existsByFolderId(id);
    }

    private void checkFolderExists(Long id) {
        if (!isFolderExists(id))
            throw new InvalidParametersException("DataSource FOLDER folder with id: " + id + " does not exists");
    }

    private boolean isFolderExists(Long id) {
        return folderRepository.existsById(id);
    }

    public void checkDataSourceConnectivity(DataSourceCheckRequest request) {
        try (final var ignored = DriverManager.getConnection(request.getUrl(), request.getUserName(), request.getPassword())){
            log.debug("Connection test successful: " + request);
        } catch (SQLException ex){
            throw new InvalidParametersException("Database connection failed.", ex);
        }
    }
}
