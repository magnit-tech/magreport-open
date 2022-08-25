package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolderRole;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolderRole;

import java.util.List;

@Repository
public interface ExcelTemplateFolderRoleRepository extends JpaRepository<ExcelTemplateFolderRole, Long> {
    void deleteByFolderId(Long folderId);

    void deleteAllByFolderIdIn(List<Long> folders);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);

    List<ExcelTemplateFolderRole> getAllByRoleId(Long roleId);


    List<DataSetFolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folderIds, List<Long> userRoles);
}
