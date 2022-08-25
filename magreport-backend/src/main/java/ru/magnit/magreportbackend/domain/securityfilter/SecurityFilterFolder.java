package ru.magnit.magreportbackend.domain.securityfilter;

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

@Entity(name = "SECURITY_FILTER_FOLDER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SECURITY_FILTER_FOLDER_ID"))
public class SecurityFilterFolder extends EntityWithName implements FolderEntity {


    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private SecurityFilterFolder parentFolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentFolder")
    private List<SecurityFilterFolder> childFolders = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<SecurityFilterFolderRole> folderRoles = Collections.emptyList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folder")
    private List<SecurityFilter> filters = new LinkedList<>();

    public SecurityFilterFolder(Long id) {
        this.id = id;
    }

    @Override
    public SecurityFilterFolder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public SecurityFilterFolder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public SecurityFilterFolder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public SecurityFilterFolder setParentFolder(EntityWithName parentFolder) {
        this.parentFolder = (SecurityFilterFolder) parentFolder;
        return this;
    }

    @Override
    public SecurityFilterFolder setChildFolders(List<? extends EntityWithName> list) {
        this.childFolders = list.stream()
                .map(SecurityFilterFolder.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public List<? extends EntityWithName> getChildElements() {
        return this.filters;
    }

    @Override
    public SecurityFilterFolder setChildElements(List<? extends EntityWithName> list) {
        this.filters = list.stream()
                .map(SecurityFilter.class::cast)
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public SecurityFilterFolder setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public SecurityFilterFolder setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
