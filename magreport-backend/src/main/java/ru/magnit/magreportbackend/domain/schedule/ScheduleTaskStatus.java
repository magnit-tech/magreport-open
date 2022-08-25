package ru.magnit.magreportbackend.domain.schedule;

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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity(name = "SCHEDULE_TASK_STATUS")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SCHEDULE_TASK_STATUS_ID"))
public class ScheduleTaskStatus extends EntityWithName {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<ScheduleTask> scheduleTasks = Collections.emptyList();

    public ScheduleTaskStatus(Long id) {
        this.id = id;
    }

    @Override
    public ScheduleTaskStatus setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ScheduleTaskStatus setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ScheduleTaskStatus setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ScheduleTaskStatus setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ScheduleTaskStatus setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

}
