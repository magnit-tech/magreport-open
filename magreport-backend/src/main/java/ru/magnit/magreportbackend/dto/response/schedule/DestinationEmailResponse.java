package ru.magnit.magreportbackend.dto.response.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.schedule.DestinationTypeEnum;

import java.time.LocalDateTime;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DestinationEmailResponse {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    DestinationTypeEnum type;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Long scheduleTaskId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String value;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime modified;

}
