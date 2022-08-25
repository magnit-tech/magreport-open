package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.reportjobstats.ReportJobStatistics;

@Repository
public interface ReportJobStatisticsRepository extends JpaRepository<ReportJobStatistics, Long> {
}
