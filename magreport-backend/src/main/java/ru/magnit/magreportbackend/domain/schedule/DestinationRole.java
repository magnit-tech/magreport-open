package ru.magnit.magreportbackend.domain.schedule;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "DESTINATION_ROLE")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "DESTINATION_ROLE_ID"))

public class DestinationRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "VAL")
    private Long value;

    @Column
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DESTINATION_TYPE_ID")
    private DestinationType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SCHEDULE_TASK_ID")
    private ScheduleTask scheduleTask;

    @Override
    public DestinationRole setId(Long id) {
        this.id = id;
        return this;
    }


    @Override
    public DestinationRole setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @Override
    public DestinationRole setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
        return this;
    }

    public DestinationRole() {
    }


    public DestinationRole(Long value){
        this.value = value;
    }
}
