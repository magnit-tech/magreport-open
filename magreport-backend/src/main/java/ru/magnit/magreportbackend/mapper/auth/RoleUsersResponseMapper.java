package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserRole;
import ru.magnit.magreportbackend.dto.response.user.RoleUsersResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleUsersResponseMapper implements Mapper<RoleUsersResponse, Role> {

    private final Mapper<UserResponse, User> userResponseMapper;

    @Override
    public RoleUsersResponse from(Role source) {

        return new RoleUsersResponse()
                .setId(source.getId())
                .setUsers(userResponseMapper.from(source.getUserRoles()
                        .stream()
                        .map(UserRole::getUser)
                        .collect(Collectors.toList())));
    }
}
