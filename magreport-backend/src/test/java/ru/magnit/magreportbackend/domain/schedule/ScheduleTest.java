package ru.magnit.magreportbackend.domain.schedule;

import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest extends BaseEntityTest {
    private static final Long ID = 1L;
    private static final String NAME = "schedule";
    private static final String DESCRIPTION = "description";
    private static final Long HOUR = 23L;
    private static final Long MINUTE = 59L;
    private static final Long SECOND = 58L;
    private static final Long DAY = 5L;
    private static final Long DAY_WEEK = 6L;
    private static final Long MONTH = 12L;
    private static final Long YEAR = 2000L;
    private static final Long DAY_END_MONTH = 7L;
    private static final Long WEEK_MONTH = 2L;
    private static final Long WEEK_END_MONTH = 3L;
    private static final Long DIFFERENCE_TIME = 0L;
    private static final User USER = new User(-1L);
    private static final ScheduleType SCHEDULE_TYPE = new ScheduleType(0L);
    private static final LocalDateTime PLAN_START_DATE = LocalDateTime.now();
    private static final LocalDateTime LAST_START_DATE = LocalDateTime.now().minusDays(1L);
    private static final List<ScheduleTask> TASKS = Collections.emptyList();

    @Test
    void testNoArgsConstructor() {
        var schedule = getSchedule();

        assertEquals(ID, schedule.getId());
        assertEquals(NAME, schedule.getName());
        assertEquals(DESCRIPTION, schedule.getDescription());
        assertEquals(HOUR, schedule.getHour());
        assertEquals(MINUTE, schedule.getMinute());
        assertEquals(SECOND, schedule.getSecond());
        assertEquals(DAY, schedule.getDay());
        assertEquals(DAY_WEEK, schedule.getDayWeek());
        assertEquals(MONTH, schedule.getMonth());
        assertEquals(YEAR, schedule.getYear());
        assertEquals(DAY_END_MONTH, schedule.getDayEndMonth());
        assertEquals(WEEK_MONTH, schedule.getWeekMonth());
        assertEquals(WEEK_END_MONTH, schedule.getWeekEndMonth());
        assertEquals(DIFFERENCE_TIME, schedule.getDifferenceTime());
        assertEquals(USER, schedule.getUser());
        assertEquals(SCHEDULE_TYPE, schedule.getType());
        assertEquals(PLAN_START_DATE, schedule.getPlanStartDate());
        assertEquals(LAST_START_DATE, schedule.getLastStartDate());
        assertEquals(TASKS, schedule.getScheduleTasks());

    }

    @Test
    void testArgsConstructor() {
        var schedule = new Schedule(ID);

        assertEquals(ID, schedule.getId());
    }

    @Test
    void testEquals() {
        var schedule1 = new Schedule(ID);
        var schedule2 = new Schedule(ID);
        assertEquals(schedule1,schedule2);
    }

    private Schedule getSchedule() {
        return new Schedule()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setHour(HOUR)
                .setMinute(MINUTE)
                .setSecond(SECOND)
                .setDay(DAY)
                .setDayWeek(DAY_WEEK)
                .setMonth(MONTH)
                .setYear(YEAR)
                .setDayEndMonth(DAY_END_MONTH)
                .setWeekMonth(WEEK_MONTH)
                .setWeekEndMonth(WEEK_END_MONTH)
                .setType(SCHEDULE_TYPE)
                .setPlanStartDate(PLAN_START_DATE)
                .setLastStartDate(LAST_START_DATE)
                .setDifferenceTime(DIFFERENCE_TIME)
                .setScheduleTasks(TASKS)
                .setUser(USER);
    }
}