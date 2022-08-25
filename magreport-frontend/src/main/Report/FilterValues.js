export default function FilterValues() {

    // Хранит объекты выбора для каждого фильтра по ключу - ID фильтра
    // объект выбора: {operationType, parameters}

    this.values = new Map();

    this.setFilterValue = (filterValue) => {
        this.values.set(filterValue.filterId, filterValue);
    }

    this.buildOnParametersObject = (lastParameters, buildForLastParamters) => {
        this.values = new Map();
        for (let p of lastParameters) {
            if (buildForLastParamters || p.filterType !== 'TOKEN_INPUT') {
                // Для фильтра TOKEN_INPUT lastParameters отличается по структуре от parameters и прошлое значение получается
                // обратным вызовом onChangeFilterValue из самого фильтра, в котором правильным образом обрабатывается значение
                // lastParameters, поэтому его lastParamters учитываются только для заполнения прошлого выбора, а не для текущего
                this.setFilterValue(p);
            }
        }
    }

    this.getFilterValue = (filterId) => {
        return this.values.get(filterId);
    }

    this.checkMandatoryFilters = mandaroryFilters => {
        for (let f of mandaroryFilters) {
            const filterVal = this.values.get(f)
            if (!filterVal || filterVal.parameters.length === 0) {
                return false
            }
        }
        return true
    }

    this.checkInvalidValues = () => {
        for (let f of this.values.values()) {
            if (f.validation && f.validation !== 'success') {
                return false
            }
        }
        return true
    }

    this.getParameters = () => {
        let parameters = [];

        this.values.forEach((v) => {
            if (v.parameters.length > 0) {
                parameters.push(v)
            }
        });

        return parameters;
    }
}
