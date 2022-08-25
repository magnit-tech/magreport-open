package ru.magnit.magreportbackend.domain.theme;

public enum ThemeTypeEnum {

    WHITE,
    BLACK;

    public Long getId() {
        return (long) this.ordinal();
    }

    public static ThemeTypeEnum getById(long id) {
        return ThemeTypeEnum.values()[(int) id];
    }
}
