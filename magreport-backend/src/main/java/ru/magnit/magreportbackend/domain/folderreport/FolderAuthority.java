package ru.magnit.magreportbackend.domain.folderreport;

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
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "FOLDER_AUTHORITY")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FOLDER_AUTHORITY_ID"))
public class FolderAuthority extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public FolderAuthority(Long id) {
        this.id = id;
    }

    public FolderAuthority(FolderAuthorityEnum folderAuthority) {
        this.id = (long)folderAuthority.ordinal();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authority")
    private List<FolderRolePermission> permissions = Collections.emptyList();

    @Override
    public FolderAuthority setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FolderAuthority setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FolderAuthority setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FolderAuthority setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FolderAuthority setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
