package ru.magnit.magreportbackend.domain.theme;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.user.User;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "THEME")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "THEME_ID"))
public class Theme extends EntityWithName {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "THEME_TYPE_ID")
    private ThemeType type;

    @Column(name = "FAVORITE")
    Boolean favorite;

    @Lob
    @Column(name = "DATA")
    private String data;

    @Override
    public Theme setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Theme setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Theme setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

}
