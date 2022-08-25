package ru.magnit.magreportbackend.domain.serversettings;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "SERVER_SETTINGS")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SERVER_SETTINGS_ID"))
public class ServerSettings extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "ORDINAL")
    private Integer ordinal;

    @Column(name = "ENCODED")
    private boolean encoded;

    @Column(name = "CODE")
    private String code;

    @Lob
    @Column(name = "VAL")
    private String value;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "FOLDER_ID")
    private ServerSettingsFolder folder;

    public ServerSettings(Long id) {
        this.id = id;
    }

    @Override
    public ServerSettings setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ServerSettings setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ServerSettings setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ServerSettings setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ServerSettings setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
