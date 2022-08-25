package ru.magnit.magreportbackend.domain.schedule;


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
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "SCHEDULE_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SCHEDULE_TYPE_ID"))
public class ScheduleType extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public ScheduleType (Long id){
        this.id = id;
    }

    @OneToMany(cascade = ALL, mappedBy = "type")
    private List<Schedule> schedules = Collections.emptyList();

    @Override
    public ScheduleType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ScheduleType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ScheduleType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ScheduleType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ScheduleType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
