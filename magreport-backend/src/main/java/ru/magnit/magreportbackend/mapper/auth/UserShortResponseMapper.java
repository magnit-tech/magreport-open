package ru.magnit.magreportbackend.mapper.auth;

import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
public class UserShortResponseMapper implements Mapper<UserShortResponse, User> {

    @Override
    public UserShortResponse from(User source) {
        return new UserShortResponse(source.getId(), source.getName());
    }
}
