package ru.magnit.magreportbackend.domain.user;

public enum UserStatusEnum {
    DISABLED,
    ACTIVE,
    LOGGED_OFF,
    ARCHIVE;

    public static UserStatusEnum getById(long id) {
        return UserStatusEnum.values()[(int) id];
    }

    public Long getId() {
        return (long)this.ordinal();
    }
}
