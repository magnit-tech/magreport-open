package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.DestinationRole;
import ru.magnit.magreportbackend.domain.schedule.DestinationType;
import ru.magnit.magreportbackend.dto.request.schedule.DestinationRoleAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DestinationRoleMapper implements Mapper<DestinationRole, DestinationRoleAddRequest> {
    @Override
    public DestinationRole from(DestinationRoleAddRequest source) {
        return new DestinationRole()
                .setType(source.getTypeId() == null ? new DestinationType(0L) : new DestinationType(source.getTypeId()))
                .setValue(source.getRoleId())
                .setName(source.getName());
    }
}
