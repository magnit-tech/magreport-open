package ru.magnit.magreportbackend.domain.serversettings;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "SERVER_SETTINGS_JOURNAL")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SERVER_SETTINGS_JOURNAL_ID"))
public class ServerSettingsJournal extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Lob
    @Column(name = "VALUE_BEFORE")
    private String valueBefore;

    @Lob
    @Column(name = "VALUE_AFTER")
    private String valueAfter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SERVER_SETTINGS_ID")
    private ServerSettings setting;

    public ServerSettingsJournal(Long id) {
        this.id = id;
    }

    @Override
    public ServerSettingsJournal setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ServerSettingsJournal setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ServerSettingsJournal setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

}
