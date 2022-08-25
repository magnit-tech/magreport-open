package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.DestinationEmail;
import ru.magnit.magreportbackend.domain.schedule.DestinationTypeEnum;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationEmailResponse;
import ru.magnit.magreportbackend.mapper.Mapper;


@Service
@RequiredArgsConstructor
public class DestinationEmailResponseMapper implements Mapper<DestinationEmailResponse, DestinationEmail> {
    @Override
    public DestinationEmailResponse from(DestinationEmail source) {
        return new DestinationEmailResponse()
                .setId(source.getId())
                .setType(DestinationTypeEnum.getById(source.getType().getId()))
                .setScheduleTaskId(source.getScheduleTask().getId())
                .setValue(source.getValue())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

}
