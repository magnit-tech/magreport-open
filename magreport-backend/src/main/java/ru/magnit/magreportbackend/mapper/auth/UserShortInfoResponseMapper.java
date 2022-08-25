package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.user.UserShortInfoResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class UserShortInfoResponseMapper implements Mapper<UserShortInfoResponse, User> {
    @Override
    public UserShortInfoResponse from(User source) {
        return new UserShortInfoResponse()
                .setLogin(source.getName())
                .setFullName(String.format("%s %s %s", source.getLastName(),source.getFirstName(),source.getPatronymic()));
    }
}
