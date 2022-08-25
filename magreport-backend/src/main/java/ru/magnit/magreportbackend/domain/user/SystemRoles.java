package ru.magnit.magreportbackend.domain.user;

public enum SystemRoles {
    ADMIN, DEVELOPER, USER;

    public Long getId() {
        return (long)ordinal();
    }
}
