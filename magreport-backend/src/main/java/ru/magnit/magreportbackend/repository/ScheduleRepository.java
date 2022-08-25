package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM REPOSITORY.SCHEDULE S WHERE S.PLAN_START_DATE BETWEEN S.LAST_START_DATE and :date")
    List<Schedule> getTaskForDate(@Param("date") LocalDateTime date);
}
