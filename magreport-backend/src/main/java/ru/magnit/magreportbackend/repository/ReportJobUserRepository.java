package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUser;

import java.util.List;
import java.util.Optional;

public interface ReportJobUserRepository extends JpaRepository<ReportJobUser,Long> {

    @Query(value = "select * from REPOSITORY.REPORT_JOB_USER " +
            "where " +
            "REPORT_JOB_ID = :jobId and " +
            "USER_ID = :userId and " +
            "REPORT_JOB_USER_TYPE_ID = :typeId",
            nativeQuery = true)
    Optional<ReportJobUser> findSharedJob(@Param("jobId") Long jobId, @Param("userId") Long userId, @Param("typeId") Long typeId);

    @Query(value = "select * from REPOSITORY.REPORT_JOB_USER  where REPORT_JOB_ID = :jobId", nativeQuery = true)
    List<ReportJobUser> findAllForJob(@Param("jobId") Long jobId);

}
