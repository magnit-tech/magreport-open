package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;

import java.util.List;

@Repository
public interface FolderRoleRepository extends JpaRepository<FolderRole, Long> {

    List<FolderRole> getAllByFolderId(Long folderId);

    void deleteByFolderId(Long folderId);

    void deleteAllByFolderIdIn(List<Long> folderIds);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);

    List<FolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folders, List<Long> roles);

    List<FolderRole> getAllByRoleId(Long roleId);
}
