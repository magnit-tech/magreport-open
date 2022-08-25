package ru.magnit.magreportbackend.domain.schedule;

public enum DestinationTypeEnum {

    REPORT,
    ERROR;

    public boolean equalsIsLong(Long id){
        return this.ordinal() == id.intValue();
    }

    public static DestinationTypeEnum getById(Long id) {
        return values()[id.intValue()];
    }
}
