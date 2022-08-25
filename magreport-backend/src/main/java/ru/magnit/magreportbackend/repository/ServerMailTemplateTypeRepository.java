package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.serversettings.ServerMailTemplateType;

public interface ServerMailTemplateTypeRepository extends JpaRepository<ServerMailTemplateType, Long> {
}
