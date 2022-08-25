import React from 'react';
import Autocomplete from '@material-ui/lab/Autocomplete';
import TextField from '@material-ui/core/TextField';

// styles
import {DesignerMultipleSelectFieldCSS} from './DesignerCSS';

/**
 * 
 * @param {*} props.label - название поля
 * @param {*} props.onChange - function(id) - действие при изменении значения
 * @param {*} props.data - массив объектов {id, name}
 * @param {*} props.disabled - неактивно
 * @param {*} props.needName - если нужно передавать name
 */
export default function DesignerMultipleSelectField(props){
    
    const classes = DesignerMultipleSelectFieldCSS();

    const handleChange = (event, values) => {
        let operations = [];
        if (props.needName){
            values.forEach( item => {
                operations.push(item.name);
            });
        } else if (props.needIdName) {
            values.forEach( item => {
                operations.push({id: item.id, name: item.name});
            });
        }
        else {
            values.forEach( item => {
                operations.push(item.id);
            });
        }
        props.onChange(operations);
    }

    return (
        <div className={classes.root}>
            <Autocomplete
                style={{minWidth: props.minWidth}}
                multiple
                disabled={props.disabled}
                id="tags-outlined"
                options={props.data}
                getOptionLabel={option => option.name}
                onChange={handleChange}
                value={props.value}
                filterSelectedOptions
                renderInput={params => 
                    <TextField
                        {...params}
                        variant="outlined"
                        label={props.label}
                        error={!!props.error}
                    />
                }
            />
        </div>
    )
}
