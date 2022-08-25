package ru.magnit.magreportbackend.mapper.theme;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.theme.Theme;
import ru.magnit.magreportbackend.domain.theme.ThemeType;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.theme.ThemeAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ThemeMapper  implements Mapper<Theme, ThemeAddRequest> {
    @Override
    public Theme from(ThemeAddRequest source) {
        return new Theme()
                .setType(new ThemeType(source.getTypeId()))
                .setUser(new User().setId(source.getUserId()))
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setFavorite(source.getFavorite())
                .setData(source.getData().toString());
    }
}
