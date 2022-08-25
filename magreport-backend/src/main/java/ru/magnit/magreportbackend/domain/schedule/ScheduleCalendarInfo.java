package ru.magnit.magreportbackend.domain.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "SCHEDULE_CALENDAR_INFO")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ScheduleCalendarInfo {

    @Id
    LocalDate date;

    @Column(name = "DAY_ID")
    private int day;

    @Column(name = "DAY_WEEK")
    private int dayWeek;

    @Column(name = "MONTH_ID")
    private int month;

    @Column(name = "YEAR_ID")
    private int year;

    @Column(name = "DAY_END_MONTH")
    private int numDayEndMonth;

    @Column(name = "WEEK_MONTH")
    private int numWeekMonth;

    @Column(name = "WEEK_END_MONTH")
    private int numWeekEndMonth;

    public ScheduleCalendarInfo(LocalDate date) {
        this.date = date;
        day = date.getDayOfMonth();
        dayWeek = date.getDayOfWeek().getValue();
        month = date.getMonthValue();
        year = date.getYear();
        numDayEndMonth = date.lengthOfMonth() - date.getDayOfMonth() + 1;
        numWeekMonth = getNumWeekDayMonth(date);
        numWeekEndMonth = getNumWeekDayMonthEnd(date);
    }

    private int getNumWeekDayMonth(LocalDate currentDate) {
        int numWeekDayMonth = 1;
        int dayDate = currentDate.getDayOfMonth();
        while (dayDate > 7) {
            dayDate -= 7;
            numWeekDayMonth++;
        }
        return numWeekDayMonth;

    }

    private int getNumWeekDayMonthEnd(LocalDate currentDate) {

        int numWeekDayMonthEnd = 1;
        int lastDayMonth = currentDate.lengthOfMonth();

        while (currentDate.getDayOfMonth() <= lastDayMonth - 7) {
            lastDayMonth -= 7;
            numWeekDayMonthEnd++;
        }
        return numWeekDayMonthEnd;
    }
}
