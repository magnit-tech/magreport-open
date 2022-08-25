package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldType;

public interface FilterFieldTypeRepository extends JpaRepository<FilterFieldType, Long> {
}
