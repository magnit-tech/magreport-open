package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRole;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRolePermission;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceFieldAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterFieldAddRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceShortResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceValuesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterNodeResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filter.FilterChildNodesRequestDataMerger;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterDataFIMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceCloner;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceDependenciesResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFieldMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderCloner;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderRoleCloner;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceMerger;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceQueryDataMerger;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceShortResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FolderNodeResponseFilterInstanceFolderMapper;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;
import ru.magnit.magreportbackend.repository.DataSetRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFieldRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceRepository;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;
import ru.magnit.magreportbackend.util.Pair;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum.PROCEDURE;

@Service
@RequiredArgsConstructor
public class FilterInstanceDomainService {

    private final FilterInstanceFolderRepository folderRepository;
    private final FilterInstanceFolderRoleRepository filterInstanceFolderRoleRepository;
    private final FilterInstanceRepository filterRepository;
    private final FilterInstanceFieldRepository fieldRepository;
    private final DataSetRepository dataSetRepository;
    private final FilterQueryExecutor queryExecutor;

    private final FilterInstanceFolderResponseMapper filterInstanceFolderResponseMapper;
    private final FilterInstanceFolderMapper filterInstanceFolderMapper;
    private final FilterInstanceMapper filterInstanceMapper;
    private final FilterInstanceResponseMapper filterInstanceResponseMapper;
    private final FilterInstanceMerger filterInstanceMerger;
    private final FilterInstanceQueryDataMerger filterInstanceQueryDataMerger;
    private final FilterDataFIMapper filterDataFIMapper;
    private final FilterChildNodesRequestDataMerger filterChildNodesRequestDataMerger;
    private final FilterInstanceShortResponseMapper filterInstanceShortResponseMapper;
    private final FilterInstanceDependenciesResponseMapper filterInstanceDependenciesResponseMapper;
    private final ReportResponseMapper reportResponseMapper;
    private final SecurityFilterResponseMapper securityFilterResponseMapper;
    private final FolderNodeResponseFilterInstanceFolderMapper folderNodeResponseFilterInstanceFolderMapper;
    private final FilterInstanceFieldMapper filterInstanceFieldMapper;
    private final FilterInstanceCloner filterInstanceCloner;
    private final FilterInstanceFolderRolePermissionMapper filterInstanceFolderRolePermissionMapper;
    private final FilterInstanceFolderCloner filterInstanceFolderCloner;
    private final FilterInstanceFolderRoleCloner filterInstanceFolderRoleCloner;

    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Transactional
    public FilterInstanceFolderResponse getFolder(Long id) {

        if (id != null) checkFolderExists(id);

        if (id == null) {
            return new FilterInstanceFolderResponse()
                    .setName("root")
                    .setChildFolders(filterInstanceFolderResponseMapper.shallowMap(folderRepository.getAllByParentFolderIsNull()))
                    .setCreated(LocalDateTime.now())
                    .setModified(LocalDateTime.now());
        } else {
            var folder = folderRepository.getReferenceById(id);

            return filterInstanceFolderResponseMapper.from(folder);
        }
    }

    @Transactional
    public FilterInstanceFolderResponse addFolder(FolderAddRequest request) {

        var folder = filterInstanceFolderMapper.from(request);
        folder = folderRepository.saveAndFlush(folder);

        if (folderRepository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return filterInstanceFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<FilterInstanceFolderResponse> getChildFolders(Long parentId) {

        var folders = folderRepository.getAllByParentFolderId(parentId);
        return filterInstanceFolderResponseMapper.from(folders);
    }

    @Transactional
    public FilterInstanceFolderResponse renameFolder(FolderRenameRequest request) {

        checkFolderExists(request.getId());

        var folder = folderRepository.getReferenceById(request.getId());

        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folder = folderRepository.saveAndFlush(folder);

        return filterInstanceFolderResponseMapper.from(folder);
    }

    @Transactional
    public void deleteFolder(Long id) {

        checkFolderExists(id);
        checkFolderEmpty(id);

        folderRepository.deleteById(id);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = filterInstanceFolderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(FilterInstanceFolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.FILTER_INSTANCE
        )).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new FilterInstanceFolderRole()
                    .setFolder(new FilterInstanceFolder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(filterInstanceFolderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
            return res;
        }).toList();

        filterInstanceFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        filterInstanceFolderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds, roleId);
    }

