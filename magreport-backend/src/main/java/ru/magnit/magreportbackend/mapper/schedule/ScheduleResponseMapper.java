package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.Schedule;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ScheduleResponseMapper implements Mapper<ScheduleResponse, Schedule> {

    private final ScheduleTaskShortResponseMapper mapperTask;

    @Override
    public ScheduleResponse from(Schedule source) {
        return new ScheduleResponse()
            .setId(source.getId())
            .setName(source.getName())
            .setDescription(source.getDescription())
            .setType(ScheduleTypeEnum.getById(source.getType().getId()))
            .setSecond(source.getSecond())
            .setMinute(source.getMinute())
            .setHour(source.getHour())
            .setDay(source.getDay())
            .setDayWeek(source.getDayWeek())
            .setDayEndMonth(source.getDayEndMonth())
            .setMonth(source.getMonth())
            .setYear(source.getYear())
            .setWeekMonth(source.getWeekMonth())
            .setWeekEndMonth(source.getWeekEndMonth())
            .setTasks(mapperTask.from(source.getScheduleTasks()))
            .setCreated(source.getCreatedDateTime())
            .setModified(source.getModifiedDateTime())
            .setUserName(source.getUser().getName())
            .setPlanStartDate(source.getPlanStartDate());
    }
}
