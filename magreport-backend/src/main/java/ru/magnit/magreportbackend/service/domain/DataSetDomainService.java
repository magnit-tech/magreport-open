package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRolePermission;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetTypeResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.dataset.DataSetAddRequestMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetCloner;
import ru.magnit.magreportbackend.mapper.dataset.DataSetDataTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetDependenciesResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldAddRequestMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFieldResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderCloner;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderRoleCloner;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetMerger;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.FolderNodeResponseDataSetFolderMapper;
import ru.magnit.magreportbackend.repository.DataSetDataTypeRepository;
import ru.magnit.magreportbackend.repository.DataSetFieldRepository;
import ru.magnit.magreportbackend.repository.DataSetFolderRepository;
import ru.magnit.magreportbackend.repository.DataSetFolderRoleRepository;
import ru.magnit.magreportbackend.repository.DataSetRepository;
import ru.magnit.magreportbackend.repository.DataSetTypeRepository;

import javax.transaction.Transactional;
import java.sql.JDBCType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum.PROCEDURE;

@Service
@RequiredArgsConstructor
public class DataSetDomainService {

    private final DataSetFolderRepository folderRepository;
    private final DataSetRepository dataSetRepository;
    private final DataSetTypeRepository dataSetTypeRepository;
    private final DataSetDataTypeRepository dataSetDataTypeRepository;
    private final DataSetFieldRepository dataSetFieldRepository;
    private final DataSetFolderRoleRepository dataSetFolderRoleRepository;

    private final FolderNodeResponseDataSetFolderMapper folderNodeMapper;
    private final DataSetFolderResponseMapper dataSetFolderResponseMapper;
    private final DataSetFolderMapper dataSetFolderMapper;
    private final DataSetResponseMapper dataSetResponseMapper;
    private final DataSetAddRequestMapper dataSetAddRequestMapper;
    private final DataSetFieldMapper dataSetFieldMapper;
    private final DataSetFieldResponseMapper dataSetFieldResponseMapper;
    private final DataSetFieldAddRequestMapper dataSetFieldAddRequestMapper;
    private final DataSetDataTypeResponseMapper dataSetDataTypeResponseMapper;
    private final DataSetTypeResponseMapper dataSetTypeResponseMapper;
    private final DataSetMerger dataSetMerger;
    private final DataSetMapper dataSetMapper;
    private final DataSetDependenciesResponseMapper dataSetDependenciesResponseMapper;
    private final DataSetCloner dataSetCloner;
    private final DataSetFolderRolePermissionMapper dataSetFolderRolePermissionMapper;
    private final DataSetFolderCloner dataSetFolderCloner;
    private final DataSetFolderRoleCloner dataSetFolderRoleCloner;


    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Transactional
    public DataSetFolderResponse getFolder(Long id) {

        if (id != null) checkFolderExists(id);

        if (id == null) {
            return new DataSetFolderResponse()
                    .setName("root")
                    .setChildFolders(dataSetFolderResponseMapper.shallowMap(folderRepository.getAllByParentFolderIsNull()))
                    .setCreated(LocalDateTime.now())
                    .setModified(LocalDateTime.now());
        } else {
            var folder = folderRepository.getReferenceById(id);

            return dataSetFolderResponseMapper.from(folder);
        }
    }

    @Transactional
    public DataSetFolderResponse addFolder(FolderAddRequest request) {
        var folder = dataSetFolderMapper.from(request);
        folder = folderRepository.saveAndFlush(folder);

        if (folderRepository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return dataSetFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<DataSetFolderResponse> getChildFolders(Long parentId) {

        var folders = folderRepository.getAllByParentFolderId(parentId);

        return dataSetFolderResponseMapper.from(folders);
    }

    @Transactional
    public DataSetFolderResponse renameFolder(FolderRenameRequest request) {

        checkFolderExists(request.getId());

        var folder = folderRepository.getReferenceById(request.getId());

        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folder = folderRepository.saveAndFlush(folder);

        return dataSetFolderResponseMapper.from(folder);
    }

    @Transactional
    public void deleteFolder(Long id) {
        checkFolderExists(id);
        checkFolderEmpty(id);

        folderRepository.deleteById(id);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = dataSetFolderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(DataSetFolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.DATASET
        )).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new DataSetFolderRole()
                    .setFolder(new DataSetFolder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(dataSetFolderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
            return res;
        }).toList();

        dataSetFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        dataSetFolderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds, roleId);
    }

