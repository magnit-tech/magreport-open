package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.user.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = """
            SELECT DISTINCT A.* FROM REPOSITORY.ROLE A
            INNER JOIN REPOSITORY.ROLE_DOMAIN_GROUP B ON B.ROLE_ID=A.ROLE_ID
            INNER JOIN REPOSITORY.DOMAIN_GROUP C ON C.DOMAIN_GROUP_ID=B.DOMAIN_GROUP_ID
            WHERE C.NAME IN (:domain_group_names)
            """, nativeQuery = true)
    List<Role> getRolesByDomainGroupsList(@Param("domain_group_names") List<String> domainGroupNames);

    Role getByName(String roleName);

    void deleteAllByName(String roleName);

    List<Role> findAllByNameIn(List<String> roleNames);

    boolean existsByName(String name);
}
