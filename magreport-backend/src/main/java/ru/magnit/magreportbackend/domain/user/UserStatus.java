package ru.magnit.magreportbackend.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "USER_STATUS")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "USER_STATUS_ID"))
public class UserStatus extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = ALL, mappedBy = "userStatus")
    private List<User> users = Collections.emptyList();

    public UserStatus(Long userStatusId) {
        this.id = userStatusId;
    }

    public UserStatus(UserStatusEnum userStatus) {
        this.id = (long) userStatus.ordinal();
    }

    @Override
    public UserStatus setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public UserStatus setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public UserStatus setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public UserStatus setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public UserStatus setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}