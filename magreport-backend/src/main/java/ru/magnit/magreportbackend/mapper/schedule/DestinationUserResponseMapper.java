package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.DestinationTypeEnum;
import ru.magnit.magreportbackend.domain.schedule.DestinationUser;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationUserResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DestinationUserResponseMapper implements Mapper<DestinationUserResponse, DestinationUser> {
    @Override
    public DestinationUserResponse from(DestinationUser source) {
        return new DestinationUserResponse()
                .setId(source.getId())
                .setType(source.getType() == null ? DestinationTypeEnum.REPORT : DestinationTypeEnum.getById(source.getType().getId()))
                .setScheduleTaskId(source.getScheduleTask().getId())
                .setUserName(source.getValue())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

}
