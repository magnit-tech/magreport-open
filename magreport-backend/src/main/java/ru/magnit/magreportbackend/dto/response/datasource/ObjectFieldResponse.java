package ru.magnit.magreportbackend.dto.response.datasource;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class ObjectFieldResponse {

    private String catalogName;
    private String schemaName;
    private String tableName;
    private String fieldName;
    private int dataType;
    private String dataTypeName;
    private int fieldSize;
    private int bufferSize;
    private int decimalDigits;
    private int numPrecision;
    private boolean nullable;
    private String remarks;
    private int sqlDataType;
    private int charOctetLength;
    private int ordinalPosition;
}
