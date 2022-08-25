package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.serversettings.ServerMailTemplate;

public interface ServerMailTemplateRepository extends JpaRepository<ServerMailTemplate, Long> {

    ServerMailTemplate findByCode(String code);
}
