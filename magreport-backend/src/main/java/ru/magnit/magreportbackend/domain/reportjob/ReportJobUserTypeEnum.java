package ru.magnit.magreportbackend.domain.reportjob;

public enum ReportJobUserTypeEnum {
    SCHEDULE,
    SHARE;

    public Long getId() {
        return (long) this.ordinal();
    }
    public static ReportJobUserTypeEnum getById(long id) {
        return ReportJobUserTypeEnum.values()[(int) id];
    }
    }
