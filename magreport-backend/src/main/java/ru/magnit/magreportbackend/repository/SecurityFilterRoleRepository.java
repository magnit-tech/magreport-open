package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;

import java.util.List;

@Repository
public interface SecurityFilterRoleRepository extends JpaRepository<SecurityFilterRole, Long> {

    void deleteAllBySecurityFilterIdAndRoleId(Long securityFilterId, Long roleId);

    void deleteBySecurityFilterId(Long securityFilterId);

    List<SecurityFilterRole> getAllByRoleId(Long roleId);
}
