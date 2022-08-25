package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolderRole;

import java.util.List;

public interface SecurityFilterFolderRoleRepository extends JpaRepository<SecurityFilterFolderRole, Long> {
    void deleteByFolderId(Long folderId);

    void deleteAllByFolderIdIn(List<Long> folders);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);

    List<SecurityFilterFolderRole> getAllByRoleId(Long roleId);

    List<SecurityFilterFolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folderIds, List<Long> userRoles);
}
