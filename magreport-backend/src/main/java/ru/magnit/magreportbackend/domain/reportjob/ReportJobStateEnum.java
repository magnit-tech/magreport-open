package ru.magnit.magreportbackend.domain.reportjob;

public enum ReportJobStateEnum {
    NORMAL,
    DESYNC;

    public Long getId() {
        return (long)this.ordinal();
    }

    public static ReportJobStateEnum getById(long id) {
        return ReportJobStateEnum.values()[(int) id];
    }

}
