package ru.magnit.magreportbackend.domain.filtertemplate;

public enum FilterOperationTypeEnum {
    IS_IN_LIST,
    IS_NOT_IN_LIST,
    IS_EQUAL,
    IS_NOT_EQUAL,
    IS_GREATER,
    IS_LOWER,
    IS_GREATER_OR_EQUAL,
    IS_LOWER_OR_EQUAL,
    IS_BETWEEN,
    IS_NOT_BETWEEN;

    public static FilterOperationTypeEnum getById(long id) {

        return FilterOperationTypeEnum.values()[(int) id];
    }
}
