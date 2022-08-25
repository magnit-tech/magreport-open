package ru.magnit.magreportbackend.domain.theme;

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
import java.util.List;

@Entity(name = "THEME_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "THEME_TYPE_ID"))
public class ThemeType extends EntityWithName {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<Theme> themeList;


    public ThemeType(Long id) {
        this.id = id;
    }
}
