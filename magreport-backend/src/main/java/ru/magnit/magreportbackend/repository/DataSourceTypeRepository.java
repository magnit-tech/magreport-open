package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.datasource.DataSourceType;

public interface DataSourceTypeRepository extends JpaRepository<DataSourceType, Long> {
}
