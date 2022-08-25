package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.theme.ThemeTypeEnum;
import ru.magnit.magreportbackend.dto.request.theme.ThemeAddRequest;
import ru.magnit.magreportbackend.dto.request.theme.ThemeRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.response.theme.ThemeResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;
import ru.magnit.magreportbackend.service.domain.ThemeDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    private final static Long ID = 0L;
    private final static String NAME = "NAME";
    private final static String DESCRIPTION = "DESCRIPTION";
    private final static Long TYPE_ID = 1L;
    private final static Long USER_ID = 0L;
    private final static Boolean FAVORITE = true;
    private final static Object DATA = "JSON";


    @Mock
    private ThemeDomainService domainService;
    @Mock
    private UserDomainService userDomainService;

    @InjectMocks
    private ThemeService service;

    @Test
    void addTheme() {

        when(domainService.addTheme(any())).thenReturn(getThemeResponse());

        var response = service.addTheme(getThemeAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(TYPE_ID, response.getType().getId());
        assertEquals(USER_ID, response.getUser().id());
        assertEquals(FAVORITE, response.getFavorites());
        assertEquals(DATA, response.getData());

        verify(domainService).addTheme(any());
        verifyNoMoreInteractions(domainService);
        verifyNoInteractions(userDomainService);
    }

    @Test
    void editTheme() {

        when(domainService.editTheme(any())).thenReturn(getThemeResponse());

        var response = service.editTheme(getThemeAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(TYPE_ID, response.getType().getId());
        assertEquals(USER_ID, response.getUser().id());
        assertEquals(FAVORITE, response.getFavorites());
        assertEquals(DATA, response.getData());

        verify(domainService).editTheme(any());
        verifyNoMoreInteractions(domainService);
        verifyNoInteractions(userDomainService);

    }

    @Test
    void getTheme() {

        when(domainService.getTheme(any())).thenReturn(getThemeResponse());

        var response = service.getTheme(getThemeRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(TYPE_ID, response.getType().getId());
        assertEquals(USER_ID, response.getUser().id());
        assertEquals(FAVORITE, response.getFavorites());
        assertEquals(DATA, response.getData());

        verify(domainService).getTheme(any());
        verifyNoMoreInteractions(domainService);
        verifyNoInteractions(userDomainService);

    }

    @Test
    void deleteTheme() {

        service.deleteTheme(getThemeRequest());

        verify(domainService).deleteTheme(any());
        verifyNoMoreInteractions(domainService);
        verifyNoInteractions(userDomainService);
    }

    @Test
    void setFavoriteTheme() {

        service.setFavoriteTheme(getThemeRequest());

        verify(domainService).setFavorite(any());
        verifyNoMoreInteractions(domainService);
        verifyNoInteractions(userDomainService);

    }

    @Test
    void getAllThemes() {

        when(domainService.getAllThemes()).thenReturn(Collections.singletonList(getThemeResponse()));

        var response = service.getAllThemes();

        assertEquals(1, response.size());

        verify(domainService).getAllThemes();
        verifyNoMoreInteractions(domainService);
        verifyNoInteractions(userDomainService);

    }

    @Test
    void getAllUserThemes() {

        when(domainService.getAllUserThemes(any())).thenReturn(Collections.singletonList(getThemeResponse()));
        when(userDomainService.getUserResponse(any())).thenReturn(new UserResponse().setId(ID));

        var response = service.getAllUserThemes(new UserRequest().setUserName(NAME));

        assertEquals(1, response.size());

        verify(domainService).getAllUserThemes(any());
        verify(userDomainService).getUserResponse(any());
        verifyNoMoreInteractions(domainService,userDomainService);
    }


    private ThemeAddRequest getThemeAddRequest() {
        return new ThemeAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setTypeId(TYPE_ID)
                .setUserId(USER_ID)
                .setFavorite(FAVORITE)
                .setData(DATA);
    }

    private ThemeRequest getThemeRequest() {
        return new ThemeRequest()
                .setId(ID);
    }

    private ThemeResponse getThemeResponse() {
        return new ThemeResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setType(ThemeTypeEnum.getById(TYPE_ID))
                .setUser(new UserShortResponse(USER_ID, NAME))
                .setFavorites(FAVORITE)
                .setData("JSON");
    }
}