    @Transactional
    public void editDataSet(DataSetAddRequest request) {
        updateDataSet(request);
    }

    @Transactional
    public void deleteDataSet(Long id) {
        checkDataSetExists(id);

        dataSetRepository.deleteById(id);
    }

    @Transactional
    public DataSetResponse getDataSet(Long id) {
        checkDataSetExists(id);

        return dataSetResponseMapper.from(dataSetRepository.getReferenceById(id));
    }

    @Transactional
    public Long createDataSetFromMetaData(UserView currentUser, DataSetCreateFromMetaDataRequest request, List<ObjectFieldResponse> objectFields) {

        checkFolderExists(request.getFolderId());
        return createDataSet(currentUser, request, objectFields);
    }

    private Long createDataSet(UserView currentUser, DataSetCreateFromMetaDataRequest request, List<ObjectFieldResponse> objectFields) {
        var dataSetAddRequest = dataSetAddRequestMapper.from(request);
        dataSetAddRequest.setFields(dataSetFieldAddRequestMapper.from(objectFields));

        var dataSet = dataSetMapper.from(dataSetAddRequest);

        dataSet.setUser(new User(currentUser.getId()));
        dataSet = dataSetRepository.save(dataSet);

        return dataSet.getId();
    }

    @Transactional
    public List<DataSetTypeResponse> getDataSetTypes() {

        var dataSetTypes = dataSetTypeRepository.findAll();

        return dataSetTypeResponseMapper.from(dataSetTypes);
    }

    @Transactional
    public List<DataSetDataTypeResponse> getDataSetDataTypes() {

        var dataSetDataTypes = dataSetDataTypeRepository.findAll();

        return dataSetDataTypeResponseMapper.from(dataSetDataTypes);
    }

    @Transactional
    public List<DataSetFieldResponse> refreshDataSet(DataSetResponse dataSet, List<ObjectFieldResponse> objectFields) {

        final var currentFields = dataSet.getFields().stream().map(DataSetFieldResponse::getName).map(String::toUpperCase).collect(Collectors.toSet());
        final var newFields = objectFields.stream().map(ObjectFieldResponse::getFieldName).map(String::toUpperCase).collect(Collectors.toSet());

        final var deletedFNames = currentFields.stream().filter(fn -> !newFields.contains(fn)).map(String::toUpperCase).collect(Collectors.toSet());
        final var insertedFNames = newFields.stream().filter(fn -> !currentFields.contains(fn)).map(String::toUpperCase).collect(Collectors.toSet());

        var newFieldTypes = objectFields.stream().collect(Collectors.toMap(o -> o.getFieldName().toUpperCase(), o -> DataTypeEnum.valueOf(JDBCType.valueOf(o.getDataType())).name()));

        dataSet.getFields()
                .stream()
                .filter(field -> !field.getTypeName().equals(newFieldTypes.getOrDefault(field.getName(), field.getTypeName())))
                .forEach( f -> {
                    var typeId = DataTypeEnum.getDataTypeId(newFieldTypes.get(f.getName()));
                    dataSetFieldRepository.updateDataTypeField(f.getId(),typeId);
                });


        dataSet
                .getFields()
                .stream()
                .filter(field -> deletedFNames.contains(field.getName().toUpperCase()))
                .forEach(field -> dataSetFieldRepository.markFieldSync(field.getId(), false));

        dataSet
                .getFields()
                .stream()
                .filter(field -> newFields.contains(field.getName().toUpperCase()))
                .forEach(field -> dataSetFieldRepository.markFieldSync(field.getId(), true));

        final var addedFields = objectFields
                .stream()
                .filter(field -> insertedFNames.contains(field.getFieldName().toUpperCase()))
                .map(dataSetFieldAddRequestMapper::from)
                .map(dataSetFieldMapper::from)
                .toList();

        addedFields.forEach(field -> field.setDataSet(new DataSet(dataSet.getId())));

        dataSetFieldRepository.saveAll(addedFields);

        return dataSetFieldResponseMapper.from(addedFields);
    }

