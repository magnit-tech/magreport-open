package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.Schedule;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleShortResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ScheduleShortResponseMapper implements Mapper<ScheduleShortResponse, Schedule> {

    @Override
    public ScheduleShortResponse from(Schedule source) {

        return new ScheduleShortResponse()
            .setId(source.getId())
            .setType(ScheduleTypeEnum.getById(source.getType().getId()))
            .setName(source.getName());
    }
}