    @Transactional
    public Long addFilterInstance(UserView currentUser, FilterInstanceAddRequest request, FilterTemplateResponse filterTemplate) {

        checkCodeUnique(request.getCode());
        checkFolderExists(request.getFolderId());

        request.getFields().forEach(field -> field.setTemplateFieldId(Objects.requireNonNull(filterTemplate.getFields().stream().filter(o -> o.getType() == field.getType()).findFirst().orElse(null)).getId()));

        var filterInstance = filterInstanceMapper.from(request);

        if (request.getDataSetId() != null) {
            final var dataSet = dataSetRepository.getReferenceById(request.getDataSetId());
            filterInstance.setDataSet(dataSet);
        }

        checkDataSetTypeProcedure(filterInstance.getDataSet());
        filterInstance.setUser(new User(currentUser.getId()));
        filterRepository.save(filterInstance);

        return filterInstance.getId();
    }

    @Transactional
    public void deleteFilterInstance(Long id) {

        checkFilterInstanceExists(id);

        filterRepository.deleteById(id);
    }

    @Transactional
    public FilterInstanceResponse getFilterInstance(Long id) {
        return filterInstanceResponseMapper.from(filterRepository.getReferenceById(id));
    }

    @Transactional
    public Long editFilterInstance(FilterInstanceAddRequest request, FilterTemplateResponse filterTemplate) {

        var filterInstance = filterRepository.getReferenceById(request.getId());
        checkDataSetTypeProcedure(filterInstance.getDataSet());
        filterInstance = filterRepository.getReferenceById(request.getId());

        if (!filterInstance.getCode().equals(request.getCode()) || filterInstance.getCode().equals(""))
            checkCodeUnique(request.getCode());
        request.getFields().forEach(field -> field.setTemplateFieldId(Objects.requireNonNull(filterTemplate.getFields().stream().filter(o -> o.getType() == field.getType()).findFirst().orElse(null)).getId()));
        filterInstance.setFields(checkEditFilterInstance(request));
        filterInstanceMerger.merge(filterInstance, request);
        return request.getId();
    }

    @Transactional
    public FilterInstanceValuesResponse getFilterInstanceValues(ListValuesRequest request) {

        final var filterInstance = filterRepository.getReferenceById(request.getFilterId());

        final var requestData = filterInstanceQueryDataMerger.merge(filterInstance, request);
        final var tuples = queryExecutor.getFilterInstanceValuesQuery(requestData);

        return new FilterInstanceValuesResponse(filterInstanceResponseMapper.from(filterInstance), tuples);
    }

    @Transactional
    public FilterInstanceChildNodesResponse getChildNodes(ChildNodesRequest request, List<ReportJobFilterData> effectiveSettings) {

        final var filterInstance = filterRepository.getReferenceById(request.getFilterId());

        final var requestData = filterChildNodesRequestDataMerger.merge(filterDataFIMapper.from(filterInstance), request, effectiveSettings);
        final var childNodes = queryExecutor.getFilterInstanceChildNodes(requestData);
        final var rootNode = new FilterNodeResponse(
                requestData.rootFieldId(),
                requestData.level(),
                null,
                null,
                childNodes);

        return new FilterInstanceChildNodesResponse(filterInstanceResponseMapper.from(filterInstance), requestData.responseFieldId(), rootNode);
    }

    @Transactional
    public FilterGroupAddRequest addMissingFields(FilterGroupAddRequest request) {

        if (request.getFilters() == null) return request;

        request.getFilters().forEach(filter -> {
            final var fieldIds = filter.getFields().stream().map(FilterFieldAddRequest::getFilterInstanceFieldId).collect(Collectors.toSet());
            final var filterInstance = filterRepository.getReferenceById(filter.getFilterInstanceId());
            filterInstance
                    .getFields()
                    .stream()
                    .filter(o -> !fieldIds.contains(o.getId()))
                    .forEach(fiField -> filter.getFields().add(new FilterFieldAddRequest()
                            .setName(fiField.getName())
                            .setDescription(fiField.getDescription())
                            .setFilterInstanceFieldId(fiField.getId())
                            .setOrdinal(0L)));
        });

        request.getChildGroups().forEach(this::addMissingFields);

        return request;
    }

    @Transactional
    public Long getDataSetId(Long filterId) {
        final var filterInstance = filterRepository.getReferenceById(filterId);

        return filterInstance.getDataSet().getId();
    }

    @Transactional
    public FilterInstanceFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        final var folder = folderRepository.getReferenceById(request.getId());
        final var parentFolder = request.getParentId() == null ? null : new FilterInstanceFolder(request.getParentId());
        folder.setParentFolder(parentFolder);

        folderRepository.save(folder);

