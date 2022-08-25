package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Long> {

    ScheduleTask findByExpirationCode(UUID expirationCode);

    List<ScheduleTask> findByStatusId(Long status);
    ScheduleTask findByCode(String code);
    List<ScheduleTask> findByReportId(Long reportId);
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM REPOSITORY.SCHEDULE_TASK S " +
                            "WHERE " +
                    "S.EXPIRATION_DATE BETWEEN CURRENT_DATE and :date")

    List<ScheduleTask> findByExpirationDateBefore(LocalDate date);

}
