package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRole;

import java.util.List;

@Repository
public interface FilterTemplateFolderRoleRepository extends JpaRepository<FilterTemplateFolderRole, Long> {
    void deleteByFolderId(Long folderId);

    List<FilterTemplateFolderRole> getAllByFolderIdInAndRoleIdIn(List<Long> folderIds, List<Long> userRoles);

    void deleteAllByFolderIdIn(List<Long> folders);

    List<FilterTemplateFolderRole> getAllByRoleId(Long roleId);

    void deleteAllByFolderIdInAndRoleId(List<Long> folderIds, Long roleId);
}
