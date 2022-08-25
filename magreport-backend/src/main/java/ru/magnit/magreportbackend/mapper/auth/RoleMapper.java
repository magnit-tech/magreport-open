package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class RoleMapper implements Mapper<Role, RoleAddRequest> {

    @Override
    public Role from(RoleAddRequest source) {

        return new Role()
                .setId(source.getId())
                .setRoleType(new RoleType(source.getTypeId()))
                .setName(source.getName())
                .setDescription(source.getDescription());
    }
}
