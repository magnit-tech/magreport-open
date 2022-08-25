export function TableData() {
    
    this.fieldIdToNameMapping = new Map();
    this.columnDimensionsFields = [];
    this.rowDimensionsFields = [];
    this.columnDimensionsValues = [];
    this.rowDimensionsValues = [];
    this.metrics = [];
    this.columnFrom = 0;
    this.columnCount = 0;
    this.rowFrom = 0;
    this.rowCount = 0;
    this.totalColumns = 0;
    this.totalRows = 0;

    this.setDataFromResponse = (fieldsLists, fieldIdToNameMapping, response, columnFrom, columnCount, rowFrom, rowCount) => {
        this.fieldIdToNameMapping = fieldIdToNameMapping;
        this.columnDimensionsFields = fieldsLists.columnFields.slice();
        this.rowDimensionsFields = fieldsLists.rowFields.slice();
        this.columnDimensionsValues = response.data.columnValues;
        this.rowDimensionsValues = response.data.rowValues;
        this.metrics = response.data.metricValues;
        this.columnFrom = columnFrom;
        this.columnCount = columnCount;
        this.rowFrom = rowFrom;
        this.rowCount = rowCount;
        this.totalColumns = response.data.totalColumns;
        this.totalRows = response.data.totalRows;

        for(let v of this.metrics){
            v.metricName = fieldIdToNameMapping[v.fieldId];
        }
    }

    this.subTable = (columnFrom, columnCount, rowFrom, rowCount) => {

        if(columnFrom >= this.columnFrom && columnFrom + columnCount <= this.columnFrom + this.columnCount &&
            rowFrom >= this.rowFrom && rowFrom + rowCount <= this.rowFrom + this.rowCount){
                
            let subTableData = new TableData();

            let innerColumnFrom = columnFrom - this.columnFrom;
            let innerColumnTo = innerColumnFrom + columnCount;
            let innerRowFrom = rowFrom - this.rowFrom;
            let innerRowTo = innerRowFrom + rowCount;

            subTableData.fieldIdToNameMapping = this.fieldIdToNameMapping;
            subTableData.columnDimensionsFields = this.columnDimensionsFields;
            subTableData.rowDimensionsFields = this.rowDimensionsFields;
            subTableData.columnDimensionsValues = this.columnDimensionsValues.slice(innerColumnFrom, innerColumnTo);
            subTableData.rowDimensionsValues = this.rowDimensionsValues.slice(innerRowFrom, innerRowTo);

            let mColumnCount = columnCount > 0 ? columnCount : 1;
            let mRowCount = rowCount > 0 ? rowCount : 1;

            subTableData.metrics = this.metrics.map( (m) => {
                let subValues = [];

                //console.log(mColumnCount, mRowCount);

                for(let i = 0; i < mColumnCount; i++){
                    subValues.push(new Array(mRowCount));
                    for(let j = 0; j < mRowCount; j++){
                        //console.log(i, j);
                        subValues[i][j] = m.values[innerColumnFrom + i][innerRowFrom + j];
                    }
                }

                return {
                    ...m,
                    values: subValues
                }
            });

            subTableData.columnFrom = columnFrom;
            subTableData.columnCount = columnCount;
            subTableData.rowFrom = rowFrom;
            subTableData.rowCount = rowCount;
            subTableData.totalColumns = this.totalColumns;
            subTableData.totalRows = this.totalRows;

            return subTableData;
        }
        else{
            return null;
        }

    }
}