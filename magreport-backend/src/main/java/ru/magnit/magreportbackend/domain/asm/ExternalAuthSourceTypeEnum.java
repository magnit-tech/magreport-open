package ru.magnit.magreportbackend.domain.asm;

public enum ExternalAuthSourceTypeEnum {
    GROUP_SOURCE(1L),
    USER_MAP_SOURCE(2L),
    PERMISSION_SOURCE(3L);

    ExternalAuthSourceTypeEnum(Long id) {
        this.id = id;
    }

    private final Long id;

    public Long getId() {
        return this.id;
    }

    public static ExternalAuthSourceTypeEnum getTypeById(Long id) {
        for(ExternalAuthSourceTypeEnum typeEnum: values()) {
            if(typeEnum.getId().equals(id)) {
                return typeEnum;
            }
        }
        return null;
    }
}
