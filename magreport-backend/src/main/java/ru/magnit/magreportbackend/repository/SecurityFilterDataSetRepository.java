package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;

import java.util.List;

public interface SecurityFilterDataSetRepository extends JpaRepository<SecurityFilterDataSet, Long> {

    void deleteAllBySecurityFilterId(Long securityFilterId);

    List<SecurityFilterDataSet> findAllByDataSetId(Long dataSetId);
}
