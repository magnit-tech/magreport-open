package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.user.RoleDomainGroup;

import java.util.List;

@Repository
public interface RoleDomainGroupRepository extends JpaRepository<RoleDomainGroup, Long> {

    // Hack to delete bidirectional link
    @Modifying
    @Query("delete from ROLE_DOMAIN_GROUP t where t.id in ?1")
    void delete(List<Long> id);
}
