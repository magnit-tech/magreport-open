package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingSetRequest;
import ru.magnit.magreportbackend.dto.request.serversettings.ServerSettingsJournalRequest;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerSettingsJournalResponse;
import ru.magnit.magreportbackend.dto.response.serversettings.ServerSettingsResponse;
import ru.magnit.magreportbackend.service.domain.SettingsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingsDomainService domainService;
    private final UserDomainService userDomainService;

    public ServerSettingsResponse getSettings() {

        return domainService.getSettings();
    }

    public void setSetting(ServerSettingSetRequest request) {

        final var currentUser = userDomainService.getCurrentUser();

        domainService.setSetting(currentUser, request);
    }

    public List<ServerSettingsJournalResponse> getJournal(ServerSettingsJournalRequest request) {

        return domainService.getJournal(request);
    }

    public String getValueSetting (String code){
        return domainService.getValueSetting(code);
    }
}
