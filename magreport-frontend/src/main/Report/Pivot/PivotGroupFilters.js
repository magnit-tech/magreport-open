import React from 'react';
import PivotFilters from './PivotFilters';

export default function PivotGroupFilters(props){
    let filterList = [];

    function handleChangeFilterValues(value)
    {
        props.onChangeFilterValues(value)
    }

    filterList.push(
        <PivotFilters
            filterValues = {props.filterValues}
            filterType={props.filterType}
            key={1}
            jobId = {props.jobId}
            fieldId = {props.fieldId}
            pivotTableFilters = {props.pivotTableFilters}
           // params={buildParams('', props.field?.fieldId)} /*{params}*/
            onChangeValues={handleChangeFilterValues}
        />
    )
    return (
        <div>
            {filterList}
        </div>
    )

}