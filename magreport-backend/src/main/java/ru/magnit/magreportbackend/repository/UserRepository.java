package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserStatus;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByName(String userName);

    boolean existsByName(String userName);

    List<User> getAllByIdIn(List<Long> users);

    @Modifying
    @Query("update USERS set userStatus=:newUserStatus")
    void setStatusToAllUsers(@Param("newUserStatus") UserStatus status);

    @Query(value = "select u.* from REPOSITORY.USER_ROLE UR " +
            "join REPOSITORY.USERS U on UR.USER_ID = U.USER_ID " +
            "where UR.ROLE_ID = :roleId and USER_STATUS_ID != :statusId", nativeQuery = true)
    List<User> getUsersByRole(@Param("roleId") Long roleId, @Param("statusId") Long statusId);

    @Query(value = "select u.* from REPOSITORY.USERS u where USER_STATUS_ID != :statusId ",nativeQuery = true)
    List<User> getUserByStatusIsNotEquals(@Param("statusId") Long statusId);

}
