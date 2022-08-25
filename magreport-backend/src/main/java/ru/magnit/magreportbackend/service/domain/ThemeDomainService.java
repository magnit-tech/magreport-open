package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.request.theme.ThemeAddRequest;
import ru.magnit.magreportbackend.dto.response.theme.ThemeResponse;
import ru.magnit.magreportbackend.mapper.theme.ThemeMapper;
import ru.magnit.magreportbackend.mapper.theme.ThemeMerger;
import ru.magnit.magreportbackend.mapper.theme.ThemeResponseMapper;
import ru.magnit.magreportbackend.repository.ThemeRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeDomainService {

    private final ThemeRepository repository;
    private final ThemeMapper mapper;
    private final ThemeResponseMapper responseMapper;
    private final ThemeMerger merger;


    @Transactional
    public ThemeResponse addTheme(ThemeAddRequest request) {
        if (request.getFavorite())
            clearFavoriteTheme(request.getTypeId());

        var theme = repository.save(mapper.from(request));
        return responseMapper.from(theme);
    }

    @Transactional
    public ThemeResponse editTheme(ThemeAddRequest request) {
        var theme = repository.getReferenceById(request.getId());
        var merged = merger.merge(theme, request);
        repository.save(merged);
        return responseMapper.from(merged);
    }

    @Transactional
    public void deleteTheme(Long themeId) {
        repository.deleteById(themeId);
    }

    @Transactional
    public void setFavorite(Long themeId) {
        var theme = repository.getReferenceById(themeId);

        if (!theme.getFavorite()) {
            repository.clearFavoriteTheme(theme.getType().getId());
            repository.save(theme.setFavorite(true));
        }
    }

    @Transactional
    public ThemeResponse getTheme(Long themeId) {
        var theme = repository.getReferenceById(themeId);
        return responseMapper.from(theme);
    }

    @Transactional
    public List<ThemeResponse> getAllThemes(){
       return responseMapper.from(repository.findAll());
    }

    @Transactional
    public List<ThemeResponse> getAllUserThemes(Long userId){
        return responseMapper.from(repository.findAllByUserId(userId));
    }


    private void clearFavoriteTheme(Long themeTypeId) {
        repository.clearFavoriteTheme(themeTypeId);
    }
}
