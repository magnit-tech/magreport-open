package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.excel.ReportExcelTemplate;

import java.util.List;

public interface ReportExcelTemplateRepository extends JpaRepository<ReportExcelTemplate, Long> {

    ReportExcelTemplate getTopByReportIdAndIsDefaultIsTrue(Long reportId);

    @Modifying
    @Query("UPDATE REPORT_EXCEL_TEMPLATE set isDefault = true where " +
            "report.id not in (select distinct report.id from REPORT_EXCEL_TEMPLATE where isDefault = true) and " +
            "excelTemplate.id = 1")
    void setDefaultExcelTemplateInsteadOfRemote();
    @Modifying
    @Query("delete from REPORT_EXCEL_TEMPLATE ret " +
            "where ret.report.id = :reportId")
    void removeReportExcelTemplate(@Param("reportId") Long reportId);

    List<ReportExcelTemplate> findAllByReportId(Long reportId);

}
