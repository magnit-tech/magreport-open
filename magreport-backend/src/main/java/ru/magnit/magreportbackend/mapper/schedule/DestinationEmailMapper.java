package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.DestinationEmail;
import ru.magnit.magreportbackend.domain.schedule.DestinationType;
import ru.magnit.magreportbackend.dto.request.schedule.DestinationEmailAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DestinationEmailMapper implements Mapper<DestinationEmail, DestinationEmailAddRequest> {
    @Override
    public DestinationEmail from(DestinationEmailAddRequest source) {
        return new DestinationEmail()
                .setValue(source.getEmailValue())
                .setType(source.getTypeId() == null ? new DestinationType(0L) : new DestinationType(source.getTypeId()));
    }
}
