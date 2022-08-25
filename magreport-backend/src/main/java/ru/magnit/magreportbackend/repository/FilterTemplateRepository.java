package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;

public interface FilterTemplateRepository  extends JpaRepository<FilterTemplate, Long> {

    Boolean existsByFolderId(Long folderId);
}
