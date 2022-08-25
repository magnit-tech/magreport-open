package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserRole;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserResponseMapper implements Mapper<UserResponse, User> {

    private final Mapper<RoleResponse, Role> roleResponseMapper;

    @Override
    public UserResponse from(User source) {
        return mapBaseProperties(source);
    }

    private UserResponse mapBaseProperties(User source) {
        return new UserResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFirstName(source.getFirstName())
                .setPatronymic(source.getPatronymic())
                .setLastName(source.getLastName())
                .setEmail(source.getEmail())
                .setStatus(UserStatusEnum.getById(source.getUserStatus().getId()))
                .setRoles(roleResponseMapper.from(source.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toUnmodifiableList())))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
