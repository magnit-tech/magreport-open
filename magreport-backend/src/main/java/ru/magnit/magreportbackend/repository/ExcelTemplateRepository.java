package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;

import java.util.List;

public interface ExcelTemplateRepository extends JpaRepository<ExcelTemplate, Long> {
    boolean existsByFolderId(Long folderId);
    List<ExcelTemplate> getAllByIdIn(List<Long> ids);
}
