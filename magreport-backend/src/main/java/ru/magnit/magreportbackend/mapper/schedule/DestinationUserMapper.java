package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.DestinationType;
import ru.magnit.magreportbackend.domain.schedule.DestinationUser;
import ru.magnit.magreportbackend.dto.request.schedule.DestinationUserAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DestinationUserMapper implements Mapper<DestinationUser, DestinationUserAddRequest> {

    @Override
    public DestinationUser from(DestinationUserAddRequest source) {
        return new DestinationUser()
                .setType(source.getTypeId() == null ? new DestinationType(0L) : new DestinationType(source.getTypeId()))
                .setValue(source.getUserName());
    }
}
