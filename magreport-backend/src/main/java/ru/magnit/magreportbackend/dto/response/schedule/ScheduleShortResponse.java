package ru.magnit.magreportbackend.dto.response.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScheduleShortResponse {

    private Long id;
    private ScheduleTypeEnum type;
    private String name;

}
