package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.schedule.DestinationRole;

public interface DestinationRoleRepository extends JpaRepository<DestinationRole,Long> {
    void deleteByScheduleTaskId(Long taskId);
}
