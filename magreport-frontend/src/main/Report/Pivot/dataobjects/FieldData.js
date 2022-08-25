import restoreObjectData from "utils/restoreObjectData";

export default function FieldData(fieldData){
    /**
     * Поля
     */

    // Из метаданных отчёта:
    this.id = 0;
    this.name = "";
    this.typeId = 0;
    this.visible = true;
    this.dataType = ''

    // Добавленные на уровне сводной свойства:
    this.aggFuncName = "";
    this.filter = null;
    this.formatting = {
        personalSettings: false,
        bold: false,
        rounding: 0
    };

    // Обновляем из входящего объекта
    restoreObjectData(this, fieldData);

    /*
        Синонимы для полей:
    */
    this.fieldId = this.id;
    this.fieldName = this.name;

    /*
        Методы задания фильтрации и сортировки
    */
    this.setFilter = (filter) => {
        this.filter = filter;
    }

    // Изменение тип данных метрики
    this.changeDataType = (dataType) => {
        this.dataType = dataType
    }

    // Изменение данных метрики 
    this.changeData = (data) => {
        if (this.formatting.personalSettings) {
            return Number(data).toFixed(this.formatting.rounding)
        } else {
            if (this.dataType === 'DOUBLE') {
                this.formatting.rounding = 2;
                return Number(data).toFixed(2)
            } else {
                this.formatting.rounding = 0;
                return data
            }
        }
    }

    // Изменение начертания шрифта
    this.changeFormatBold = (formatObj) => {
        this.formatting = formatObj
        this.formatting.bold = !this.formatting.bold;
    }

    // Изменение всего объекта форматирования
    this.changeDataStyle = (formatObj) => {
        this.formatting = formatObj
    }
    
}