package ru.magnit.magreportbackend.domain.schedule;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleCalendarInfoTest {

    private static final LocalDate ID = LocalDate.parse("2021-10-01");

    private static final int DAY = 1;
    private static final int MONTH = 10;
    private static final int YEAR = 2021;
    private static final int DAY_WEEK = 5;
    private static final int DAY_END_MONTH = 31;
    private static final int WEEK_MONTH = 1;
    private static final int WEEK_END_MONTH = 5;

    @Test
    void testDateConstructor() {

        var scheduleCalendar = new ScheduleCalendarInfo(ID);

        assertEquals(ID, scheduleCalendar.getDate());
        assertEquals(DAY, scheduleCalendar.getDay());
        assertEquals(MONTH, scheduleCalendar.getMonth());
        assertEquals(YEAR, scheduleCalendar.getYear());
        assertEquals(DAY_WEEK, scheduleCalendar.getDayWeek());
        assertEquals(DAY_END_MONTH, scheduleCalendar.getNumDayEndMonth());
        assertEquals(WEEK_MONTH, scheduleCalendar.getNumWeekMonth());
        assertEquals(WEEK_END_MONTH, scheduleCalendar.getNumWeekEndMonth());

    }
}
