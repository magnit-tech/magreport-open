package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceAddRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.FilterInstanceRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceChildNodesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceFolderResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceValuesResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterInstanceFolderPermissionsResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceResponseMapper;
import ru.magnit.magreportbackend.repository.FilterInstanceFolderRepository;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.FilterTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterInstanceService {

    private final FilterInstanceDomainService domainService;
    private final FilterTemplateDomainService filterTemplateDomainService;
    private final UserDomainService userDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final SecurityFilterDomainService securityFilterDomainService;
    private final FilterReportDomainService filterReportDomainService;
    private final FolderEntitySearchDomainService folderEntitySearchDomainService;
    private final ReportDomainService reportDomainService;
    private final FilterInstanceFolderRepository filterInstanceFolderRepository;
    private final FilterInstanceResponseMapper filterInstanceResponseMapper;
    private final PermissionCheckerSystem permissionCheckerSystem;

    public FilterInstanceFolderResponse getFolder(FolderRequest request) {
        final var folderResponse = domainService.getFolder(request.getId());

        final var folderIds = folderResponse.getChildFolders().stream().map(FilterInstanceFolderResponse::getId).collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var userRoles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();
        final var foldersPermissions = folderPermissionsDomainService.getFilterInstanceFolderPermissionsForRoles(folderIds, userRoles).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));

        folderResponse.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE));
        folderResponse.getChildFolders()
                .forEach(folder -> folder.setAuthority(foldersPermissions.getOrDefault(folder.getId(), FolderAuthorityEnum.NONE)));

        if (userRoles.contains(SystemRoles.ADMIN.getId())) {
            folderResponse.setAuthority(FolderAuthorityEnum.WRITE);
            folderResponse.getChildFolders().forEach(folder -> folder.setAuthority(FolderAuthorityEnum.WRITE));
            return folderResponse;
        }

        if (folderResponse.getParentId() != null && folderResponse.getAuthority() == FolderAuthorityEnum.NONE)
            return null;

        folderResponse.getChildFolders().removeIf(folder -> folder.getAuthority() == FolderAuthorityEnum.NONE);

        return folderResponse;
    }

    public FilterInstanceFolderResponse addFolder(FolderAddRequest request) {
        FilterInstanceFolderPermissionsResponse currentFolderPermissions = request.getParentId() == null ?
                null :
                folderPermissionsDomainService.getFilterInstanceFolderPermissions(request.getParentId());

        final var result = domainService.addFolder(request);

        if (currentFolderPermissions != null) {
            folderPermissionsDomainService.setFilterInstanceFolderPermissions(
                    new FolderPermissionSetRequest()
                            .setFolderId(result.getId())
                            .setRoles(currentFolderPermissions
                                    .rolePermissions()
                                    .stream()
                                    .map(rfp -> new RoleAddPermissionRequest()
                                            .setRoleId(rfp.role().getId())
                                            .setPermissions(rfp.permissions()))
                                    .toList()
                            ));
        }

        return domainService.getFolder(result.getId());
    }

    public List<FilterInstanceFolderResponse> getChildFolders(FolderRequest request) {

        return domainService.getChildFolders(request.getId());
    }

    public FilterInstanceFolderResponse renameFolder(FolderRenameRequest request) {

        return domainService.renameFolder(request);
    }

    public void deleteFolder(FolderRequest request) {

        domainService.deleteFolder(request.getId());
    }

    public FilterInstanceResponse addFilterInstance(FilterInstanceAddRequest request) {

        final var currentUser = userDomainService.getCurrentUser();
        final var filterTemplate = filterTemplateDomainService.getFilterTemplate(request.getTemplateId());
        var filterId = domainService.addFilterInstance(currentUser, request, filterTemplate);
        return domainService.getFilterInstance(filterId);
    }

    public void deleteFilterInstance(FilterInstanceRequest request) {

        var filters = filterReportDomainService.checkFilterReportForInstance(request.getId());
        if (!filters.isEmpty())
            throw new InvalidParametersException("Экземпляр фильтра не может быть удален пока существуют отчеты, которые его используют");

        domainService.deleteFilterInstance(request.getId());
    }

    public FilterInstanceResponse getFilterInstance(FilterInstanceRequest request) {

        return domainService.getFilterInstance(request.getId());
    }

    public FilterInstanceResponse editFilterInstance(FilterInstanceAddRequest request) {

        final var filterTemplate = filterTemplateDomainService.getFilterTemplate(request.getTemplateId());
        var filterId = domainService.editFilterInstance(request, filterTemplate);
        filterReportDomainService.updateExpandFields(filterId);
        return domainService.getFilterInstance(filterId);
    }

    public FilterInstanceValuesResponse getFilterInstanceValues(ListValuesRequest request) {

        return domainService.getFilterInstanceValues(request);
    }

    public FilterInstanceChildNodesResponse getChildNodes(ChildNodesRequest request) {

        final var currentUserRoles = userDomainService.getCurrentUserRoles(UserRoleTypeEnum.MANUAL);
        final var dataSetId = domainService.getDataSetId(request.getFilterId());
        final var effectiveSettings = securityFilterDomainService.getEffectiveSettings(dataSetId, currentUserRoles.stream().map(RoleView::getId).collect(Collectors.toSet()));

        return domainService.getChildNodes(request, effectiveSettings);
    }

    public FilterInstanceFolderResponse changeParentFolder(FolderChangeParentRequest request) {

        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getFilterInstanceFolderPermissionsForRoles(Collections.singletonList(request.getId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Move for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getFilterInstanceFolderPermissionsForRoles(Collections.singletonList(request.getParentId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });
        return domainService.changeParentFolder(request);
    }

    public FolderSearchResponse searchFilterInstance(FolderSearchRequest request) {
        return folderEntitySearchDomainService.search(request, filterInstanceFolderRepository, filterInstanceResponseMapper, FolderTypes.FILTER_INSTANCE);
    }

    public FilterInstanceDependenciesResponse getFilterInstanceDependants(FilterInstanceRequest request) {

        var response = domainService.getFilterInstanceDependants(request.getId());

        response.getReports().forEach(reportResponse -> reportResponse.setPath(reportDomainService.getPathReport(reportResponse.getId())));
        response.getSecurityFilters().forEach(securityFilterResponse -> securityFilterResponse.path().addAll(securityFilterDomainService.getPathSecurityFilter(securityFilterResponse.id())));
        response.setPath(domainService.getPathFilter(response.getId()));
        return response;
    }

    public void changeFilterInstanceParentFolder(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, domainService::getFolderIds, folderPermissionsDomainService::getExcelTemplateFolderPermissionsForRoles);
        domainService.changeFilterInstanceParentFolder(request);
    }

    public void copyFilterInstance(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, null, folderPermissionsDomainService::getFilterInstanceFolderPermissionsForRoles);
        domainService.copyFilterInstance(request, userDomainService.getCurrentUser());
    }

    public List<FilterInstanceFolderResponse> copyFilterInstanceFolder(CopyFolderRequest request) {
        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getFilterInstanceFolderPermissionsForRoles(request.getFolderIds(), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Copy for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getFilterInstanceFolderPermissionsForRoles(Collections.singletonList(request.getDestFolderId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied" + f.getFolderId());
                });

        var newFolders = domainService.copyFilterInstanceFolder(request, userDomainService.getCurrentUser());
        return newFolders.stream().map(domainService::getFolder).toList();
    }
}
