package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;

import java.util.List;

public interface FilterTemplateFolderRepository extends JpaRepository<FilterTemplateFolder, Long> {

    List<FilterTemplateFolder> getAllByParentFolderId(Long parentId);

    Boolean existsByParentFolderId(Long parentId);

    FilterTemplateFolder getFirstByParentFolderIsNull();

    List<FilterTemplateFolder> getAllByParentFolderIsNull();

    @Query(value = "WITH folders(FILTER_TEMPLATE_FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select FILTER_TEMPLATE_FOLDER_ID, parent_id, 1 AS LEVEL from repository.FILTER_TEMPLATE_FOLDER " +
            "where FILTER_TEMPLATE_FOLDER_ID = :folderId " +
            "union all " +
            "select " +
            "        f.FILTER_TEMPLATE_FOLDER_ID, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.FILTER_TEMPLATE_FOLDER f " +
            "inner join folders fs on fs.parent_id = f.FILTER_TEMPLATE_FOLDER_ID " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select FILTER_TEMPLATE_FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);
}
