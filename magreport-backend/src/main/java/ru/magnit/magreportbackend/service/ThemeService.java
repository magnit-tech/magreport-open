package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.request.theme.ThemeAddRequest;
import ru.magnit.magreportbackend.dto.request.theme.ThemeRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.response.theme.ThemeResponse;
import ru.magnit.magreportbackend.service.domain.ThemeDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeDomainService domainService;
    private final UserDomainService userDomainService;


    public ThemeResponse addTheme(ThemeAddRequest request) {
        return domainService.addTheme(request);
    }

    public ThemeResponse editTheme(ThemeAddRequest request) {
        return domainService.editTheme(request);
    }

    public ThemeResponse getTheme(ThemeRequest request) {
        return domainService.getTheme(request.getId());
    }

    public void deleteTheme(ThemeRequest request) {
        domainService.deleteTheme(request.getId());
    }

    public void setFavoriteTheme(ThemeRequest request) {
        domainService.setFavorite(request.getId());
    }

    public List<ThemeResponse> getAllThemes() {
        return domainService.getAllThemes();
    }

    public List<ThemeResponse> getAllUserThemes(UserRequest request) {
        return domainService.getAllUserThemes(userDomainService.getUserResponse(request.getUserName()).getId());
    }

}
