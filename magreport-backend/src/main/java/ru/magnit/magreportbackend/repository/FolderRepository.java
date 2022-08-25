package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.folderreport.Folder;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> getAllByParentFolderId(Long parentId);

    boolean existsByParentFolderId(Long id);

    Folder getFirstByParentFolderIsNull();

    List<Folder> getAllByParentFolderIsNull();

    @Query(value = "WITH folders(FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select folder_id, parent_id, 1 AS LEVEL from repository.folder " +
            "where folder_id = :folderId " +
            "union all " +
            "select " +
            "        f.folder_id, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.folder f " +
            "inner join folders fs on fs.parent_id = f.folder_id " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);
}
