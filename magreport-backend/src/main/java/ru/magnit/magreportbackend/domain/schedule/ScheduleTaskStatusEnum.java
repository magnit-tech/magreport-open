package ru.magnit.magreportbackend.domain.schedule;

public enum ScheduleTaskStatusEnum {
    SCHEDULED,
    RUNNING,
    COMPLETE,
    FAILED,
    EXPIRED,
    CHANGED,
    INACTIVE;

    public Long getId() {
        return (long) this.ordinal();
    }

    public static ScheduleTaskStatusEnum getById(long id) {
        return ScheduleTaskStatusEnum.values()[(int) id];
    }
}
