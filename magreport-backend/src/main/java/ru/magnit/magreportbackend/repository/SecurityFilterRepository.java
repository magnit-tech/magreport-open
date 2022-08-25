package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;

import java.util.List;

public interface SecurityFilterRepository extends JpaRepository<SecurityFilter, Long> {
    Boolean existsByFolderId(Long id);
    List<SecurityFilter> getAllByIdIn(List<Long> ids);
}
