package ru.magnit.magreportbackend.domain.serversettings;

public enum ServerMailTemplateTypeEnum {
    SCHEDULE;

    public Long getId() {
        return (long) this.ordinal();
    }

    public static ServerMailTemplateTypeEnum getById(long id) {
        return ServerMailTemplateTypeEnum.values()[(int) id];
    }
}