    @Transactional
    public List<Long> getUnlinkedInvalidFields(DataSetResponse dataSet, List<ObjectFieldResponse> objectFields) {
        final var currentFields = dataSet.getFields().stream().map(DataSetFieldResponse::getName).map(String::toUpperCase).collect(Collectors.toSet());
        final var newFields = objectFields.stream().map(ObjectFieldResponse::getFieldName).map(String::toUpperCase).collect(Collectors.toSet());

        final var deletedFNames = currentFields.stream().filter(fn -> !newFields.contains(fn)).map(String::toUpperCase).collect(Collectors.toSet());
        final var dependencies = dataSetRepository.getReferenceById(dataSet.getId());

        return dependencies.getFields().stream()
                .filter(field -> field.getReportFields().isEmpty())
                .filter(field -> field.getInstanceFields().isEmpty())
                .filter(field -> field.getFieldMappings().isEmpty())
                .filter(field -> field.getAuthSourceFields().isEmpty())
                .filter(field -> deletedFNames.contains(field.getName().toUpperCase()))
                .map(BaseEntity::getId)
                .toList();
    }

    @Transactional
    public void deleteFields(List<Long> fields) {

        if (!fields.isEmpty()) dataSetFieldRepository.deleteAllByIdIn(fields);
    }

    @Transactional
    public List<Long> getReportIds(Long dataSetId) {

        final var dataSet = dataSetRepository.getReferenceById(dataSetId);
        return dataSet.getReports().stream().map(BaseEntity::getId).toList();
    }

    @Transactional
    public DataSetDependenciesResponse getDataSetDependants(Long dataSetId) {
        final var dataSet = dataSetRepository.getReferenceById(dataSetId);

        return dataSetDependenciesResponseMapper.from(dataSet).setPath(getPath(dataSet.getFolder()));
    }

    @Transactional
    public void actualizeDataSet(Long id) {
        var dataSet = dataSetRepository.getReferenceById(id);

        dataSetRepository.save(dataSet);
    }

    @Transactional
    public Long addDataSetFromProcedure(UserView currentUser, DataSetCreateFromMetaDataRequest request, List<ObjectFieldResponse> objectFields) {
        checkFolderExists(request.getFolderId());
        checkDataSetTypeProcedure(request.getTypeId());

        return createDataSet(currentUser, request, objectFields);
    }

    @Transactional
    public DataSetFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        final var folder = folderRepository.getReferenceById(request.getId());
        final var parentFolder = request.getParentId() == null ? null : new DataSetFolder(request.getParentId());
        folder.setParentFolder(parentFolder);

        folderRepository.save(folder);

        if (folderRepository.checkRingPath(request.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid new parent id folder: path is looped");

        return dataSetFolderResponseMapper.from(folder);
    }

    @Transactional
    public void changeDataSetParentFolder(ChangeParentFolderRequest request) {
        final var dataSets = dataSetRepository.getAllByIdIn(request.getObjIds());
        final var newFolder = new DataSetFolder(request.getDestFolderId());
        dataSets.forEach(dataSet -> dataSet.setFolder(newFolder));
        dataSetRepository.saveAll(dataSets);
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(folderRepository.getReferenceById(folderId));
    }

    @Transactional
    public List<Long> getFolderIds(List<Long> objIds) {
        return dataSetRepository.getAllByIdIn(objIds)
                .stream()
                .map(dataSet -> dataSet.getFolder().getId())
                .distinct()
                .toList();
    }

    @Transactional
    public void copyDataSet(ChangeParentFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var folder = new DataSetFolder(request.getDestFolderId());
        final var dataSets = dataSetCloner.clone(dataSetRepository.getAllByIdIn(request.getObjIds()));

        dataSets.forEach(dataSet -> {
            dataSet.setUser(user);
            dataSet.setFolder(folder);
        });

        dataSetRepository.saveAll(dataSets);
    }

    @Transactional
    public List<Long> copyDataSetFolder(CopyFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var destFolder = request.getDestFolderId() == null ?
                null :
                folderRepository.getReferenceById(request.getDestFolderId());

        var destFolderRoles = destFolder == null ?
                new ArrayList<DataSetFolderRole>() :
                dataSetFolderRoleCloner.clone(destFolder.getFolderRoles());

        var folderCopyIds = new ArrayList<Long>();
        request.getFolderIds().forEach(f -> {
            final var originalFolder = folderRepository.getReferenceById(f);
            final DataSetFolder folderCopy;
            if (request.isInheritParentRights() && request.isInheritRightsRecursive())
                folderCopy = copyFolder(originalFolder, destFolder, user, destFolderRoles);
            else
                folderCopy = copyFolder(originalFolder, destFolder, user, null);

            if (Boolean.TRUE.equals(request.isInheritParentRights())) {
                folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(), destFolderRoles));
                folderCopy.getFolderRoles().forEach(fr -> fr.setFolder(folderCopy));
            }

            Long newFolderId = folderRepository.save(folderCopy).getId();
            folderCopyIds.add(newFolderId);
        });

