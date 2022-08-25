package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.datasource.DataSource;

import java.util.List;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    Boolean existsByFolderId(Long folderId);
    List<DataSource> getAllByIdIn(List<Long> ids);
}
