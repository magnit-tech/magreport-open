package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterType;

public interface FilterTypeRepository extends JpaRepository<FilterType, Long> {
}
