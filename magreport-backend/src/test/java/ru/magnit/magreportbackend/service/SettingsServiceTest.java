package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingSetRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingsJournalRequest;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerSettingsResponse;
import ru.magnit.magreportbackend.service.domain.SettingsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {
    private static final Long ID = 1L;
    private static final String PARAM = "Test value";

    @InjectMocks
    private SettingsService service;
    @Mock
    private SettingsDomainService domainService;
    @Mock
    private UserDomainService userDomainService;


    @Test
    void getSettings() {
        when(domainService.getSettings()).thenReturn(new ServerSettingsResponse());

        assertNotNull(service.getSettings());

        verify(domainService).getSettings();
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void setSetting() {
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setId(ID));

        service.setSetting(getServerSettingSetRequest());

        verify(userDomainService).getCurrentUser();
        verify(domainService).setSetting(any(), any());

        verifyNoMoreInteractions(userDomainService);
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getJournal() {

        when(domainService.getJournal(any())).thenReturn(Collections.emptyList());

        assertNotNull(service.getJournal(getServerSettingsJournalRequest()));

        verify(domainService).getJournal(any());
        verifyNoMoreInteractions(domainService);
    }

    @Test
    void getValueSetting() {

        when(domainService.getValueSetting(any())).thenReturn(PARAM);

        var response = service.getValueSetting(PARAM);

        assertNotNull(response);

        verify(domainService).getValueSetting(any());
        verifyNoMoreInteractions(domainService);

    }

    private ServerSettingSetRequest getServerSettingSetRequest() {
        return new ServerSettingSetRequest()
                .setSettingId(ID)
                .setValue(PARAM);
    }

    private ServerSettingsJournalRequest getServerSettingsJournalRequest() {
        return new ServerSettingsJournalRequest()
                .setSettingId(ID);
    }
}