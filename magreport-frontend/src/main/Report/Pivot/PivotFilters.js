import React, { useState } from 'react';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
// local
import PivotFiltersItem from './PivotFiltersItem';
import {PivotCSS} from './PivotCSS';

function PivotFilters(props){

    const classes = PivotCSS();

    const filterType = [
        {id: "EQUALS", name: "Равно"},
    //   {id: "NOT_EQUALS", name: "Не равно"},
        {id: "IN_LIST", name: "В списке"},
    //   {id: "NOT_IN_LIST", name: "Не в списке"},
        {id: "CONTAINS_CI", name: "Содержит"},
    //    {id: "NOT_CONTAINS", name: "Не содержит"},
    //    {id: "BETWEEN", name: "Между"}, 
    //    {id: "NOT_BETWEEN", name: "Не между"}
    ];

    const [filterTypeChoosen, setFilterTypeChoosen] = useState(props.filterType ? props.filterType : filterType[0].id);

    function handleChangeFilterValues(value){
        props.onChangeValues({values: value, filterType: filterTypeChoosen, invertResult: false});
    }

    return (
        <div>
            <FormControl className={classes.formControl} size="small">
                <Select
                    defaultValue = {filterTypeChoosen}
                    children={ filterType.map((value, index) => <MenuItem key={index} value={value.id}>{value.name}</MenuItem>) }
                    onChange={event => setFilterTypeChoosen(event.target.value)}
                    inputProps={{
                        name: 'Тип фильтра',
                        id: 'filter-type',
                    }}
                />
            </FormControl>
            <PivotFiltersItem
                filterValues = {props.filterValues}
                jobId = {props.jobId}
                fieldId = {props.fieldId}
                pivotTableFilters = {props.pivotTableFilters}
                filterType = {filterTypeChoosen}
                onChangeValues={handleChangeFilterValues}
            />
        </div>
    )

}

export default PivotFilters