package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRole;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRolePermission;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterRoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterShortResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterValuesCheckResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterDataFIMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobFilterDataSFMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.FolderNodeResponseSecurityFilterFolderMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterDataMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterMerger;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterRoleMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterRoleSettingsResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterShortResponseMapper;
import ru.magnit.magreportbackend.repository.SecurityFilterDataSetFieldRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterDataSetRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRoleRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterRoleRepository;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityFilterDomainService {

    private final SecurityFilterRepository repository;
    private final SecurityFilterRoleRepository roleRepository;
    private final SecurityFilterFolderRepository folderRepository;
    private final SecurityFilterFolderRoleRepository securityFilterFolderRoleRepository;
    private final SecurityFilterDataSetRepository dataSetRepository;
    private final SecurityFilterDataSetFieldRepository dataSetFieldRepository;
    private final FilterQueryExecutor queryExecutor;

    private final SecurityFilterMapper securityFilterMapper;
    private final SecurityFilterResponseMapper securityFilterResponseMapper;
    private final SecurityFilterShortResponseMapper securityFilterShortResponseMapper;
    private final SecurityFilterFolderResponseMapper securityFilterFolderResponseMapper;
    private final SecurityFilterFolderMapper securityFilterFolderMapper;
    private final SecurityFilterRoleMapper securityFilterRoleMapper;
    private final SecurityFilterRoleSettingsResponseMapper securityFilterRoleSettingsResponseMapper;
    private final FilterDataFIMapper filterDataFIMapper;
    private final SecurityFilterMerger securityFilterMerger;
    private final ReportJobFilterDataSFMapper reportJobFilterDataSFMapper;
    private final FolderNodeResponseSecurityFilterFolderMapper folderNodeResponseSecurityFilterFolderMapper;
    private final SecurityFilterFolderRolePermissionMapper securityFilterFolderRolePermissionMapper;

    private final SecurityFilterDataMapper securityFilterDataMapper;


    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Transactional
    public Long addSecurityFilter(UserView currentUser, SecurityFilterAddRequest request) {

        var securityFilter = securityFilterMapper.from(request);
        securityFilter.setUser(new User(currentUser.getId()));
        securityFilter = repository.save(securityFilter);

        return securityFilter.getId();
    }

    @Transactional
    public SecurityFilterResponse getSecurityFilter(Long securityFilterId) {
        return securityFilterResponseMapper.from(repository.getReferenceById(securityFilterId));
    }

    @Transactional
    public void deleteSecurityFilter(Long securityFilterId) {

        repository.deleteById(securityFilterId);
    }

    @Transactional
    public void deleteRoles(Long securityFilterId) {

        roleRepository.deleteBySecurityFilterId(securityFilterId);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = securityFilterFolderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(SecurityFilterFolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.SECURITY_FILTER)
        ).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new SecurityFilterFolderRole()
                    .setFolder(new SecurityFilterFolder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(securityFilterFolderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
            return res;
        }).toList();

        securityFilterFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        securityFilterFolderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds,roleId);
    }

    @Transactional
    public List<SecurityFilterShortResponse> getFiltersWithSettingsForRole(Long roleId) {
        final var filterRoles = roleRepository.getAllByRoleId(roleId);
        final var securityFilters = filterRoles.stream().map(SecurityFilterRole::getSecurityFilter).distinct().toList();
        return securityFilterShortResponseMapper.from(securityFilters);
    }

    @Transactional
    public SecurityFilterFolderResponse getFolder(Long folderId) {
        if (folderId != null) checkFolderExists(folderId);

        if (folderId == null) {
            return new SecurityFilterFolderResponse(
                    null,
                    null,
                    "root",
                    null,
                    securityFilterFolderResponseMapper.shallowMap(folderRepository.getAllByParentFolderIsNull()),
                    Collections.emptyList(),
                    FolderAuthorityEnum.NONE,
                    LocalDateTime.now(),
                    LocalDateTime.now());
        } else {
            var folder = folderRepository.getReferenceById(folderId);

            return securityFilterFolderResponseMapper.from(folder);
        }
    }

    @Transactional
    public Long addFolder(FolderAddRequest request) {

        var folder = securityFilterFolderMapper.from(request);
        folder = folderRepository.save(folder);

        if (folderRepository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return folder.getId();
    }

    @Transactional
    public Long renameFolder(FolderRenameRequest request) {

        checkFolderExists(request.getId());

        var folder = folderRepository.getReferenceById(request.getId());

        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folderRepository.save(folder);

        return request.getId();
    }

    @Transactional
    public void deleteFolder(Long folderId) {
        checkFolderExists(folderId);
        checkFolderEmpty(folderId);

        folderRepository.deleteById(folderId);
    }

    @Transactional
    public void setRoleSettings(SecurityFilterSetRoleRequest request) {
        var securityFilterRoles = securityFilterRoleMapper.from(Collections.singletonList(request));
        roleRepository.saveAll(securityFilterRoles);
    }

    @Transactional
    public SecurityFilterRoleSettingsResponse getFilterRoleSettings(Long securityFilterId) {

        final var securityFilterRole = repository.getReferenceById(securityFilterId);
        final var filterData = filterDataFIMapper.from(securityFilterRole.getFilterInstance());

        final var response = securityFilterRoleSettingsResponseMapper.from(securityFilterRole);

        if (filterData.filterType() == FilterTypeEnum.TOKEN_INPUT || filterData.filterType() == FilterTypeEnum.VALUE_LIST) {
            response
                    .roleSettings()
                    .forEach(settings -> {
                        final var fieldsValues = queryExecutor.getFieldsValues(new FilterRequestData(filterData, settings.tuples()));
                        settings.tuples().clear();
                        settings.tuples().addAll(fieldsValues);
                    });
        }

        return response;
    }

    @Transactional
    public void deleteDataSetMappings(Long securityFilterId) {

        dataSetRepository.deleteAllBySecurityFilterId(securityFilterId);
        dataSetFieldRepository.deleteAllBySecurityFilterId(securityFilterId);
    }

    @Transactional
    public void editSecurityFilter(SecurityFilterAddRequest request) {
        final var securityFilter = repository.getReferenceById(request.getId());
        var result = securityFilterMerger.merge(securityFilter, request);
        repository.save(result);
    }

    @Transactional
    public List<ReportJobFilterData> getEffectiveSettings(Long dataSetId, Set<Long> roleSet) {

        final var dataSetList = dataSetRepository.findAllByDataSetId(dataSetId);

        final var securityFilterRoles = dataSetList.stream()
                .map(SecurityFilterDataSet::getSecurityFilter)
                .flatMap(secFilter -> secFilter.getFilterRoles().stream())
                .filter(o -> roleSet.contains(o.getRole().getId()))
                .toList();
        return reportJobFilterDataSFMapper.from(securityFilterRoles);
    }

    @Transactional
    public void deleteRole(Long securityFilterId, Long roleId) {
        roleRepository.deleteAllBySecurityFilterIdAndRoleId(securityFilterId, roleId);
    }

    @Transactional
    public SecurityFilterFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        final var folder = folderRepository.getReferenceById(request.getId());
        final var parentFolder = request.getParentId() == null ? null : new SecurityFilterFolder(request.getParentId());
        folder.setParentFolder(parentFolder);
        folderRepository.save(folder);
        return securityFilterFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<FolderNodeResponse> getPathSecurityFilter(Long securityFilterId) {
        return getPath(repository.getReferenceById(securityFilterId).getFolder());
    }

    @Transactional
    public List<SecurityFilterResponse> checkSecurityFilterExistsForDataset(Long dataSetId) {
        var securityFilterDataSets = dataSetRepository.findAllByDataSetId(dataSetId);

        var filters = securityFilterDataSets.stream().map(SecurityFilterDataSet::getSecurityFilter).collect(Collectors.toList());

        return filters.stream().map(securityFilterResponseMapper::from).collect(Collectors.toList());
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(folderRepository.getReferenceById(folderId));
    }

    @Transactional
    public List<Long> getFolderIds(List<Long> objIds) {
        return repository.getAllByIdIn(objIds)
                .stream()
                .map(securityFilter -> securityFilter.getFolder().getId())
                .distinct()
                .toList();
    }

    @Transactional
    public void changeFilterInstanceParentFolder(ChangeParentFolderRequest request) {
        final var securityFilters = repository.getAllByIdIn(request.getObjIds());
        final var newFolder = new SecurityFilterFolder(request.getDestFolderId());
        securityFilters.forEach(filterInstance -> filterInstance.setFolder(newFolder));
        repository.saveAll(securityFilters);
    }

    @Transactional
    public SecurityFilterValuesCheckResponse checkFilterReportValues(ListValuesCheckRequest request) {
        final var securityFilter = repository.getReferenceById(request.getFilterId());

        final var tuples = queryExecutor.getFieldsValues(new FilterRequestData(securityFilterDataMapper.from(securityFilter), Collections.singletonList(new Tuple(request.getValues()))));
        final var tupleValues = request.getValues()
                .stream()
                .map(TupleValue::getValue)
                .filter(value -> tuples.stream().flatMap(tuple -> tuple.getValues().stream()).noneMatch(val -> val.getValue().equals(value)))
                .toList();

        return new SecurityFilterValuesCheckResponse(securityFilterResponseMapper.from(securityFilter), tupleValues);
    }


    private List<FolderNodeResponse> getPath(SecurityFilterFolder folder) {

        var result = new LinkedList<FolderNodeResponse>();

        result.add(folderNodeResponseSecurityFilterFolderMapper.from(folder));

        while (folder.getParentFolder() != null) {
            folder = folder.getParentFolder();
            result.addFirst(folderNodeResponseSecurityFilterFolderMapper.from(folder));
        }

        return result;
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
                !repository.existsByFolderId(id);
    }
}
