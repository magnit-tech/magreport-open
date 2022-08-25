package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.serversettings.ServerSettings;
import ru.magnit.magreportbackend.domain.serversettings.ServerSettingsFolder;
import ru.magnit.magreportbackend.domain.serversettings.ServerSettingsJournal;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingSetRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingsJournalRequest;
import ru.magnit.magreportbackend.repository.ServerSettingsFolderRepository;
import ru.magnit.magreportbackend.repository.ServerSettingsJournalRepository;
import ru.magnit.magreportbackend.repository.ServerSettingsRepository;
import ru.magnit.magreportbackend.service.security.CryptoService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingsDomainServiceTest {


    @Mock
    private ServerSettingsRepository settingsRepository;

    @Mock
    private ServerSettingsFolderRepository folderRepository;

    @Mock
    private ServerSettingsJournalRepository journalRepository;

    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    SettingsDomainService domainService;

    @Test
    void getSettings() {
        when(folderRepository.findAll()).thenReturn(Collections.singletonList(getServerSettingsFolder()));

        Assertions.assertNotNull(domainService.getSettings());

        verify(folderRepository).findAll();
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void setSetting() {

        when(settingsRepository.getReferenceById(anyLong())).thenReturn(new ServerSettings().setValue("").setEncoded(false));

        domainService.setSetting(new UserView().setId(1L), new ServerSettingSetRequest().setSettingId(1L).setValue(""));

        verify(settingsRepository).save(any());
        verify(settingsRepository).getReferenceById(anyLong());
        verify(journalRepository).save(any());
        verifyNoMoreInteractions(settingsRepository, journalRepository, cryptoService);

    }

    @Test
    void getJournal() {

        when(journalRepository.findAll()).thenReturn(Collections.singletonList(getServerSettingsJournal()));

        Assertions.assertNotNull(domainService.getJournal(new ServerSettingsJournalRequest()));

        verify(journalRepository).findAll();
        verifyNoMoreInteractions(journalRepository);

        Mockito.reset(journalRepository);

        when(journalRepository.findAllBySettingId(anyLong())).thenReturn(Collections.singletonList(getServerSettingsJournal()));

        Assertions.assertNotNull(domainService.getJournal(new ServerSettingsJournalRequest().setSettingId(1L)));

        verify(journalRepository).findAllBySettingId(anyLong());
        verifyNoMoreInteractions(journalRepository);
    }

    @Test
    void getValueSetting() {

        when(settingsRepository.getServerSettingsByCode(anyString())).thenReturn(new ServerSettings().setValue(""));

        Assertions.assertNotNull(domainService.getValueSetting(""));

        verify(settingsRepository).getServerSettingsByCode(anyString());
        verifyNoMoreInteractions(settingsRepository);
    }

    private ServerSettingsFolder getServerSettingsFolder() {
        return new ServerSettingsFolder()
                .setOrdinal(1)
                .setCode("CODE")
                .setName("NAME")
                .setDescription("DESCRIPTION")
                .setSettings(Collections.singletonList(
                        new ServerSettings()
                                .setId(1L)
                                .setCode("CODE")
                                .setEncoded(false)
                                .setName("NAME")
                                .setDescription("DESCRIPTION")
                                .setValue("123")
                ));
    }

    private ServerSettingsJournal getServerSettingsJournal() {
        return new ServerSettingsJournal()
                .setId(1L)
                .setSetting(new ServerSettings()
                        .setId(1L)
                        .setCode("CODE")
                        .setName("NAME")
                        .setDescription("DESCRIPTION"))
                .setUser(new User())
                .setValueAfter("new")
                .setValueBefore("old")
                .setCreatedDateTime(LocalDateTime.now())
                .setModifiedDateTime(LocalDateTime.now());
    }


}
