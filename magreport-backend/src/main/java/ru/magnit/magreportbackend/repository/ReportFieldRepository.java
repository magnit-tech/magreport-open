package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.report.ReportField;

import java.util.List;

@Repository
public interface ReportFieldRepository extends JpaRepository<ReportField, Long> {

    void deleteAllByReportId(Long reportId);
    void deleteAllByIdIn(List<Long> idList);


    ReportField getFirstByReportIdOrderByOrdinalDesc(Long reportId);
}
