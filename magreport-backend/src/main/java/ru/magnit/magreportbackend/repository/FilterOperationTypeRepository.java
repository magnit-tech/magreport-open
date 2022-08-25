package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;

public interface FilterOperationTypeRepository extends JpaRepository<FilterOperationType, Long> {
}
