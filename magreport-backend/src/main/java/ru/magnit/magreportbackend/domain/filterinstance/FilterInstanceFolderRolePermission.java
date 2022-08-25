package ru.magnit.magreportbackend.domain.filterinstance;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity(name = "FILTER_INSTANCE_FOLDER_ROLE_PERMISSION")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "FILTER_INSTANCE_FOLDER_ROLE_PERMISSION_ID"))
public class FilterInstanceFolderRolePermission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public FilterInstanceFolderRolePermission(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILTER_INSTANCE_FOLDER_ROLE_ID")
    private FilterInstanceFolderRole folderRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLDER_AUTHORITY_ID")
    private FolderAuthority authority;

    @Override
    public FilterInstanceFolderRolePermission setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public FilterInstanceFolderRolePermission setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public FilterInstanceFolderRolePermission setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
