package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.magnit.magreportbackend.domain.schedule.ScheduleCalendarInfo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleCalendarInfoRepository extends JpaRepository<ScheduleCalendarInfo, LocalDate> {

    ScheduleCalendarInfo findFirstByOrderByDateDesc();

    @Modifying
    @Transactional
    @Query(" delete from SCHEDULE_CALENDAR_INFO sci where sci.date < :currentDate")
    void removeOldDate(@Param("currentDate") LocalDate currentDay);


    @Query(nativeQuery = true, value = "SELECT SCI.* FROM REPOSITORY.SCHEDULE_CALENDAR_INFO SCI WHERE " +
            "SCI.DATE >= CURRENT_DATE")
    List<ScheduleCalendarInfo> getDatesForEveryDaySchedule();

    @Query(nativeQuery = true, value = "SELECT SCI.* FROM REPOSITORY.SCHEDULE_CALENDAR_INFO SCI " +
            "WHERE " +
            "SCI.DATE >= CURRENT_DATE AND " +
            "(SELECT S.DAY_WEEK from REPOSITORY.SCHEDULE S where s.SCHEDULE_ID = :scheduleId) = SCI.DAY_WEEK")
    List<ScheduleCalendarInfo> getDatesForEveryWeekSchedule(@Param("scheduleId") Long scheduleId);

    @Query(nativeQuery = true, value = "SELECT SCI.* FROM REPOSITORY.SCHEDULE_CALENDAR_INFO SCI " +
            "WHERE " +
            "SCI.DATE >= CURRENT_DATE AND " +
            "(SELECT S.DAY_ID from REPOSITORY.SCHEDULE S where s.SCHEDULE_ID = :scheduleId) = SCI.DAY_ID")
    List<ScheduleCalendarInfo> getDatesForEveryMonthSchedule(@Param("scheduleId") Long scheduleId);

    @Query(nativeQuery = true, value = "SELECT SCI.* FROM REPOSITORY.SCHEDULE_CALENDAR_INFO SCI " +
            "WHERE " +
            "SCI.DATE >= CURRENT_DATE AND " +
            "(SELECT S.DAY_END_MONTH from REPOSITORY.SCHEDULE S where s.SCHEDULE_ID = :scheduleId) = SCI.DAY_END_MONTH")
    List<ScheduleCalendarInfo> getDatesForDayEndMonthSchedule(@Param("scheduleId") Long scheduleId);

    @Query(nativeQuery = true, value = "SELECT SCI.* FROM REPOSITORY.SCHEDULE_CALENDAR_INFO SCI " +
            "WHERE " +
            "SCI.DATE > CURRENT_DATE AND " +
            "(SELECT S.DAY_WEEK from REPOSITORY.SCHEDULE S where s.SCHEDULE_ID = :scheduleId) = SCI.DAY_WEEK AND " +
            "(SELECT S.WEEK_MONTH from REPOSITORY.SCHEDULE S where s.SCHEDULE_ID = :scheduleId) = SCI.WEEK_MONTH")
    List<ScheduleCalendarInfo> getDatesForWeekMonthSchedule(@Param("scheduleId") Long scheduleId);

    @Query(nativeQuery = true, value = "SELECT SCI.* FROM REPOSITORY.SCHEDULE_CALENDAR_INFO SCI " +
            "WHERE " +
            "SCI.DATE >= CURRENT_DATE AND " +
            "(SELECT S.DAY_WEEK from REPOSITORY.SCHEDULE S where s.SCHEDULE_ID = :scheduleId) = SCI.DAY_WEEK AND " +
            "(SELECT S.WEEK_END_MONTH from REPOSITORY.SCHEDULE S where s.SCHEDULE_ID = :scheduleId) = SCI.WEEK_END_MONTH")
    List<ScheduleCalendarInfo> getDatesForWeekEndMonthSchedule(@Param("scheduleId") Long scheduleId);
}
