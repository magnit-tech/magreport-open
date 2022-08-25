package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.asm.ExternalAuth;

import java.util.List;

public interface ExternalAuthRepository extends JpaRepository<ExternalAuth, Long> {

    List<ExternalAuth> findAllByIdIn(List<Long> idList);
}
