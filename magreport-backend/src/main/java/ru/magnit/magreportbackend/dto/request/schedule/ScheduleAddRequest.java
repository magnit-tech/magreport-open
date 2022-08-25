package ru.magnit.magreportbackend.dto.request.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor

public class ScheduleAddRequest {
    private Long id;
    private Long scheduleTypeId;
    private Long second;
    private Long minute;
    private Long hour;
    private Long day;
    private Long dayWeek;
    private Long month;
    private Long year;
    private Long dayEndMonth;
    private Long weekMonth;
    private Long weekEndMonth;
    private String name;
    private String description;
    private Long differenceTime;
}
