import React from "react";

// local
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";
import ViewerTextField from "../../Development/Viewer/ViewerTextField";
import ViewerChildCard from "../../Development/Viewer/ViewerChildCard";
import {ViewerCSS} from "../../Development/Viewer/ViewerCSS";
import ViewerFieldMapping from "../../Development/Viewer/ViewerFieldMapping";

/**
 * Карточка просмотра Security Source из объекта ASM
 * @param {Object} props - свойства компонента
 * @param {Object} props.securitySource - security source
 * @param {Object} props.fieldMappings - связи полей набора данных и экземпляра фильтра, в разрезе фильтров безопасности
 * @returns {JSX.Element}
 * @constructor
 */
export default function ASMSecuritySourceViewer(props) {

    const classes = ViewerCSS();

    const securitySource = props.securitySource;

    const dataSet = securitySource.dataSet || {};
    const securityFilters = securitySource.securityFilters || [];
    const fields = securitySource.fields || [];

    let fieldItems = [];
    let securityFilterItems = [];

    // building component

    fields.forEach((field, fieldIndex) => {

        fieldItems.push(
            <div key={fieldIndex}>
                <ViewerTextField
                    label={field.fieldType}
                    value={(field.dataSetField || {}).name}
                />
            </div>
        );
    });

    securityFilters.forEach((securityFilterWrapper, securityFilterIndex) => {
        const securityFilter = securityFilterWrapper.securityFilter;
        const fieldMappings = props.fieldMappings[securityFilterIndex];

        const securityFilterFieldMappings = [];
        fieldMappings.forEach(fm => {

            let dataSetFieldName;
            let filterInstanceFieldName;

            for(let securityFilterDataSetWrapper of securityFilter.dataSets) {
                const securityFilterDataSet = securityFilterDataSetWrapper.dataSet;
                for(let dataSetField of securityFilterDataSet.fields) {
                    if(fm.dataSetFieldId === dataSetField.id) {
                        dataSetFieldName = dataSetField.name;
                        break
                    }
                }
            }

            for(let filterInstanceField of securityFilter.filterInstance.fields) {
                if(fm.filterInstanceFieldId === filterInstanceField.id) {
                    filterInstanceFieldName = filterInstanceField.name;
                    break;
                }
            }

            securityFilterFieldMappings.push(
                <ViewerFieldMapping
                    leftLabel={"Поле набора данных"}
                    leftValue={dataSetFieldName}
                    rightLabel={"Поле фильтра"}
                    rightValue={filterInstanceFieldName}
                />)
        })
        securityFilterItems.push(
            <ViewerChildCard
                key={securityFilterIndex}
                id={securityFilter.id}
                name={securityFilter.name}
                itemType={FolderItemTypes.securityFilters}
            >
                {securityFilterFieldMappings}
            </ViewerChildCard>
        );
    });

    return (
        <div className={classes.viewerTabPage}>
            <ViewerTextField
                label="Название"
                value={securitySource.name}
            />
            <ViewerTextField
                label="Описание"
                value={securitySource.description}
            />
            <ViewerChildCard
                id={dataSet.id}
                name={dataSet.name}
                itemType={FolderItemTypes.dataset}
            />
            <ViewerTextField
                label="PreSQL"
                value={securitySource.preSql}
            />
            <ViewerTextField
                label="PostSQL"
                value={securitySource.postSql}
            />

            {fieldItems}

            {securityFilterItems}

        </div>
    );
}
