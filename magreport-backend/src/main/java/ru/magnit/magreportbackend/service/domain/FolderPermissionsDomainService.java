package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSetFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.DataSourceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ExcelTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterInstanceFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.ReportFolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.permission.SecurityFilterFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.dataset.DataSetFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderReportPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderRoleMapper;
import ru.magnit.magreportbackend.mapper.folderreport.FolderRoleViewMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportFolderRoleMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderPermissionsResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterFolderRoleMapper;
import ru.magnit.magreportbackend.repository.DataSetFolderRepository;
import ru.magnit.magreportbackend.repository.DataSetFolderRoleRepository;
import ru.magnit.magreportbackend.repository.DataSourceFolderRepository;
import ru.magnit.magreportbackend.repository.DataSourceFolderRoleRepository;
import ru.magnit.magreportbackend.repository.ExcelTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.ExcelTemplateFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRepository;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FolderRepository;
import ru.magnit.magreportbackend.repository.FolderRoleRepository;
import ru.magnit.magreportbackend.repository.ReportFolderRepository;
import ru.magnit.magreportbackend.repository.ReportFolderRoleRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRepository;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderPermissionsDomainService {

    private static final String ERROR_TEXT = "null value in permissions";
    private final FolderRepository folderRepository;
    private final FolderRoleRepository folderRoleRepository;
    private final ReportFolderRepository reportFolderRepository;
    private final ReportFolderRoleRepository reportFolderRoleRepository;
    private final DataSourceFolderRoleRepository dataSourceFolderRoleRepository;
    private final DataSourceFolderRepository dataSourceFolderRepository;
    private final DataSetFolderRepository dataSetFolderRepository;
    private final DataSetFolderRoleRepository dataSetFolderRoleRepository;
    private final ExcelTemplateFolderRepository excelTemplateFolderRepository;
    private final ExcelTemplateFolderRoleRepository excelTemplateFolderRoleRepository;
    private final FilterInstanceFolderRepository filterInstanceFolderRepository;
    private final FilterInstanceFolderRoleRepository filterInstanceFolderRoleRepository;
    private final FilterTemplateFolderRepository filterTemplateFolderRepository;
    private final FilterTemplateFolderRoleRepository filterTemplateFolderRoleRepository;
    private final SecurityFilterFolderRepository securityFilterFolderRepository;
    private final SecurityFilterFolderRoleRepository securityFilterFolderRoleRepository;

    private final FolderRoleMapper folderRoleMapper;
    private final FolderRoleViewMapper folderRoleViewMapper;
    private final ReportFolderRoleMapper reportFolderRoleMapper;
    private final DataSetFolderRoleMapper dataSetFolderRoleMapper;
    private final DataSourceFolderRoleMapper dataSourceFolderRoleMapper;
    private final ExcelTemplateFolderRoleMapper excelTemplateFolderRoleMapper;
    private final FilterInstanceFolderRoleMapper filterInstanceFolderRoleMapper;
    private final FilterTemplateFolderRoleMapper filterTemplateFolderRoleMapper;
    private final SecurityFilterFolderRoleMapper securityFilterFolderRoleMapper;
    private final ReportFolderPermissionsResponseMapper reportFolderPermissionsResponseMapper;
    private final FolderReportPermissionsResponseMapper folderReportPermissionsResponseMapper;
    private final DataSetFolderPermissionsResponseMapper dataSetFolderPermissionsResponseMapper;
    private final DataSourceFolderPermissionsResponseMapper dataSourceFolderPermissionsResponseMapper;
    private final ExcelTemplateFolderPermissionsResponseMapper excelTemplateFolderPermissionsResponseMapper;
    private final FilterInstanceFolderPermissionsResponseMapper filterInstanceFolderPermissionsResponseMapper;
    private final FilterTemplateFolderPermissionsResponseMapper filterTemplateFolderPermissionsResponseMapper;
    private final SecurityFilterFolderPermissionsResponseMapper securityFilterFolderPermissionsResponseMapper;

    @Transactional
    public FolderPermissionsResponse getFolderReportPermissions(Long id) {
        final var folderRoles = folderRepository.getReferenceById(id);
        return folderReportPermissionsResponseMapper.from(folderRoles);
    }

    @Transactional
    public void setFolderReportPermissions(List<Long> folders, FolderPermissionSetRequest request) {

        final var folderPermissions = folders
            .stream()
            .map(request::setFolderId)
            .map(folderRoleViewMapper::from)
            .map(folderRoleMapper::from)
            .flatMap(Collection::stream)
            .toList();

        folderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public List<FolderRoleResponse> getFoldersReportPermissionsForRoles(List<Long> folders, List<Long> roles) {
        if (roles.contains(SystemRoles.ADMIN.getId()))
            return folders.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = folderRoleRepository.getAllByFolderIdInAndRoleIdIn(folders, roles);

        final var allPermissions = folderRoles.stream().map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L)))).toList();

        final var maxPermissions = allPermissions.stream().collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values().stream().filter(Optional::isPresent).map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT))).toList();
    }

    @Transactional
    public ReportFolderPermissionsResponse getReportFolderPermissions(Long id) {
        var folderRoles = reportFolderRepository.getReferenceById(id);
        return reportFolderPermissionsResponseMapper.from(folderRoles);
    }

    @Transactional
    public void setReportFolderPermissions(List<Long> folders, FolderPermissionSetRequest request) {
        final var folderPermissions = folders
            .stream()
            .map(request::setFolderId)
            .map(folderRoleViewMapper::from)
            .map(reportFolderRoleMapper::from)
            .flatMap(Collection::stream)
            .toList();

        reportFolderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public void setDataSourceFolderPermissions(FolderPermissionSetRequest request) {

        dataSourceFolderRoleRepository.deleteByFolderId(request.getFolderId());

        var folderRoles = dataSourceFolderRoleMapper.from(request.getRoles());
        folderRoles.forEach(folderRole -> folderRole.setFolder(new DataSourceFolder(request.getFolderId())));

        dataSourceFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public DataSourceFolderPermissionsResponse getDataSourceFolderPermissions(Long folderId) {

        var folder = dataSourceFolderRepository.getReferenceById(folderId);
        return dataSourceFolderPermissionsResponseMapper.from(folder);
    }

    @Transactional
    public DataSetFolderPermissionsResponse getDataSetFolderPermissions(Long id) {

        var folder = dataSetFolderRepository.getReferenceById(id);
        return dataSetFolderPermissionsResponseMapper.from(folder);
    }

    @Transactional
    public void setDataSetFolderPermissions(FolderPermissionSetRequest request) {
        dataSetFolderRoleRepository.deleteByFolderId(request.getFolderId());

        var folderRoles = dataSetFolderRoleMapper.from(request.getRoles());
        folderRoles.forEach(folderRole -> folderRole.setFolder(new DataSetFolder(request.getFolderId())));

        dataSetFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public ExcelTemplateFolderPermissionsResponse getExcelTemplateFolderPermissions(Long id) {

        if (excelTemplateFolderRepository.existsById(id))
            return excelTemplateFolderPermissionsResponseMapper.from(excelTemplateFolderRepository.getReferenceById(id));
        else
            return null;
    }

    @Transactional
    public void setExcelTemplateFolderPermissions(FolderPermissionSetRequest request) {

        excelTemplateFolderRoleRepository.deleteByFolderId(request.getFolderId());

        var folderRoles = excelTemplateFolderRoleMapper.from(request.getRoles());
        folderRoles.forEach(folderRole -> folderRole.setFolder(new ExcelTemplateFolder(request.getFolderId())));

        excelTemplateFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public FilterInstanceFolderPermissionsResponse getFilterInstanceFolderPermissions(Long id) {
        var folder = filterInstanceFolderRepository.getReferenceById(id);
        return filterInstanceFolderPermissionsResponseMapper.from(folder);
    }

    @Transactional
    public void setFilterInstanceFolderPermissions(FolderPermissionSetRequest request) {

        filterInstanceFolderRoleRepository.deleteByFolderId(request.getFolderId());
        var folderRoles = filterInstanceFolderRoleMapper.from(request.getRoles());
        folderRoles.forEach(folderRole -> folderRole.setFolder(new FilterInstanceFolder(request.getFolderId())));

        filterInstanceFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public FilterTemplateFolderPermissionsResponse getFilterTemplateFolderPermissions(Long id) {
        var folder = filterTemplateFolderRepository.getReferenceById(id);
        return filterTemplateFolderPermissionsResponseMapper.from(folder);
    }

    @Transactional
    public void setFilterTemplateFolderPermissions(FolderPermissionSetRequest request) {

        filterTemplateFolderRoleRepository.deleteByFolderId(request.getFolderId());
        var folderRoles = filterTemplateFolderRoleMapper.from(request.getRoles());
        folderRoles.forEach(folderRole -> folderRole.setFolder(new FilterTemplateFolder(request.getFolderId())));

        filterTemplateFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public SecurityFilterFolderPermissionsResponse getSecurityFilterFolderPermissions(Long id) {

        if (securityFilterFolderRepository.existsById(id))
            return securityFilterFolderPermissionsResponseMapper.from(securityFilterFolderRepository.getReferenceById(id));
        else
            return null;
    }

    @Transactional
    public void setSecurityFilterFolderPermissions(FolderPermissionSetRequest request) {

        var folderRoles = securityFilterFolderRoleMapper.from(request.getRoles());
        folderRoles.forEach(folderRole -> folderRole.setFolder(new SecurityFilterFolder(request.getFolderId())));

        securityFilterFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void clearFolderReportsPermissions(List<Long> folderIds) {

        folderRoleRepository.deleteAllByFolderIdIn(folderIds);
    }

    @Transactional
    public List<Long> getFolderReportBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = folderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getFolderReportBranch(folder.getId())));
        return result;
    }

    @Transactional
    public List<Long> getReportFolderBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = reportFolderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getReportFolderBranch(folder.getId())));
        return result;
    }

    @Transactional
    public List<Long> getDataSourceFolderBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = dataSourceFolderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getDataSourceFolderBranch(folder.getId())));
        return result;
    }

    @Transactional
    public List<Long> getDataSetFolderBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = dataSetFolderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getDataSetFolderBranch(folder.getId())));
        return result;
    }

    @Transactional
    public List<Long> getExcelTemplateFolderBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = excelTemplateFolderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getExcelTemplateFolderBranch(folder.getId())));
        return result;
    }

    @Transactional
    public List<Long> getFilterInstanceFolderBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = filterInstanceFolderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getFilterInstanceFolderBranch(folder.getId())));
        return result;
    }

    @Transactional
    public List<Long> getFilterTemplateFolderBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = filterTemplateFolderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getFilterTemplateFolderBranch(folder.getId())));
        return result;
    }

    @Transactional
    public List<Long> getSecurityFilterFolderBranch(Long rootFolderId) {
        final var result = new ArrayList<Long>();

        final var childFolders = securityFilterFolderRepository.getAllByParentFolderId(rootFolderId);
        if (childFolders.isEmpty()) return result;
        result.addAll(childFolders.stream().map(BaseEntity::getId).toList());
        childFolders.forEach(folder -> result.addAll(getSecurityFilterFolderBranch(folder.getId())));
        return result;
    }

    @Transactional
    public void setDataSourceFoldersPermissions(List<Long> folders, FolderPermissionSetRequest request) {
        final var folderPermissions = folders
            .stream()
            .map(id -> new FolderPermissionSetRequest().setFolderId(id).setRoles(request.getRoles()))
            .map(o -> dataSourceFolderRoleMapper.from(o.getRoles())
                .stream()
                .map(e -> e.setFolder(new DataSourceFolder(o.getFolderId())))
                .toList())
            .flatMap(Collection::stream)
            .toList();

        dataSourceFolderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public void setDataSetFoldersPermissions(List<Long> folders, FolderPermissionSetRequest request) {
        final var folderPermissions = folders
            .stream()
            .map(id -> new FolderPermissionSetRequest().setFolderId(id).setRoles(request.getRoles()))
            .map(o -> dataSetFolderRoleMapper.from(o.getRoles())
                .stream()
                .map(e -> e.setFolder(new DataSetFolder(o.getFolderId())))
                .toList())
            .flatMap(Collection::stream)
            .toList();

        dataSetFolderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public void setExcelTemplateFoldersPermissions(List<Long> folders, FolderPermissionSetRequest request) {
        final var folderPermissions = folders
            .stream()
            .map(id -> new FolderPermissionSetRequest().setFolderId(id).setRoles(request.getRoles()))
            .map(o -> excelTemplateFolderRoleMapper.from(o.getRoles())
                .stream()
                .map(e -> e.setFolder(new ExcelTemplateFolder(o.getFolderId())))
                .toList())
            .flatMap(Collection::stream)
            .toList();

        excelTemplateFolderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public void setFilterInstanceFoldersPermissions(List<Long> folders, FolderPermissionSetRequest request) {
        final var folderPermissions = folders
            .stream()
            .map(id -> new FolderPermissionSetRequest().setFolderId(id).setRoles(request.getRoles()))
            .map(o -> filterInstanceFolderRoleMapper.from(o.getRoles())
                .stream()
                .map(e -> e.setFolder(new FilterInstanceFolder(o.getFolderId())))
                .toList())
            .flatMap(Collection::stream)
            .toList();

        filterInstanceFolderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public void setFilterTemplateFoldersPermissions(List<Long> folders, FolderPermissionSetRequest request) {
        final var folderPermissions = folders
            .stream()
            .map(id -> new FolderPermissionSetRequest().setFolderId(id).setRoles(request.getRoles()))
            .map(o -> filterTemplateFolderRoleMapper.from(o.getRoles())
                .stream()
                .map(e -> e.setFolder(new FilterTemplateFolder(o.getFolderId())))
                .toList())
            .flatMap(Collection::stream)
            .toList();

        filterTemplateFolderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public void setSecurityFilterFoldersPermissions(List<Long> folders, FolderPermissionSetRequest request) {
        final var folderPermissions = folders
            .stream()
            .map(id -> new FolderPermissionSetRequest().setFolderId(id).setRoles(request.getRoles()))
            .map(o -> securityFilterFolderRoleMapper.from(o.getRoles())
                .stream()
                .map(e -> e.setFolder(new SecurityFilterFolder(o.getFolderId())))
                .toList())
            .flatMap(Collection::stream)
            .toList();

        securityFilterFolderRoleRepository.saveAll(folderPermissions);
    }

    @Transactional
    public void clearReportFoldersPermissions(List<Long> folderIds) {
        reportFolderRoleRepository.deleteAllByFolderIdIn(folderIds);
    }

    @Transactional
    public void clearDataSourceFoldersPermissions(List<Long> folders) {
        dataSourceFolderRoleRepository.deleteAllByFolderIdIn(folders);
    }

    @Transactional
    public void clearDataSetFoldersPermissions(List<Long> folders) {
        dataSetFolderRoleRepository.deleteAllByFolderIdIn(folders);
    }

    @Transactional
    public void clearExcelTemplateFoldersPermissions(List<Long> folders) {
        excelTemplateFolderRoleRepository.deleteAllByFolderIdIn(folders);
    }

    @Transactional
    public void clearFilterInstanceFoldersPermissions(List<Long> folders) {
        filterInstanceFolderRoleRepository.deleteAllByFolderIdIn(folders);
    }

    @Transactional
    public void clearFilterTemplateFoldersPermissions(List<Long> folders) {
        filterTemplateFolderRoleRepository.deleteAllByFolderIdIn(folders);
    }

    @Transactional
    public void clearSecurityFilterFoldersPermissions(List<Long> folders) {
        securityFilterFolderRoleRepository.deleteAllByFolderIdIn(folders);
    }

    @Transactional
    public List<FolderRoleResponse> getReportFolderPermissionsForRoles(List<Long> folderIds, List<Long> userRoles) {
        if (userRoles.contains(SystemRoles.ADMIN.getId()))
            return folderIds.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = reportFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(folderIds, userRoles);

        final var allPermissions = folderRoles.stream().map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L)))).toList();

        final var maxPermissions = allPermissions
            .stream()
            .collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values()
            .stream()
            .filter(Optional::isPresent)
            .map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT)))
            .toList();
    }

    @Transactional
    public List<FolderRoleResponse> getDataSourceFolderPermissionsForRoles(List<Long> folderIds, List<Long> userRoles) {
        if (userRoles.contains(SystemRoles.ADMIN.getId()))
            return folderIds.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = dataSourceFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(folderIds, userRoles);

        final var allPermissions = folderRoles
            .stream()
            .map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L))))
            .toList();

        final var maxPermissions = allPermissions
            .stream()
            .collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values()
            .stream()
            .filter(Optional::isPresent)
            .map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT)))
            .toList();
    }

    @Transactional
    public List<FolderRoleResponse> getDataSetFolderPermissionsForRoles(List<Long> folderIds, List<Long> userRoles) {
        if (userRoles.contains(SystemRoles.ADMIN.getId()))
            return folderIds.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = dataSetFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(folderIds, userRoles);

        final var allPermissions = folderRoles
            .stream()
            .map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L))))
            .toList();

        final var maxPermissions = allPermissions
            .stream()
            .collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values()
            .stream()
            .filter(Optional::isPresent)
            .map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT)))
            .toList();
    }

    @Transactional
    public List<FolderRoleResponse> getFilterTemplateFolderPermissionsForRoles(List<Long> folderIds, List<Long> userRoles) {
        if (userRoles.contains(SystemRoles.ADMIN.getId()))
            return folderIds.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = filterTemplateFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(folderIds, userRoles);

        final var allPermissions = folderRoles
            .stream()
            .map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L))))
            .toList();

        final var maxPermissions = allPermissions
            .stream()
            .collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values()
            .stream()
            .filter(Optional::isPresent)
            .map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT)))
            .toList();
    }

    @Transactional
    public List<FolderRoleResponse> getFilterInstanceFolderPermissionsForRoles(List<Long> folderIds, List<Long> userRoles) {
        if (userRoles.contains(SystemRoles.ADMIN.getId()))
            return folderIds.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = filterInstanceFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(folderIds, userRoles);

        final var allPermissions = folderRoles
            .stream()
            .map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L))))
            .toList();

        final var maxPermissions = allPermissions
            .stream()
            .collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values()
            .stream()
            .filter(Optional::isPresent)
            .map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT)))
            .toList();
    }

    @Transactional
    public List<FolderRoleResponse> getExcelTemplateFolderPermissionsForRoles(List<Long> folderIds, List<Long> userRoles) {
        if (userRoles.contains(SystemRoles.ADMIN.getId()))
            return folderIds.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = excelTemplateFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(folderIds, userRoles);

        final var allPermissions = folderRoles
            .stream()
            .map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L))))
            .toList();

        final var maxPermissions = allPermissions
            .stream()
            .collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values()
            .stream()
            .filter(Optional::isPresent)
            .map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT)))
            .toList();
    }

    @Transactional
    public List<FolderRoleResponse> getSecurityFilterFolderPermissionsForRoles(List<Long> folderIds, List<Long> userRoles) {
        if (userRoles.contains(SystemRoles.ADMIN.getId()))
            return folderIds.stream().map(folderId -> new FolderRoleResponse(folderId, FolderAuthorityEnum.WRITE)).toList();

        final var folderRoles = securityFilterFolderRoleRepository.getAllByFolderIdInAndRoleIdIn(folderIds, userRoles);

        final var allPermissions = folderRoles
                .stream()
                .map(f -> new FolderRoleResponse(f.getFolder().getId(), FolderAuthorityEnum.getById(f.getPermissions().stream().map(o -> o.getAuthority().getId()).max(Long::compareTo).orElse(0L))))
                .toList();

        final var maxPermissions = allPermissions
                .stream()
                .collect(Collectors.groupingBy(FolderRoleResponse::getFolderId, Collectors.maxBy(Comparator.comparingInt(o -> o.getAuthority().ordinal()))));

        return maxPermissions.values()
                .stream()
                .filter(Optional::isPresent)
                .map(folderRoleResponse -> folderRoleResponse.orElseThrow(() -> new IllegalArgumentException(ERROR_TEXT)))
                .toList();
    }


}
