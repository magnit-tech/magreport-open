package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;

import java.util.List;

@Repository
public interface DataSetFolderRepository extends JpaRepository<DataSetFolder, Long> {

    List<DataSetFolder> getAllByParentFolderId(Long parentId);

    Boolean existsByParentFolderId(Long parentId);

    DataSetFolder getFirstByParentFolderIsNull();

    List<DataSetFolder> getAllByParentFolderIsNull();

    @Query(value = "WITH folders(DATASET_FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select DATASET_FOLDER_ID, parent_id, 1 AS LEVEL from repository.DATASET_FOLDER " +
            "where DATASET_FOLDER_ID = :folderId " +
            "union all " +
            "select " +
            "        f.DATASET_FOLDER_ID, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.DATASET_FOLDER f " +
            "inner join folders fs on fs.parent_id = f.DATASET_FOLDER_ID " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select DATASET_FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);
}
