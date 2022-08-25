package ru.magnit.magreportbackend.domain.schedule;

public enum ScheduleTaskTypeEnum {
    EMAIL,
    USER_TASK;

    public Long getId() {
        return (long) this.ordinal();
    }

    public static ScheduleTaskTypeEnum getById(long id) {
        return ScheduleTaskTypeEnum.values()[(int) id];
    }
}
