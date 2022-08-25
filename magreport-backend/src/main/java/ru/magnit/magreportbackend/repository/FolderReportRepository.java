package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;

import java.util.List;

public interface FolderReportRepository extends JpaRepository<FolderReport, Long> {

    boolean existsByFolderId(Long folderId);

    void deleteByFolderIdAndReportId(Long folderId, Long reportId);

    boolean existsByFolderIdAndReportId(Long folderId, Long reportId);

    List<FolderReport> findByReportId(Long reportId);
}
