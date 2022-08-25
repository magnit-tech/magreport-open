package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.user.UserRole;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    void deleteUserRoleByUserIdAndRoleIdAndUserRoleTypeId(Long userId, Long roleId, Long userRoleTypeId);
    void deleteByRoleId(Long roleId);
    void deleteByRoleIdAndUserIdIn(Long roleId, List<Long> users);
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
    void deleteAllByIdIn(List<Long> ids);
}
