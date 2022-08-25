package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.user.RoleType;

public interface RoleTypeRepository extends JpaRepository<RoleType, Long> {
}
