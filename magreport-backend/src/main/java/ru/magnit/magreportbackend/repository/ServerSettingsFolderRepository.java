package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.serversettings.ServerSettingsFolder;

public interface ServerSettingsFolderRepository extends JpaRepository<ServerSettingsFolder, Long> {
}
