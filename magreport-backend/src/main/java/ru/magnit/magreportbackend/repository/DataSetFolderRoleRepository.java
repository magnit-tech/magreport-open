package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;

import java.util.List;

@Repository
public interface DataSetFolderRoleRepository extends JpaRepository<DataSetFolderRole, Long> {
    void deleteByFolderId(Long folderId);

    List<DataSetFolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folderIds, List<Long> userRoles);

    void deleteAllByFolderIdIn(List<Long> folders);

    List<DataSetFolderRole> getAllByRoleId(Long roleId);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);
}