        if (folderRepository.checkRingPath(request.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid new parent id folder: path is looped");

        return filterInstanceFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<FolderNodeResponse> getPathFilter(Long filterId) {
        return getPath(filterRepository.getReferenceById(filterId).getFolder());
    }

    @Transactional
    public List<FilterInstanceShortResponse> checkFilterExistsForDataset(Long dataSetId) {
        var result = filterRepository.findByDataSetId(dataSetId);
        return result.stream().map(filterInstanceShortResponseMapper::from).toList();
    }

    @Transactional
    public FilterInstanceDependenciesResponse getFilterInstanceDependants(Long filterId) {
        var filter = filterRepository.getReferenceById(filterId);
        var response = filterInstanceDependenciesResponseMapper.from(filter);

        response.setReports(filter.getFilterReports()
                .stream()
                .map(FilterReport::getGroup)
                .map(FilterReportGroup::getReport)
                .distinct()
                .map(reportResponseMapper::from)
                .toList());

        response.setSecurityFilters(filter.getSecurityFilters()
                .stream()
                .map(securityFilterResponseMapper::from)
                .toList());

        return response;
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(folderRepository.getReferenceById(folderId));
    }

    @Transactional
    public List<Long> getFolderIds(List<Long> objIds) {
        return filterRepository.getAllByIdIn(objIds)
                .stream()
                .map(filterInstance -> filterInstance.getFolder().getId())
                .distinct()
                .toList();
    }

    @Transactional
    public void changeFilterInstanceParentFolder(ChangeParentFolderRequest request) {
        final var filterInstances = filterRepository.getAllByIdIn(request.getObjIds());
        final var newFolder = new FilterInstanceFolder(request.getDestFolderId());
        filterInstances.forEach(filterInstance -> filterInstance.setFolder(newFolder));
        filterRepository.saveAll(filterInstances);
    }

    @Transactional
    public void copyFilterInstance(ChangeParentFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var folder = new FilterInstanceFolder(request.getDestFolderId());
        final var filterInstances = filterInstanceCloner.clone(filterRepository.getAllByIdIn(request.getObjIds()));

        filterInstances.forEach(dataSource -> {
            dataSource.setUser(user);
            dataSource.setFolder(folder);
        });

        filterRepository.saveAll(filterInstances);
    }

    @Transactional
    public List<Long> copyFilterInstanceFolder(CopyFolderRequest request, UserView currentUser) {
        final var user = new User(currentUser.getId());
        final var destFolder = request.getDestFolderId() == null ?
                null :
                folderRepository.getReferenceById(request.getDestFolderId());

        var destFolderRoles = destFolder == null ?
                new ArrayList<FilterInstanceFolderRole>() :
                filterInstanceFolderRoleCloner.clone(destFolder.getFolderRoles());

        var folderCopyIds = new ArrayList<Long>();

        request.getFolderIds().forEach(f -> {
            final var originalFolder = folderRepository.getReferenceById(f);
            final FilterInstanceFolder folderCopy;
            if (request.isInheritParentRights() && request.isInheritRightsRecursive())
                folderCopy = copyFolder(originalFolder, destFolder, user, destFolderRoles);
            else
                folderCopy = copyFolder(originalFolder, destFolder, user, null);

            if (Boolean.TRUE.equals(request.isInheritParentRights())) {
                folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(),destFolderRoles));
                folderCopy.getFolderRoles().forEach(fr -> fr.setFolder(folderCopy));
            }

            Long newFolderId =  folderRepository.save(folderCopy).getId();
            folderCopyIds.add(newFolderId);
        });
        return folderCopyIds;
    }

    private FilterInstanceFolder copyFolder(FilterInstanceFolder originalFolder, FilterInstanceFolder parentFolder, User currentUser, List<FilterInstanceFolderRole> destParentFolderRoles) {

        var folderCopy = filterInstanceFolderCloner.clone(originalFolder);

        folderCopy.setParentFolder(parentFolder);
        folderCopy.getFilterInstances().forEach(d -> d.setUser(currentUser));

        var copyChildFolders = new ArrayList<FilterInstanceFolder>();
        originalFolder.getChildFolders().forEach(f -> copyChildFolders.add(copyFolder(f, folderCopy, currentUser, destParentFolderRoles)));
        folderCopy.setChildFolders(copyChildFolders);

        if (destParentFolderRoles != null) {
            folderCopy.setFolderRoles(mergeFolderRoles(folderCopy.getFolderRoles(), destParentFolderRoles));
            folderCopy.getFolderRoles().forEach(f -> f.setFolder(folderCopy));
        }

        return folderCopy;
    }

