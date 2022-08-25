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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FOLDER_ID"))
public class Folder extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Folder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<Folder> childFolders = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<FolderReport> folderReports = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<FolderRole> folderRoles = Collections.emptyList();

    public Folder(Long id) {
        this.id = id;
    }

    @Override
    public Folder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Folder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Folder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Folder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public Folder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
