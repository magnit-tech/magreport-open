package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.schedule.DestinationUser;

public interface DestinationUserRepository extends JpaRepository<DestinationUser,Long> {
    void deleteByScheduleTaskId(Long taskId);
}
