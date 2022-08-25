package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;

import java.util.List;

@Repository
public interface FilterReportRepository extends JpaRepository<FilterReport, Long> {

    List<FilterReport> findFilterReportByFilterInstanceId (Long filterInstanceId);
}
