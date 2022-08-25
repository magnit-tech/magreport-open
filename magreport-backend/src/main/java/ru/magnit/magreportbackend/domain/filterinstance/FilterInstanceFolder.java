package ru.magnit.magreportbackend.domain.filterinstance;

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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "FILTER_INSTANCE_FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_INSTANCE_FOLDER_ID"))
public class FilterInstanceFolder extends EntityWithName implements FolderEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private FilterInstanceFolder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<FilterInstanceFolder> childFolders = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<FilterInstance> filterInstances = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<FilterInstanceFolderRole> folderRoles = new LinkedList<>();

    public FilterInstanceFolder(Long id) {
        this.id = id;
    }

    @Override
    public FilterInstanceFolder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterInstanceFolder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public FilterInstanceFolder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilterInstanceFolder setParentFolder(EntityWithName parentFolder) {
        this.parentFolder = (FilterInstanceFolder) parentFolder;
        return this;
    }

    @Override
    public FilterInstanceFolder setChildFolders(List<? extends EntityWithName> list) {
        this.childFolders = list.stream()
                .map(FilterInstanceFolder.class::cast)
                .collect(Collectors.toList());

        return this;
    }

    @Override
    public List<? extends EntityWithName> getChildElements() {
        return this.filterInstances;
    }

    @Override
    public FilterInstanceFolder setChildElements(List<? extends EntityWithName> list) {
        this.filterInstances = list.stream()
                .map(FilterInstance.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public FilterInstanceFolder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterInstanceFolder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
