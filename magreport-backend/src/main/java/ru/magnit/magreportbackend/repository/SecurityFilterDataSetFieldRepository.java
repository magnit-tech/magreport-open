package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;

public interface SecurityFilterDataSetFieldRepository extends JpaRepository<SecurityFilterDataSetField, Long> {

    void deleteAllBySecurityFilterId(Long securityFilterId);
}
