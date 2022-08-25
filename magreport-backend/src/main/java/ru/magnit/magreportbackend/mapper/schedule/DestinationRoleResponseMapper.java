package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.DestinationRole;
import ru.magnit.magreportbackend.domain.schedule.DestinationTypeEnum;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationRoleResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DestinationRoleResponseMapper implements Mapper<DestinationRoleResponse, DestinationRole> {
    @Override
    public DestinationRoleResponse from(DestinationRole source) {
        return new DestinationRoleResponse()
                .setId(source.getId())
                .setType(source.getType() == null ? DestinationTypeEnum.REPORT : DestinationTypeEnum.getById(source.getType().getId()))
                .setScheduleTaskId(source.getScheduleTask().getId())
                .setRoleId(source.getValue())
                .setName(source.getName())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
