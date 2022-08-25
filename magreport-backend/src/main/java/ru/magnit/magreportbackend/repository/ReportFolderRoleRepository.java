package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.report.ReportFolderRole;

import java.util.List;

@Repository
public interface ReportFolderRoleRepository extends JpaRepository<ReportFolderRole, Long> {

    List<ReportFolderRole> getAllByFolderId(Long id);

    void deleteByFolderId(Long folderId);

    void deleteAllByFolderIdIn(List<Long> folderIds);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);

    List<ReportFolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folderIds, List<Long> roleIds);

    List<ReportFolderRole> getAllByRoleId(Long roleId);
}
