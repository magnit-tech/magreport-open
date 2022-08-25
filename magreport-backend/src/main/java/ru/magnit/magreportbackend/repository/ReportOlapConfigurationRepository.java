package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportOlapConfigurationRepository extends JpaRepository<ReportOlapConfiguration, Long> {

    List<ReportOlapConfiguration> getReportOlapConfigurationByCreatorIdAndCreatedDateTimeAfter(Long authorId, LocalDateTime dateTime);


    @Modifying
    @Query(value = "update REPOSITORY.REPORT_OLAP_CONFIGURATION set IS_DEFAULT = false where REPORT_ID = :reportId and USER_ID = :userId and REPORT_JOB_ID is null and IS_DEFAULT = true ",
            nativeQuery = true)
   void disableDefaultFlagForReportAndUser(@Param("reportId") Long reportId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "update REPOSITORY.REPORT_OLAP_CONFIGURATION set IS_DEFAULT = false where REPORT_ID = :reportId and USER_ID is null and REPORT_JOB_ID is null and IS_DEFAULT = true ",
            nativeQuery = true)
    void disableDefaultFlagForReport(@Param("reportId") Long reportId);
    List<ReportOlapConfiguration> getReportOlapConfigurationsByOlapConfigurationId(Long olapConfiguration);

    @Query(value = "select * from REPOSITORY.REPORT_OLAP_CONFIGURATION where REPORT_ID = :reportId and USER_ID is null and REPORT_JOB_ID is null ", nativeQuery = true)
    List<ReportOlapConfiguration> getROCByReport(@Param("reportId") Long reportId);

    @Query(value = "select * from REPOSITORY.REPORT_OLAP_CONFIGURATION where REPORT_ID = :reportId and USER_ID = :userId and REPORT_JOB_ID is null ", nativeQuery = true)
    List<ReportOlapConfiguration> getROCByReportAndUser(@Param("reportId") Long reportId, @Param("userId") Long userId);

    @Query(value = "select * from REPOSITORY.REPORT_OLAP_CONFIGURATION where USER_ID = :userId and REPORT_JOB_ID = :jobId and IS_CURRENT = false", nativeQuery = true)
    List<ReportOlapConfiguration> getROCByUserAndJob(@Param("userId") Long userId, @Param("jobId") Long jobId);

    @Query(value = "select * from REPOSITORY.REPORT_OLAP_CONFIGURATION where REPORT_JOB_ID = :jobId and IS_SHARED = true", nativeQuery = true)
    List<ReportOlapConfiguration> getSharedROCByJob(@Param("jobId") Long jobId);

    @Query(value = "select * from REPOSITORY.REPORT_OLAP_CONFIGURATION where  REPORT_JOB_ID = :jobId and IS_CURRENT = true and USER_ID = :userId ", nativeQuery = true)
    Optional<ReportOlapConfiguration> findCurrentReportOlapConfiguration(@Param("jobId") Long jobId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "delete from  REPOSITORY.REPORT_OLAP_CONFIGURATION where  REPORT_JOB_ID = :jobId and IS_CURRENT = true and USER_ID = :userId ",nativeQuery = true)
    void deleteCurrentByUserAndJob(@Param("jobId") Long jobId, @Param("userId") Long userId);

}
