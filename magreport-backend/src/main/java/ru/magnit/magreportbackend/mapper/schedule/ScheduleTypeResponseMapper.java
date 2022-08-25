package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleType;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ScheduleTypeResponseMapper implements Mapper<ScheduleTypeResponse, ScheduleType> {
    @Override
    public ScheduleTypeResponse from(ScheduleType source) {
        return new ScheduleTypeResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified( source.getModifiedDateTime());
    }
}
