package ru.magnit.magreportbackend.mapper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.domain.user.UserStatusEnum;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class UserViewMapper implements Mapper<UserView, User> {

    @Override
    public UserView from(User source) {
        return mapBaseProperties(source);
    }

    private UserView mapBaseProperties(User source) {
        return new UserView()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFirstName(source.getFirstName())
                .setPatronymic(source.getPatronymic())
                .setLastName(source.getLastName())
                .setEmail(source.getEmail())
                .setStatus(UserStatusEnum.getById(source.getUserStatus().getId()));
    }
}