        return folderCopyIds;
    }

    private DataSetFolder copyFolder(DataSetFolder originalFolder, DataSetFolder parentFolder, User currentUser, List<DataSetFolderRole> destParentFolderRoles) {

        var folderCopy = dataSetFolderCloner.clone(originalFolder);

        folderCopy.setParentFolder(parentFolder);
        folderCopy.getDataSets().forEach(d -> d.setUser(currentUser));

        var copyChildFolders = new ArrayList<DataSetFolder>();
        originalFolder.getChildFolders().forEach(f -> copyChildFolders.add(copyFolder(f, folderCopy, currentUser, destParentFolderRoles)));
        folderCopy.setChildFolders(copyChildFolders);

        if (destParentFolderRoles != null) {
            folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(), destParentFolderRoles));
            folderCopy.getFolderRoles().forEach(f -> f.setFolder(folderCopy));
        }

        return folderCopy;
    }

    private List<DataSetFolderRole> mergeFolderRoles(List<DataSetFolderRole> current, List<DataSetFolderRole> dest) {
        Map<Long, DataSetFolderRole> folderRoles = new HashMap<>();

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

    private void checkFolderExists(Long id) {
        if (!isFolderExists(id))
            throw new InvalidParametersException("DataSet folder with ID:" + id + " does not exists.");
    }

    private boolean isFolderExists(Long id) {
        return folderRepository.existsById(id);
    }

    private void checkFolderEmpty(Long id) {
        if (!isFolderEmpty(id))
            throw new InvalidParametersException("DataSet FOLDER folder with id: " + id + " does not empty.");
    }

    private boolean isFolderEmpty(Long id) {

        return !folderRepository.existsByParentFolderId(id) &&
                !dataSetRepository.existsByFolderId(id);
    }

    private void updateDataSet(DataSetAddRequest request) {
        checkFolderExists(request.getFolderId());

        var dataSet = dataSetRepository.getReferenceById(request.getId());

        checkChangeDataSetType(new DataSetType(request.getTypeId()), dataSet.getType());

        dataSet = dataSetMerger.merge(dataSet, request);

        dataSetRepository.save(dataSet);
    }

    private void checkDataSetExists(Long id) {

        if (!isDataSetExists(id))
            throw new InvalidParametersException("DataSet with id: " + id + " does not exists.");

    }

    private boolean isDataSetExists(Long id) {

        return dataSetRepository.existsById(id);
    }

    private void checkDataSetTypeProcedure(Long id) {
        DataSetTypeEnum current = DataSetTypeEnum.values()[id.intValue()];

        if (!PROCEDURE.equalsIsLong(id))
            throw new InvalidParametersException("DataSet with type: " + current.name() + " does not PROCEDURE");
    }

    private void checkChangeDataSetType(DataSetType request, DataSetType current) {
        if (!request.equals(current))
            throw new InvalidParametersException("DataSetType does not change");
    }

    private List<FolderNodeResponse> getPath(DataSetFolder folder) {
        return folderNodeMapper.from(getPathStream(folder).toList());
    }

    private Stream<DataSetFolder> getPathStream(DataSetFolder folder) {
        return folder == null ?
                Stream.empty() :
                Stream.of(folder).flatMap(node ->
                        Stream.concat(getPathStream(node.getParentFolder()), Stream.of(folder)));
    }
}
