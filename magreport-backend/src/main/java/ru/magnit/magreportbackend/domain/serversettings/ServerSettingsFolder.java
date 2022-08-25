package ru.magnit.magreportbackend.domain.serversettings;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "SERVER_SETTINGS_FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SERVER_SETTINGS_FOLDER_ID"))
public class ServerSettingsFolder extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "ORDINAL")
    private Integer ordinal;

    @Column(name = "CODE")
    private String code;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_ID")
    private ServerSettingsFolder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<ServerSettings> settings = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<ServerSettingsFolder> childFolders = Collections.emptyList();

    public ServerSettingsFolder(Long id) {
        this.id = id;
    }

    @Override
    public ServerSettingsFolder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ServerSettingsFolder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ServerSettingsFolder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ServerSettingsFolder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ServerSettingsFolder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
