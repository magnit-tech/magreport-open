package ru.magnit.magreportbackend.mapper.theme;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.theme.Theme;
import ru.magnit.magreportbackend.domain.theme.ThemeTypeEnum;
import ru.magnit.magreportbackend.dto.response.theme.ThemeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserShortResponseMapper;

@Service
@RequiredArgsConstructor
public class ThemeResponseMapper implements Mapper<ThemeResponse, Theme> {
    private final UserShortResponseMapper userMapper;

    @Override
    public ThemeResponse from(Theme source) {
        return new ThemeResponse()
                .setId(source.getId())
                .setType(ThemeTypeEnum.getById(source.getType().getId()))
                .setUser(userMapper.from(source.getUser()))
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFavorites(source.getFavorite())
                .setData(source.getData())
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
