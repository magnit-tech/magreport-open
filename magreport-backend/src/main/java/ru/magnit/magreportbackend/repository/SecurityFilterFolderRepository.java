package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;

import java.util.List;

public interface SecurityFilterFolderRepository extends JpaRepository<SecurityFilterFolder, Long> {

    List<SecurityFilterFolder> getAllByParentFolderId(Long parentId);

    SecurityFilterFolder getFirstByParentFolderIsNull();

    List<SecurityFilterFolder> getAllByParentFolderIsNull();

    Boolean existsByParentFolderId(Long id);

    @Query(value = "WITH folders(SECURITY_FILTER_FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select SECURITY_FILTER_FOLDER_ID, parent_id, 1 AS LEVEL from repository.SECURITY_FILTER_FOLDER " +
            "where SECURITY_FILTER_FOLDER_ID = :folderId " +
            "union all " +
            "select " +
            "        f.SECURITY_FILTER_FOLDER_ID, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.SECURITY_FILTER_FOLDER f " +
            "inner join folders fs on fs.parent_id = f.SECURITY_FILTER_FOLDER_ID " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select SECURITY_FILTER_FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);

}
