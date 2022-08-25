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

public class ScheduleCalendarAddRequest {

    Long countDays;

    public ScheduleCalendarAddRequest(Long countDays) {
        this.countDays = countDays;
    }
}
