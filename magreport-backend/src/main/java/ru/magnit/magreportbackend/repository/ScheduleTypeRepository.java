package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.schedule.ScheduleType;

public interface ScheduleTypeRepository extends JpaRepository<ScheduleType, Long> {
}
