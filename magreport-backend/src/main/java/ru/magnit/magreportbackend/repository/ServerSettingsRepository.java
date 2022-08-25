package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.serversettings.ServerSettings;

import java.util.List;

public interface ServerSettingsRepository extends JpaRepository<ServerSettings, Long> {

    ServerSettings getServerSettingsByCode(String code);
    List<ServerSettings> getAllServerSettingsByCode(String code);
    void deleteServerSettingsByCode(String code);
}
