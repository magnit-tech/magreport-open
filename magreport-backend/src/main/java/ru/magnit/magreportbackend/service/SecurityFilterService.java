package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.UserRoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportValuesCheckResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterRoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterValuesCheckResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;
import ru.magnit.magreportbackend.repository.SecurityFilterFolderRepository;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.Collections;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SecurityFilterService {

    private final SecurityFilterDomainService domainService;
    private final UserDomainService userDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final FolderEntitySearchDomainService folderEntitySearchDomainService;
    private final SecurityFilterFolderRepository securityFilterFolderRepository;
    private final SecurityFilterResponseMapper securityFilterResponseMapper;
    private final PermissionCheckerSystem permissionCheckerSystem;

    public SecurityFilterResponse addSecurityFilter(SecurityFilterAddRequest request) {

        final var currentUser = userDomainService.getCurrentUser();
        val securityFilterId = domainService.addSecurityFilter(currentUser, request);

        return domainService.getSecurityFilter(securityFilterId);
    }

    public SecurityFilterResponse getSecurityFilter(SecurityFilterRequest request) {

        return domainService.getSecurityFilter(request.getId());
    }

    public void deleteSecurityFilter(SecurityFilterRequest request) {

        domainService.deleteSecurityFilter(request.getId());
    }

    public SecurityFilterFolderResponse getFolder(FolderRequest request) {

        final var folderResponse = domainService.getFolder(request.getId());

        final var folderIds = folderResponse.getChildFolders().stream().map(SecurityFilterFolderResponse::getId).collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var userRoles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), UserRoleTypeEnum.MANUAL).stream().map(RoleView::getId).toList();
        final var foldersPermissions = folderPermissionsDomainService.getFoldersReportPermissionsForRoles(folderIds, userRoles).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));

        folderResponse.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE));
        folderResponse.getChildFolders().forEach(folder -> folder.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE)));

        return folderResponse;
    }

    public SecurityFilterFolderResponse addFolder(FolderAddRequest request) {

        var folderId = domainService.addFolder(request);

        return domainService.getFolder(folderId);
    }

    public SecurityFilterFolderResponse renameFolder(FolderRenameRequest request) {

        var folderId = domainService.renameFolder(request);

        return domainService.getFolder(folderId);
    }

    public void deleteFolder(FolderRequest request) {

        domainService.deleteFolder(request.getId());
    }

    public SecurityFilterRoleSettingsResponse setRoleSettings(SecurityFilterSetRoleRequest request) {

        domainService.deleteRoles(request.getSecurityFilterId());

        domainService.setRoleSettings(request);

        return domainService.getFilterRoleSettings(request.getSecurityFilterId());
    }

    public SecurityFilterRoleSettingsResponse getRoleSettings(SecurityFilterRequest request) {

        return domainService.getFilterRoleSettings(request.getId());
    }

    public SecurityFilterResponse editSecurityFilter(SecurityFilterAddRequest request) {

        if (request.getId() == null) throw new InvalidParametersException("Field 'id' must not be null.");

        domainService.deleteDataSetMappings(request.getId());

        domainService.editSecurityFilter(request);

        return domainService.getSecurityFilter(request.getId());
    }

    public void updateRoleSettings(SecurityFilterSetRoleRequest request) {

        domainService.deleteRole(request.getSecurityFilterId(), request.getRoleSettings().get(0).getRoleId());

        domainService.setRoleSettings(request);
    }

    public SecurityFilterFolderResponse changeParentFolder(FolderChangeParentRequest request) {

        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getSecurityFilterFolderPermissionsForRoles(Collections.singletonList(request.getId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Move for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getSecurityFilterFolderPermissionsForRoles(Collections.singletonList(request.getParentId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });


        return domainService.changeParentFolder(request);
    }

    public FolderSearchResponse searchSecurityFilter(FolderSearchRequest request) {
        return folderEntitySearchDomainService.search(request, securityFilterFolderRepository, securityFilterResponseMapper, FolderTypes.SECURITY_FILTER);
    }

    public void changeSecurityFilterParentFolder(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, domainService::getFolderIds, folderPermissionsDomainService::getExcelTemplateFolderPermissionsForRoles);
        domainService.changeFilterInstanceParentFolder(request);
    }

    public SecurityFilterValuesCheckResponse checkFilterReportValues(ListValuesCheckRequest request) {
        return domainService.checkFilterReportValues(request);
    }
}
