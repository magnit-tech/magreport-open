package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportJobRepository extends JpaRepository<ReportJob, Long> {

    ReportJob getFirstByStatusIdAndStateIdOrderById(Long statusId, Long stateId);

    List<ReportJob> getAllByUserId(Long userId);

    List<ReportJob> getAllByIdIn(List<Long> idList);

    ReportJob getFirstByUserIdAndReportIdOrderByIdDesc(Long userId, Long reportId);

    List<ReportJob> findAllByCreatedDateTimeBefore(LocalDateTime lastDateTime);

    List<ReportJob> findAllByStatusIdAndStateId(Long statusId, Long stateId);

    @Query(value = "SELECT RJ.REPORT_JOB_ID," +
            "       RJ.REPORT_ID," +
            "       R.NAME AS reportName," +
            "       RJ.USER_ID," +
            "       U.NAME AS userName," +
            "       RJ.REPORT_JOB_STATUS_ID," +
            "       RJ.REPORT_JOB_STATE_ID," +
            "       RJ.MESSAGE," +
            "       RJ.ROW_COUNT," +
            "       RJ.CREATED," +
            "       RJ.MODIFIED," +
            "       ET.EXCEL_TEMPLATE_ID," +
            "       ET.NAME," +
            "       ET.DESCRIPTION," +
            "       RET.IS_DEFAULT " +
            "FROM REPOSITORY.REPORT_JOB RJ" +
            "         JOIN REPOSITORY.REPORT R ON R.REPORT_ID = RJ.REPORT_ID" +
            "         JOIN REPOSITORY.USERS U ON U.USER_ID = RJ.USER_ID" +
            "         JOIN REPOSITORY.REPORT_EXCEL_TEMPLATE RET ON RET.REPORT_ID = RJ.REPORT_ID" +
            "         JOIN REPOSITORY.EXCEL_TEMPLATE ET ON ET.EXCEL_TEMPLATE_ID = RET.EXCEL_TEMPLATE_ID",
            nativeQuery = true)
    List<Tuple> getAllJobWithTemplate();

    @Query(value = "SELECT * FROM REPOSITORY.REPORT_JOB RJ " +
            "WHERE RJ.USER_ID = :userId or " +
            "RJ.REPORT_JOB_ID in (SELECT RJU.REPORT_JOB_ID from REPOSITORY.REPORT_JOB_USER RJU WHERE RJU.USER_ID = :userId )", nativeQuery = true)
    List<ReportJob> getAllByUserId2 (@Param("userId") Long userId);


}
