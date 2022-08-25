package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.report.PivotFieldType;

public interface PivotFieldTypeRepository extends JpaRepository<PivotFieldType, Long> {
}
