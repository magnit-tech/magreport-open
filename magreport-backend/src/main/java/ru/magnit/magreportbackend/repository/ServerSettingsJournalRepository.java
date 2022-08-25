package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.serversettings.ServerSettingsJournal;

import java.util.List;

public interface ServerSettingsJournalRepository extends JpaRepository<ServerSettingsJournal, Long> {

    List<ServerSettingsJournal> findAllBySettingId(Long settingId);
}
