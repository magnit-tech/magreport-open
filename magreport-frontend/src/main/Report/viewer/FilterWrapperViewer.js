import React from 'react';

// local
import ValueListViewer from './filters/ValueListViewer';
import HierTree from '../filters/HierTree';
import RangeViewer from './filters/RangeViewer';
import DateValueViewer from './filters/DateValueViewer';
import SingleValueUnbounded from '../filters/SingleValueUnbounded';

/**
 * Компонент просмотра значений у фильтра отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - метаданные
 * @param {Object} props.lastFilterValue - объект со значениями фильтров из последнего запуска
 */
export default function FilterWrapperViewer(props) {

    const filterType = props.filterData.type.name || props.filterData.type;

    if (props.filterData.hidden) {
        return (<div className="hiddenFilter"/>);
    } else if(filterType === 'SINGLE_VALUE_UNBOUNDED'){
        return(
            <SingleValueUnbounded
                disabled = {true}
                filterData={props.filterData} 
                lastFilterValue={props.lastFilterValue}
                toggleClearFilter={false}
                onChangeFilterValue={()=>{/*zalepa*/}}
            />
        );
    } else if (filterType === "VALUE_LIST" || filterType === "TOKEN_INPUT") {
        return (
            <ValueListViewer
                filterData={props.filterData}
                lastFilterValue={props.lastFilterValue}
            />
        );
    } else if (filterType === "HIERARCHY") {
        return (
            <HierTree
                filterData={props.filterData}
                lastFilterValue={props.lastFilterValue}
                onChangeFilterValue={()=>{/*zalepa*/}}
                readOnly={true}
                strict={true}
            />
        );
    } else if (filterType === "HIERARCHY_M2M") {
        return (
            <HierTree
                filterData={props.filterData}
                lastFilterValue={props.lastFilterValue}
                onChangeFilterValue={()=>{/*zalepa*/}}
                readOnly={true}
                strict={false}
            />
        );
    } else if (filterType === "DATE_RANGE") {
        return (
            <RangeViewer
                filterData={props.filterData}
                lastFilterValue={props.lastFilterValue}
                parseDate
            />
        );
    } else if (filterType === "DATE_VALUE") {
        return (
            <DateValueViewer
                filterData={props.filterData}
                lastFilterValue={props.lastFilterValue}
            />
        );
    } else if (filterType === "RANGE") {
        return (
            <RangeViewer
                filterData={props.filterData}
                lastFilterValue={props.lastFilterValue}
            />
        );
    } else {
        return (
            <p>
                Неизвестный тип фильтра
            </p>
        );
    }
}
