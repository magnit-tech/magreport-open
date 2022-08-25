import React from 'react';

// components
import ListItem from "@material-ui/core/ListItem";

// local
import ReportFieldItem from './ReportFieldItemViewer';

/**
 * Компонент просмотра полей отчета
 * @param {Object} props - свойства компонента
 * @param {Array} props.fields - поля отчета
 * @param {Object} props.dataSet - набор данных
 * @return {JSX.Element}
 * @constructor
 */
export default function ReportFieldsViewer(props) {
    return (
        <div>
            {props.fields.map((item, index) =>
                <ListItem>
                    <ReportFieldItem
                        key={item.id}
                        name={item.name}
                        visible={item.visible}
                        dataSetFieldId={item.dataSetFieldId}
                        valid={item.valid}
                        description={item.description}
                        open={item.open}
                        index={index}
                        dataSetFields={props.dataSet.fields}
                    />
                </ListItem>
            )
            }
        </div>
    )
}
