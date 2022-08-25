package ru.magnit.magreportbackend.domain.filtertemplate;

public enum FilterFieldTypeEnum {
    ID_FIELD,
    CODE_FIELD,
    NAME_FIELD;

    public static FilterFieldTypeEnum getByOrdinal(long id) {
        return FilterFieldTypeEnum.values()[(int) id];
    }
}
