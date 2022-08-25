import React from 'react';

// components
import DeleteIcon from '@material-ui/icons/Delete';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

// local
import DesignerFolderItemPicker from '../../Development/Designer/DesignerFolderItemPicker';
import DesignerTextField from '../../Development/Designer/DesignerTextField';
import DesignerSelectField from '../../Development/Designer/DesignerSelectField';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import { DatasetItemCSS } from "./SecurityFilterCSS";


import { Accordion, AccordionSummary, AccordionDetails } from '../../../Report/filters/FiltersCSS';
/**
 * 
 * @param {*} props.name : имя датасета для отображения в заголовке и в компоненте выбора
 * @param {*} props.filterInstanceFields : поля экземпляра фильтра
 * @param {*} props.datasetFields : поля датасета
 * @param {*} props.fieldsMapping : поля экземпляра фильтра
 * @param {*} props.disabled : признак недоступности компонента
 * @param {*} props.onDelete : callback при удалении карточки
 * @param {*} props.onSelectDataset : callback при изменении датасета
 * @param {*} props.onChangeFieldDataset : callback при изменении полей датасета
 */

function getIdOrUndefined(v){
    return v === undefined || v === null ? undefined : v.id;

}

export default function DatasetItem(props){
    const classes = DatasetItemCSS(); 

    function handleChange(id, item, folderId){
        props.onSelectDataset(id, item)
    }

    function handleChangeDatasetField(filterInstanceFieldId, dataSetFieldId){
        props.onChangeFieldDataset(filterInstanceFieldId, dataSetFieldId)
    }

    return (
    <Accordion defaultExpanded style={{backgroundColor: `inherit`, width: '100%'}} elevation={0}>
        <AccordionSummary>
            
           <Typography variant='h6' className={classes.accordionTitle}> 
                {props.name || 'Набор данных не выбран'}
           </Typography>
            {
                !props.disabled &&
                <IconButton 
                    aria-label="settings"
                    onClick={props.onDelete}
                >
                    <DeleteIcon />
                </IconButton>
            }
        
        </AccordionSummary>
        <AccordionDetails>
            <div className={classes.divAccordionContent}>
            <DesignerFolderItemPicker
                label = "Набор данных"
                value = {props.name ? props.name : null}
                itemType = {FolderItemTypes.dataset}
                onChange = {handleChange}
                displayBlock
                fullWidth
                error = {props.name === undefined}
                disabled={props.disabled}
            />
            <Typography variant="body2" color="textSecondary" component="p" gutterBottom>
                Сопоставьте поля экземпляра фильтра с полями набора данных:
            </Typography>
            
            {
                props.filterInstanceFields.map((item, index) => {
                    let datasetFieldId = getIdOrUndefined(props.datasetFields.find( 
                        (f) => {
                            let v = props.fieldsMapping.find( 
                                f => (f.filterInstanceFieldId === item.id)
                            )
                            return v === undefined ? false : f.id === v.dataSetFieldId
                        }
                    ));
                    return (
                    <div key={index} className={classes.divCompare}>
                        <DesignerTextField
                            //label = "Поле фильтра"
                            value = {item.name}
                            onChange = {null}
                            displayBlock
                            fullWidth
                            disabled
                            //variant = "standard"
                        />
                        
                        <Divider orientation="vertical" flexItem  className={classes.divCompare} variant="middle" />
                        <DesignerSelectField
                           // label = "Поле набора данных"
                            data = {props.datasetFields}
                            value = {datasetFieldId }
                            onChange = {id => {handleChangeDatasetField(item.id, id)}}
                            displayBlock
                            fullWidth
                            error = {datasetFieldId === undefined}
                            disabled={props.disabled || props.name === undefined}
                        />
                    </div>
                    )
                })
            }
            </div>
        </AccordionDetails>
    </Accordion>
    )
};