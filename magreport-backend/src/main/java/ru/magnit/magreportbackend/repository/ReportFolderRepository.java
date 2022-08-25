package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.report.ReportFolder;

import java.util.List;

public interface ReportFolderRepository extends JpaRepository<ReportFolder, Long> {

    boolean existsByParentFolderId(Long id);

    List<ReportFolder> getAllByParentFolderId(Long parentId);

    ReportFolder getFirstByParentFolderIsNull();

    List<ReportFolder> getAllByParentFolderIsNull();

    @Query(value = "WITH folders(REPORT_FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select REPORT_FOLDER_ID, parent_id, 1 AS LEVEL from repository.REPORT_FOLDER " +
            "where REPORT_FOLDER_ID = :folderId " +
            "union all " +
            "select " +
            "        f.REPORT_FOLDER_ID, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.REPORT_FOLDER f " +
            "inner join folders fs on fs.parent_id = f.REPORT_FOLDER_ID " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select REPORT_FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);
}
