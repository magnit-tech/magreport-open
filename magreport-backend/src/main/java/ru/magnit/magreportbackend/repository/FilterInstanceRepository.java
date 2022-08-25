package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;

import java.util.List;

public interface FilterInstanceRepository extends JpaRepository<FilterInstance, Long> {
    boolean existsByFolderId(Long folderId);
    List<FilterInstance> findByDataSetId (Long dataSetId);
    List<FilterInstance> findAllByCode (String code);
    List<FilterInstance> getAllByIdIn(List<Long> ids);
}
