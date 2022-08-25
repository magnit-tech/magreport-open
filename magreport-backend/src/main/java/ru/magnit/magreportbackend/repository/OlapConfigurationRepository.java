package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;

public interface OlapConfigurationRepository extends JpaRepository<OlapConfiguration, Long> {
}
