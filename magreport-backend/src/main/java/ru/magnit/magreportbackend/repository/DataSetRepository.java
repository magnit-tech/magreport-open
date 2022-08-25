package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.dataset.DataSet;

import java.util.List;

@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
    Boolean existsByFolderId(Long folderId);
    List<DataSet> getAllByIdIn(List<Long> ids);
}
