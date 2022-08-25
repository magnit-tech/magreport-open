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

@Entity(name = "SCHEDULE_TASK_TYPE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SCHEDULE_TASK_TYPE_ID"))
public class ScheduleTaskType extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    public ScheduleTaskType (Long id){
        this.id = id;
    }

    @OneToMany(cascade = ALL, mappedBy = "taskType")
    private List<ScheduleTask> schedulesTasks = Collections.emptyList();

    @Override
    public ScheduleTaskType setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ScheduleTaskType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ScheduleTaskType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ScheduleTaskType setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public ScheduleTaskType setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }
}
