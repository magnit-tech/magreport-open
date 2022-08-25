package ru.magnit.magreportbackend.domain.asm;

public enum ExternalAuthSourceFieldTypeEnum {
    CHANGE_TYPE_FIELD(1L),
    ROLE_NAME_FIELD(2L),
    USER_NAME_FIELD(3L),
    FILTER_VALUE_FIELD(4L);

    ExternalAuthSourceFieldTypeEnum(Long id) {
        this.id = id;
    }

    private final Long id;

    public Long getId() {
        return this.id;
    }

    public static ExternalAuthSourceFieldTypeEnum getById(Long id) {
        for(ExternalAuthSourceFieldTypeEnum typeEnum : values()) {
            if(typeEnum.getId().equals(id)) {
                return typeEnum;
            }
        }
        return null;
    }
}
