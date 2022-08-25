import React from 'react';

// local
import ReportFieldItem from './ReportFieldItem'
import DraggableItemsList from 'main/DragAndDrop/DraggableItemsList';

/**
 * @callback onFieldChange
 * @param {Number} index
 * @param {String} key
 * @param {String} newValue
 */
/**
 * @callback onDropField
 * @param {Number} index
 */
/**
 * @callback onMove
 * @param {Array} newFields
 */
/**
 * Компонент редактирования полей отчета
 * @param {Object} props - свойства компонента
 * @param {{fields: Array}} props.dataset - Набор данных отчета
 * @param {Array} props.fields - поля отчета
 * @param {onFieldChange} props.onFieldChange - callback, вызываемый при изменении поля отчета
 * @param {onFieldDrop} props.onFieldDrop - callback, вызываемый при удалении поля отчета
 * @param {onFieldMove} props.onFieldMove - callback, вызываемый при изменении порядка поля отчета
 * @return {JSX.Element}
 * @constructor
 */
export default function ReportFields(props){

    function handleFieldMove(sourceGroupId, sourceIndex, targetGroupId, targetIndex){
        props.onFieldMove(sourceIndex, targetIndex);
    }
    
    
    return (
        <div>
            <DraggableItemsList
                items = {
                    props.fields.map((item, index) => 
                    <ReportFieldItem 
                        key={item.id}
                        name={item.name}
                        visible={item.visible}
                        dataSetFieldId={item.dataSetFieldId}
                        valid={item.valid}
                        description={item.description}
                        open={item.open}
                        index={index}
                        datasetFields={props.dataset.fields}
                        onFieldChange={props.onFieldChange}
                        onDropField={props.onFieldDrop}
                    />)
                }
                groupId = "group_report_fields"
                onMove={handleFieldMove}
            />
        </div>
    )
};