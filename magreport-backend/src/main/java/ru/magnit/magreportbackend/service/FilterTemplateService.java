package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.filtertemplate.FilterTemplateRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterFieldTypeResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterOperationTypeResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTypeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.permission.FilterTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateResponseMapper;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRepository;
import ru.magnit.magreportbackend.service.domain.FilterTemplateDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterTemplateService {

    private final FilterTemplateDomainService filterTemplateDomainService;
    private final UserDomainService userDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final FolderEntitySearchDomainService folderEntitySearchDomainService;
    private final FilterTemplateFolderRepository filterTemplateFolderRepository;
    private final FilterTemplateResponseMapper filterTemplateResponseMapper;


    public FilterTemplateFolderResponse getFolder(FolderRequest request) {
        final var folderResponse = filterTemplateDomainService.getFolder(request.getId());

        final var folderIds = folderResponse.getChildFolders().stream().map(FilterTemplateFolderResponse::getId).collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var userRoles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();
        final var foldersPermissions = folderPermissionsDomainService.getFilterTemplateFolderPermissionsForRoles(folderIds, userRoles).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));

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

    public FilterTemplateFolderResponse addFolder(FolderAddRequest request) {
        FilterTemplateFolderPermissionsResponse currentFolderPermissions = request.getParentId() == null ?
                null :
                folderPermissionsDomainService.getFilterTemplateFolderPermissions(request.getParentId());

        final var result = filterTemplateDomainService.addFolder(request);

        if (currentFolderPermissions != null) {
            folderPermissionsDomainService.setFilterTemplateFolderPermissions(
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

        return filterTemplateDomainService.getFolder(result.getId());
    }

    public List<FilterTemplateFolderResponse> getChildFolders(FolderRequest request) {

        return filterTemplateDomainService.getChildFolders(request.getId());
    }

    public FilterTemplateFolderResponse renameFolder(FolderRenameRequest request) {

        return filterTemplateDomainService.renameFolder(request);
    }

    public void deleteFolder(FolderRequest request) {

        filterTemplateDomainService.deleteFolder(request.getId());
    }

    public List<FilterOperationTypeResponse> getFilterOperationTypes() {

        return filterTemplateDomainService.getFilterOperationTypes();
    }

    public List<FilterTypeResponse> getFilterTypes() {

        return filterTemplateDomainService.getFilterTypes();
    }

    public List<FilterFieldTypeResponse> getFilterFieldTypes() {

        return filterTemplateDomainService.getFilterFieldTypes();
    }

    public FilterTemplateResponse getFilterTemplate(FilterTemplateRequest request) {

        return filterTemplateDomainService.getFilterTemplate(request.getId());
    }

    public FolderSearchResponse searchFilterTemplate(FolderSearchRequest request) {
        return folderEntitySearchDomainService.search(request,filterTemplateFolderRepository,filterTemplateResponseMapper, FolderTypes.FILTER_TEMPLATE);
    }
}
