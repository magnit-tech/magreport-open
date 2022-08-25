package ru.magnit.magreportbackend.domain.dataset;

public enum DataSetTypeEnum {
    TABLE,
    PROCEDURE;

    public boolean equalsIsLong(Long id){
        return this.ordinal() == id.intValue();
    }
}
