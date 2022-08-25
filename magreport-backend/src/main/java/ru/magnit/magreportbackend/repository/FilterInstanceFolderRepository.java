package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;

import java.util.List;

public interface FilterInstanceFolderRepository extends JpaRepository<FilterInstanceFolder, Long> {

    List<FilterInstanceFolder> getAllByParentFolderId(Long parentId);

    Boolean existsByParentFolderId(Long parentId);

    FilterInstanceFolder getFirstByParentFolderIsNull();

    List<FilterInstanceFolder> getAllByParentFolderIsNull();

    @Query(value = "WITH folders(FILTER_INSTANCE_FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select FILTER_INSTANCE_FOLDER_ID, parent_id, 1 AS LEVEL from repository.FILTER_INSTANCE_FOLDER " +
            "where FILTER_INSTANCE_FOLDER_ID = :folderId " +
            "union all " +
            "select " +
            "        f.FILTER_INSTANCE_FOLDER_ID, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.FILTER_INSTANCE_FOLDER f " +
            "inner join folders fs on fs.parent_id = f.FILTER_INSTANCE_FOLDER_ID " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select FILTER_INSTANCE_FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);
}
