package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplateFolder;

import java.util.List;

@Repository
public interface ExcelTemplateFolderRepository extends JpaRepository<ExcelTemplateFolder, Long> {

    boolean existsByParentFolderId(Long folderId);
    List<ExcelTemplateFolder> getAllByParentFolderId(Long parentId);
    ExcelTemplateFolder getFirstByParentFolderIsNull();
    List<ExcelTemplateFolder> getAllByParentFolderIsNull();

    @Query(value = "WITH folders(EXCEL_TEMPLATE_FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select EXCEL_TEMPLATE_FOLDER_ID, parent_id, 1 AS LEVEL from repository.EXCEL_TEMPLATE_FOLDER " +
            "where EXCEL_TEMPLATE_FOLDER_ID = :folderId " +
            "union all " +
            "select " +
            "        f.EXCEL_TEMPLATE_FOLDER_ID, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.EXCEL_TEMPLATE_FOLDER f " +
            "inner join folders fs on fs.parent_id = f.EXCEL_TEMPLATE_FOLDER_ID " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select EXCEL_TEMPLATE_FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);
}
