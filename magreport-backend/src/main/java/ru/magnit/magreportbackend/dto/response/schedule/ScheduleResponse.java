package ru.magnit.magreportbackend.dto.response.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScheduleResponse {

    private Long id;
    private String name;
    private String description;
    private ScheduleTypeEnum type;
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
    private List<ScheduleTaskShortResponse> tasks;

    private String userName;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime planStartDate;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime modified;

}
