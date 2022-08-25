package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.schedule.DestinationEmail;

public interface DestinationEmailRepository extends JpaRepository<DestinationEmail,Long> {
    void deleteByScheduleTaskId(Long taskId);
}
