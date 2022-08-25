package ru.magnit.magreportbackend.domain.olap;

public enum FilterType {
    EMPTY,
    IN_LIST,
    CONTAINS_CS,
    CONTAINS_CI,
    EQUALS,
    GREATER,
    LESSER,
    GREATER_OR_EQUALS,
    LESSER_OR_EQUALS,
    BETWEEN
}
