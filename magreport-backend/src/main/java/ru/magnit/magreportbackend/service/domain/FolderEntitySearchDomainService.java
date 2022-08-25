package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.FolderEntity;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResultResponse;
import ru.magnit.magreportbackend.dto.response.folderentitysearch.FolderEntitySearchResultResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.folderentitty.FolderNodeResponseFolderEntityMapper;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FolderEntitySearchDomainService {

    private final UserDomainService userDomainService;

    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final FolderNodeResponseFolderEntityMapper folderNodeResponseFolderEntityMapper;

    @Transactional
    public FolderSearchResponse search(FolderSearchRequest request, JpaRepository<? extends EntityWithName, Long> repository, Mapper<?, ?> mapper, FolderTypes typeFolder) {

        var result = new FolderSearchResponse(new LinkedList<>(), new LinkedList<>());
        final var currentUser = userDomainService.getCurrentUser();
        final var userRoles = userDomainService.getUserRoles(currentUser.getName(), null).stream().map(RoleView::getId).toList();

        if (request.getRootFolderId() == null) {
            final var folders = repository.findAll();
            folders
                    .stream()
                    .map(FolderEntity.class::cast)
                    .filter(folderEntity -> folderEntity.getParentFolder() == null)
                    .toList()
                    .forEach(folder -> search(folder, request, result, mapper, typeFolder, userRoles));
        } else {

            final var folder = repository.getReferenceById(request.getRootFolderId());

                search((FolderEntity) folder, request, result, mapper, typeFolder, userRoles);

        }
        return result;
    }

    @Transactional
    FolderSearchResponse search(FolderEntity folder, FolderSearchRequest request, FolderSearchResponse result, Mapper<?, ?> mapper, FolderTypes typeFolder, List<Long> userRolesId) {

        if (folder == null) return result;

        if(Boolean.FALSE.equals(checkPermissions(folder.getId(),typeFolder,userRolesId))) return result;

        final var elements = folder.getChildElements()
                .stream()
                .filter(element -> request.getLikenessType().check(request.getSearchString(), element.getName()))
                .toList();

        elements.forEach(element -> result.objects().add(mapElement(folder, element, mapper)));

        if (request.getLikenessType().check(request.getSearchString(), folder.getName()) && folder.getParentFolder() == null)
            result.folders().add(mapFolder(folder, getPath(folder)));

        final var folders = folder.getChildFolders()
                .stream()
                .filter(childFolder -> request.getLikenessType().check(request.getSearchString(), childFolder.getName()))
                .filter(childFolder -> checkPermissions(childFolder.getId(), typeFolder, userRolesId))
                .toList();

        folders.forEach(childFolder -> result.folders().add(mapFolder((FolderEntity) childFolder, getPath((FolderEntity) childFolder))));

        if (request.isRecursive())
            folder.getChildFolders().forEach(childFolder -> search((FolderEntity) childFolder, request, result, mapper, typeFolder, userRolesId));


        return result;
    }

    @SuppressWarnings("all")
    private FolderEntitySearchResultResponse mapElement(FolderEntity path, EntityWithName element, Mapper mapper) {
        return new FolderEntitySearchResultResponse(getPath(path), mapper.from(element));
    }

    private FolderSearchResultResponse mapFolder(FolderEntity folder, List<FolderNodeResponse> path) {
        return new FolderSearchResultResponse(path, new FolderNodeResponse(folder.getId(), folder.getParentFolder() == null ? null : folder.getParentFolder().getId(), folder.getName(), folder.getDescription(), folder.getCreatedDateTime(), folder.getModifiedDateTime()));
    }

    private List<FolderNodeResponse> getPath(FolderEntity folder) {
        return folderNodeResponseFolderEntityMapper.from(getPathStream(folder).toList());
    }

    private Stream<FolderEntity> getPathStream(FolderEntity folder) {
        return folder == null ?
                Stream.empty() :
                Stream.of(folder).flatMap(node ->
                        Stream.concat(getPathStream((FolderEntity) node.getParentFolder()), Stream.of(folder)));
    }

    private Boolean checkPermissions(Long folderId, FolderTypes folderType, List<Long> roles) {

        List<FolderRoleResponse> folderRoleResponses =   switch (folderType) {
            case PUBLISHED_REPORT -> folderPermissionsDomainService.getFoldersReportPermissionsForRoles(Collections.singletonList(folderId), roles);
            case REPORT_FOLDER -> folderPermissionsDomainService.getReportFolderPermissionsForRoles(Collections.singletonList(folderId), roles);
            case DATASOURCE -> folderPermissionsDomainService.getDataSourceFolderPermissionsForRoles(Collections.singletonList(folderId), roles);
            case DATASET -> folderPermissionsDomainService.getDataSetFolderPermissionsForRoles(Collections.singletonList(folderId), roles);
            case EXCEL_TEMPLATE -> folderPermissionsDomainService.getExcelTemplateFolderPermissionsForRoles(Collections.singletonList(folderId), roles);
            case FILTER_INSTANCE -> folderPermissionsDomainService.getFilterInstanceFolderPermissionsForRoles(Collections.singletonList(folderId), roles);
            case FILTER_TEMPLATE -> folderPermissionsDomainService.getFilterTemplateFolderPermissionsForRoles(Collections.singletonList(folderId), roles);
            case SECURITY_FILTER -> folderPermissionsDomainService.getSecurityFilterFolderPermissionsForRoles(Collections.singletonList(folderId), roles);
        };

        return !folderRoleResponses.isEmpty() && folderRoleResponses
                .stream()
                .filter(r -> r.getFolderId().equals(folderId))
                .noneMatch(r -> r.getAuthority().equals(FolderAuthorityEnum.NONE));


    }

}
