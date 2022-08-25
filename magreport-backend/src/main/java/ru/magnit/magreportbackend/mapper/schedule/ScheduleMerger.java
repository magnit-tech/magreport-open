package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.Schedule;
import ru.magnit.magreportbackend.domain.schedule.ScheduleType;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;

@Service
@RequiredArgsConstructor
public class ScheduleMerger implements Merger<Schedule, ScheduleAddRequest> {
    @Override
    public Schedule merge(Schedule target, ScheduleAddRequest source) {

        return target
                .setType(new ScheduleType(source.getScheduleTypeId()))
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
                .setDifferenceTime(source.getDifferenceTime())
                .setName(source.getName())
                .setDescription(source.getDescription());

    }
}
