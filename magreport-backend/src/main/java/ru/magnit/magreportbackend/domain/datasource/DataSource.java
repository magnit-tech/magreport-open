package ru.magnit.magreportbackend.domain.datasource;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.EntityWithName;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.user.User;

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
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "DATASOURCE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DATASOURCE_ID"))
public class DataSource extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASOURCE_TYPE_ID")
    private DataSourceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATASOURCE_FOLDER_ID")
    private DataSourceFolder folder;

    @Column(name = "JDBC_URL")
    private String url;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "POOL_SIZE")
    private Short poolSize;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataSource")
    private List<DataSet> dataSets = new LinkedList<>();

    public DataSource(Long id) {
        this.id = id;
    }

    @Override
    public DataSource setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public DataSource setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DataSource setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DataSource setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DataSource setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
