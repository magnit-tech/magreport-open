package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRole;

import java.util.List;

public interface FilterInstanceFolderRoleRepository extends JpaRepository<FilterInstanceFolderRole, Long> {
    void deleteByFolderId(Long folderId);

    List<FilterInstanceFolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folderIds, List<Long> userRoles);

    void deleteAllByFolderIdIn(List<Long> folders);

    List<FilterInstanceFolderRole> getAllByRoleId(Long roleId);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);
}
