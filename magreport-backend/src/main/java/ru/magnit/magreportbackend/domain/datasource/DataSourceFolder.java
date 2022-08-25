package ru.magnit.magreportbackend.domain.datasource;

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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "DATASOURCE_FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATASOURCE_FOLDER_ID"))
public class DataSourceFolder extends EntityWithName implements FolderEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private DataSourceFolder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<DataSourceFolder> childFolders = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<DataSource> dataSources = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<DataSourceFolderRole> folderRoles = Collections.emptyList();

    public DataSourceFolder(Long id) {
        this.id = id;
    }

    @Override
    public DataSourceFolder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public DataSourceFolder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName (String name) {
        return this.name;
    }


    @Override
    public DataSourceFolder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataSourceFolder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataSourceFolder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

    @Override
    public DataSourceFolder setParentFolder(EntityWithName parentFolder) {
        this.parentFolder = (DataSourceFolder) parentFolder;
        return this;
    }

    @Override
    public DataSourceFolder setChildFolders(List<? extends EntityWithName> list) {
        this.childFolders = list
                .stream()
                .map(DataSourceFolder.class::cast)
                .collect(Collectors.toList());

        return this;
    }

    @Override
    public List<DataSource> getChildElements() {
        return this.dataSources;
    }

    @Override
    public DataSourceFolder setChildElements(List<? extends EntityWithName> list) {
        this.dataSources = list
                .stream()
                .map(DataSource.class::cast)
                .collect(Collectors.toList());
        return this ;
    }

}
