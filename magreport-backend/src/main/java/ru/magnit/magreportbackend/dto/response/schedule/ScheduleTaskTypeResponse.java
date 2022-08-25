package ru.magnit.magreportbackend.dto.response.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ScheduleTaskTypeResponse {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String description;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime modified;


}
