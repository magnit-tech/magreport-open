package ru.magnit.magreportbackend.domain.dataset;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.FolderEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "DATASET_FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATASET_FOLDER_ID"))
public class DataSetFolder extends EntityWithName implements FolderEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private DataSetFolder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<DataSetFolder> childFolders = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<DataSet> dataSets = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<DataSetFolderRole> folderRoles = Collections.emptyList();

    public DataSetFolder(Long id) {
        this.id = id;
    }

    @Override
    public DataSetFolder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DataSetFolder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DataSetFolder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataSetFolder setParentFolder(EntityWithName parentFolder) {
        this.parentFolder = (DataSetFolder) parentFolder;
        return this;
    }

    @Override
    public DataSetFolder setChildFolders(List<? extends EntityWithName> list) {
        this.childFolders = list.stream()
                .map(DataSetFolder.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public List<? extends EntityWithName> getChildElements() {
        return this.dataSets;
    }

    @Override
    public DataSetFolder setChildElements(List<? extends EntityWithName> list) {
        this.dataSets = list.stream()
                .map(DataSet.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public DataSetFolder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataSetFolder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
