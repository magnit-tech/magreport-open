package ru.magnit.magreportbackend.domain.schedule;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "SCHEDULE")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "SCHEDULE_ID"))
public class Schedule extends EntityWithName {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "SECOND_ID")
    private Long second;

    @Column(name = "MINUTE_ID")
    private Long minute;

    @Column(name = "HOUR_ID")
    private Long hour;

    @Column(name = "DAY_ID")
    private Long day;

    @Column(name = "DAY_WEEK")
    private Long dayWeek;

    @Column(name = "MONTH_ID")
    private Long month;

    @Column(name = "YEAR_ID")
    private Long year;

    @Column(name = "DAY_END_MONTH")
    private Long dayEndMonth;

    @Column(name = "WEEK_MONTH")
    private Long weekMonth;

    @Column(name = "WEEK_END_MONTH")
    private Long weekEndMonth;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SCHEDULE_TYPE_ID")
    private ScheduleType type;

    @Column(name = "PLAN_START_DATE")
    LocalDateTime planStartDate;

    @Column(name = "LAST_START_DATE")
    LocalDateTime lastStartDate;

    @Column(name = "DIFFERENCE_TIME")
    Long differenceTime;

    @ManyToMany(mappedBy = "scheduleList")
    private List<ScheduleTask> scheduleTasks = Collections.emptyList();

    public Schedule(Long id) {
        this.id = id;
    }

    @Override
    public Schedule setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Schedule setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Schedule setDescription(String description) {
        this.description = description;
        return this;
    }

}
