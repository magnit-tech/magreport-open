package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.exception.PermissionDeniedException;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionCheckerSystem {

    private final UserDomainService userDomainService;

    public void checkPermissionsOnAllFolders(ChangeParentFolderRequest request, UnaryOperator<List<Long>> folderGetter, BiFunction<List<Long>, List<Long>, List<FolderRoleResponse>> permissionsGetter) {
        final var folders = new ArrayList<Long>();
        folders.add(request.getDestFolderId());

        if (folderGetter != null) {
            folders.addAll(folderGetter.apply(request.getObjIds()));
        }

        final var userRoles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null)
            .stream()
            .map(RoleView::getId)
            .toList();

        final var foldersPermissions = permissionsGetter.apply(folders, userRoles)
            .stream()
            .filter(perm -> perm.getAuthority() == FolderAuthorityEnum.WRITE)
            .distinct()
            .map(FolderRoleResponse::getFolderId)
            .toList();

        final var sourceFolders = new HashSet<>(folders);
        foldersPermissions.forEach(sourceFolders::remove);

        if (!sourceFolders.isEmpty()) {
            throw new PermissionDeniedException("Permission denied");
        }
    }
}
