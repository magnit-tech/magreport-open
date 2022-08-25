import React from 'react';

// local
import ValueList from './filters/ValueList.js';
import HierTree from './filters/HierTree.js';
import TokenInput from './filters/TokenInput.js';
import DateRange from './filters/DateRange.js'
import Range from './filters/Range';
import DateValue from './filters/DateValue'
import SingleValueUnbounded from './filters/SingleValueUnbounded.js';

/**
 * Компонент настройки значений для фильтра отчета
 * @param {Object} props - свойства компонента
 * @param {*} props.filterData - метаданные
 * @param {*} props.lastFilterValue - объект FilterValues со значениями фильтров из последнего запуска
 * @param {*} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра, 
 *                                         filterValue - объект для передачи в сервис в массиве parameters                             
 * @param {*} props.toggleClearFilter - при изменении значения данного свойства требуется очистить выбор в фильтре
 */
function FilterWrapper(props){
    let filterType = props.filterData.type.name || props.filterData.type;

    if(props.filterData.hidden){
        return (<div className="hiddenFilter"></div>);
    }
    else if(filterType === 'SINGLE_VALUE_UNBOUNDED'){
        return(
            <SingleValueUnbounded
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue} 
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
            />
        );
    }
    else if(filterType === 'VALUE_LIST' || filterType === 'VALUE_LIST_UNBOUNDED'){
        return(
            <ValueList
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue} 
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
                unbounded = {filterType === 'VALUE_LIST_UNBOUNDED'}
            />
        );
    }
    else if (filterType === 'HIERARCHY'){
        return(
            <HierTree 
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue} 
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
                strict={true}
            />
        );
    }
    else if (filterType === 'HIERARCHY_M2M'){
        return(
            <HierTree 
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue} 
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
                strict={false}
            />
        );
    }    
    else if (filterType === 'TOKEN_INPUT'){
        return(
            <TokenInput 
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue} 
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
            />
        );
    }
    else if (filterType === 'DATE_RANGE'){
        return(
            <DateRange 
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue} 
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
            />
        );
    }
    else if (filterType === 'DATE_VALUE'){
        return(
            <DateValue
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue} 
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
            />
        );
    }
    else if (filterType === 'RANGE'){
        return(
            <Range
                filterData={props.filterData}
                lastFilterValue={props.lastFilterValue}
                toggleClearFilter={props.toggleClearFilters}
                onChangeFilterValue={props.onChangeFilterValue}
            />
        );
    }
    else{
        return(
            <p>
                Неизвестный тип фильтра
            </p>
        );
    }

}

export default FilterWrapper;