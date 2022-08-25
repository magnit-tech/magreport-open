package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;

import java.util.List;

@Repository
public interface DataSourceFolderRepository extends JpaRepository<DataSourceFolder, Long> {

    List<DataSourceFolder> getAllByParentFolderId(Long parentId);

    Boolean existsByParentFolderId(Long parentId);

    DataSourceFolder getFirstByParentFolderIsNull();

    List<DataSourceFolder> getAllByParentFolderIsNull();

    @Query(value = "WITH folders(DATASOURCE_FOLDER_ID, PARENT_ID, LEVEL) as (" +
            "select DATASOURCE_FOLDER_ID, parent_id, 1 AS LEVEL from repository.DATASOURCE_FOLDER " +
            "where DATASOURCE_FOLDER_ID = :folderId " +
            "union all " +
            "select " +
            "        f.DATASOURCE_FOLDER_ID, " +
            "        f.parent_id, " +
            "        LEVEL + 1 AS LEVEL " +
            "from repository.DATASOURCE_FOLDER f " +
            "inner join folders fs on fs.parent_id = f.DATASOURCE_FOLDER_ID " +
            "WHERE LEVEL < 1000 " +
            ") " +
            "select DATASOURCE_FOLDER_ID from folders", nativeQuery = true)
    List<Long> checkRingPath (@Param("folderId") Long folderId);
}