    private List<FilterInstanceFolderRole> mergeFolderRoles(List<FilterInstanceFolderRole> current, List<FilterInstanceFolderRole> dest) {
        Map<Long, FilterInstanceFolderRole> folderRoles = new HashMap<>();

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


    private List<FolderNodeResponse> getPath(FilterInstanceFolder folder) {

        var result = new LinkedList<FolderNodeResponse>();
        result.add(folderNodeResponseFilterInstanceFolderMapper.from(folder));

        while (folder.getParentFolder() != null) {
            folder = folder.getParentFolder();
            result.addFirst(folderNodeResponseFilterInstanceFolderMapper.from(folder));
        }

        return result;
    }

    private void checkFolderExists(Long id) {

        if (!isFolderExists(id))
            throw new InvalidParametersException("FilterInstance folder with id:" + id + " does not exists.");
    }

    private boolean isFolderExists(Long id) {

        return folderRepository.existsById(id);
    }

    private void checkFolderEmpty(Long id) {
        if (!isFolderEmpty(id))
            throw new InvalidParametersException("Folder with id: " + id + " is not empty.");
    }

    private boolean isFolderEmpty(Long id) {

        return !folderRepository.existsByParentFolderId(id) &&
                !filterRepository.existsByFolderId(id);
    }

    private void checkFilterInstanceExists(Long id) {
        if (!isFilterInstanceExists(id))
            throw new InvalidParametersException("FilterInstance with id:" + id + " does not exists.");
    }

    private boolean isFilterInstanceExists(Long id) {
        return filterRepository.existsById(id);
    }

    private void checkDataSetTypeProcedure(DataSet dataSet) {
        if (dataSet == null) return;

        if (PROCEDURE.equalsIsLong(dataSet.getType().getId()))
            throw new InvalidParametersException("FilterInstance with DataSetType: PROCEDURE");
    }

    private void checkCodeUnique(String code) {
        if (code.equals("")) {
            throw new InvalidParametersException("FilterInstance with empty Code");
        }

        if (!filterRepository.findAllByCode(code).isEmpty()) {
            throw new InvalidParametersException("FilterInstance with not unique Code: " + code);
        }
    }

    private List<FilterInstanceField> checkEditFilterInstance(FilterInstanceAddRequest request) {

        var filterInstance = filterRepository.getReferenceById(request.getId());
        var mapCurrentFields = filterInstance.getFields()
                .stream()
                .collect(Collectors.toMap(f -> new Pair<>(f.getTemplateField().getType().getId(), f.getLevel()), field -> field));

        var mapNewFields = request.getFields()
                .stream()
                .collect(Collectors.toMap(f -> new Pair<>((long) f.getType().ordinal(), f.getLevel()), f -> f));

        var filterUseFlag = filterInstance.getSecurityFilters().isEmpty() && filterInstance.getFilterReports().isEmpty();
        var flagEqualsFields = checkEqualsEditFilterInstance(mapCurrentFields.keySet(), mapNewFields.keySet());
        var flagCheckDatasets = checkDatasetEquals(mapCurrentFields, mapNewFields);
        var result = updateFields(mapCurrentFields, mapNewFields, filterUseFlag);

        return getResultCheckEdit(flagEqualsFields && flagCheckDatasets, result, filterUseFlag);
    }

    private List<FilterInstanceField> getResultCheckEdit(boolean status, List<FilterInstanceField> result, boolean flagUsed) {

        if (status || flagUsed)
            return result;
        else
            throw new InvalidParametersException("FilterInstance use for SecurityFilter or FilterReport");
    }

    private boolean checkDatasetEquals(Map<Pair<Long, Long>, FilterInstanceField> currentFields, Map<Pair<Long, Long>, FilterInstanceFieldAddRequest> newFields) {

        var flag = new AtomicBoolean(true);

        newFields.keySet()
                .stream()
                .filter(currentFields::containsKey)
                .forEach(key -> {
                    var currentField = currentFields.get(key);
                    var newField = newFields.get(key);

                    var currentDataSetId = currentField.getDataSetField() == null ? -1L : currentField.getDataSetField().getId();
                    var newDataSetId = newField.getDataSetFieldId() == null ? -1L : newField.getDataSetFieldId();

                    if (currentDataSetId != newDataSetId) {
                        flag.set(false);
                    }
                });

        return flag.get();
    }

    private boolean checkEqualsEditFilterInstance(Set<Pair<Long, Long>> currentMapKey, Set<Pair<Long, Long>> newMapKey) {
        return currentMapKey.equals(newMapKey);
    }

    private List<FilterInstanceField> updateFields(Map<Pair<Long, Long>, FilterInstanceField> currentFields, Map<Pair<Long, Long>, FilterInstanceFieldAddRequest> newFields, boolean filterUseFlag) {

        var result = new ArrayList<FilterInstanceField>();
        newFields.keySet().forEach(key -> {
            var newField = newFields.get(key);
            if (currentFields.containsKey(key)) {
                var currentField =
                        currentFields.get(key)
                                .setName(newField.getName())
                                .setDescription(newField.getDescription())
                                .setDataSetField(newField.getDataSetFieldId() == null ? null : new DataSetField(newField.getDataSetFieldId()))
                                .setExpand(newField.getExpand() != null && newField.getExpand());
                result.add(currentField);
                currentFields.remove(key);
            } else
                result.add(filterInstanceFieldMapper.from(newField));
        });

        if (filterUseFlag) {
            fieldRepository.deleteAllById(currentFields.values().stream().map(BaseEntity::getId).toList());
        }

        return result;
    }
}
