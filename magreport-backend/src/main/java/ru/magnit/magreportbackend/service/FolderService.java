package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderAddReportRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.dto.request.report.ReportIdRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folderreport.FolderResponse;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionsResponse;
import ru.magnit.magreportbackend.dto.response.report.PublishedReportResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.service.domain.FolderDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderDomainService domainService;
    private final UserDomainService userDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final ReportDomainService reportDomainService;

    public FolderResponse addFolder(FolderAddRequest request) {
        final var currentUser = userDomainService.getCurrentUser();

        FolderPermissionsResponse currentFolderPermissions = request.getParentId() == null ?
                null :
                folderPermissionsDomainService.getFolderReportPermissions(request.getParentId());

        final var result = domainService.addFolder(request);

        if (currentFolderPermissions != null) {
            folderPermissionsDomainService.setFolderReportPermissions(
                    Collections.singletonList(result.getId()),
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

        return domainService.getFolder(currentUser.getId(), result.getId());
    }

    public FolderResponse getFolder(FolderRequest request) {
        final var currentUser = userDomainService.getCurrentUser();

        final var folderResponse = domainService.getFolder(currentUser.getId(), request.getId());

        final var folderIds = folderResponse.getChildFolders().stream().map(FolderResponse::getId).collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var roleIds = userDomainService.getUserRoles(currentUser.getName(), null).stream().map(RoleView::getId).toList();

        if (roleIds.contains(SystemRoles.ADMIN.getId())) {
            folderResponse.setAuthority(FolderAuthorityEnum.WRITE);
            folderResponse.getChildFolders().forEach(folder -> folder.setAuthority(FolderAuthorityEnum.WRITE));
            return folderResponse;
        }

        final var foldersPermissions = folderPermissionsDomainService.getFoldersReportPermissionsForRoles(folderIds, roleIds).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));
        folderResponse.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE));
        folderResponse.getChildFolders().forEach(folder -> folder.setAuthority(foldersPermissions.getOrDefault(folder.getId(), FolderAuthorityEnum.NONE)));

        if (folderResponse.getParentId() != null && folderResponse.getAuthority() == FolderAuthorityEnum.NONE)
            return null;

        folderResponse.getChildFolders().removeIf(folder -> folder.getAuthority() == FolderAuthorityEnum.NONE);

        return folderResponse;
    }

    public List<FolderResponse> getChildFolders(FolderRequest request) {

        return domainService.getChildFolders(request.getId());
    }

    public FolderResponse renameFolder(FolderRenameRequest request) {

        return domainService.renameFolder(request);
    }

    public void deleteFolder(FolderRequest request) {

        domainService.deleteFolder(request.getId());
    }

    public void addReport(FolderAddReportRequest request) {

        domainService.addReport(request);
    }

    public void deleteReport(FolderAddReportRequest request) {

        domainService.deleteReport(request);
    }

    public FolderSearchResponse searchFolder(FolderSearchRequest request) {

        return domainService.searchFolder(request);
    }

    public FolderResponse changeParentFolder(FolderChangeParentRequest request) {

        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getFoldersReportPermissionsForRoles(Collections.singletonList(request.getId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Move for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getFoldersReportPermissionsForRoles(Collections.singletonList(request.getParentId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });
        return domainService.changeParentFolder(request);
    }

    public PublishedReportResponse getPublishedReports (ReportIdRequest request) {

        var response = domainService.getPublishedReports(request);

        if(response == null) response = reportDomainService.getPublishedReportResponse(request.getId());

        response.setPath(reportDomainService.getPathReport(response.getId()));

        return response;
    }

}
