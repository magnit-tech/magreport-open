package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolderRole;

import java.util.List;

@Repository
public interface DataSourceFolderRoleRepository extends JpaRepository<DataSourceFolderRole, Long> {
    void deleteByFolderId(Long folderId);

    void deleteAllByFolderIdIn(List<Long> folderIds);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);

    List<DataSourceFolderRole> getAllByRoleId(Long roleId);

    List<DataSourceFolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folderIds, List<Long> userRoles);
}
