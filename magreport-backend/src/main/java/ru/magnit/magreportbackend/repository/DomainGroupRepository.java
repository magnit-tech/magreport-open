package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.user.DomainGroup;

import java.util.List;

@Repository
public interface DomainGroupRepository extends JpaRepository<DomainGroup, Long> {

    List<DomainGroup> getAllByNameIn(List<String> groups);
}
