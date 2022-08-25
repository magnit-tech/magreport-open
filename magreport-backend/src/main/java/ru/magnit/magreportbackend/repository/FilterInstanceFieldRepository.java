package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;

public interface FilterInstanceFieldRepository extends JpaRepository<FilterInstanceField, Long> {

    void deleteAllByInstanceId(Long filterId);
}
