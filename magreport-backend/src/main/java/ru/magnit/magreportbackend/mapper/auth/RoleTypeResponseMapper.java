package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleTypeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class RoleTypeResponseMapper implements Mapper<RoleTypeResponse, RoleType> {

    private final Mapper<RoleResponse, Role> roleResponseMapper;

    @Override
    public RoleTypeResponse from(RoleType source) {

        return new RoleTypeResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setRoles(roleResponseMapper.from(source.getRoles()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }

    @Override
    public RoleTypeResponse shallowMap(RoleType source) {

        return new RoleTypeResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
