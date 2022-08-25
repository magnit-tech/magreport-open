package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobFilter;

import java.util.List;

public interface ReportJobFilterRepository  extends JpaRepository<ReportJobFilter, Long> {

     void deleteByScheduleTaskId (Long taskId);
     List<ReportJobFilter> findAllByScheduleTaskId (Long taskId);
}
