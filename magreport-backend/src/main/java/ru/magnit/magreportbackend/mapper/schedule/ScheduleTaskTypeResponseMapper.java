package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskType;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ScheduleTaskTypeResponseMapper implements Mapper<ScheduleTaskTypeResponse, ScheduleTaskType> {
    @Override
    public ScheduleTaskTypeResponse from(ScheduleTaskType source) {
        return new ScheduleTaskTypeResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified( source.getModifiedDateTime());
    }
}
