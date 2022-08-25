package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateField;

public interface FilterTemplateFieldRepository extends JpaRepository<FilterTemplateField, Long> {

    void deleteAllByFilterTemplateId(Long filterId);
}
