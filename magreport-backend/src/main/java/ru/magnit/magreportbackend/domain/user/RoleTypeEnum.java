package ru.magnit.magreportbackend.domain.user;

public enum RoleTypeEnum {
    SYSTEM,
    SECURITY_FILTER,
    FOLDER_ROLES;

    public static RoleTypeEnum getByOrdinal(long id) {
        return RoleTypeEnum.values()[(int) id];
    }
}
