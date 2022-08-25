import React from 'react';
import {useState, useRef} from 'react';

// material-ui

import Grid from '@material-ui/core/Grid';

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';

//local
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {convertTokenInputFilterToLocalData} from "./converters";
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";
import ViewerChildCard from "main/Main/Development/Viewer/ViewerChildCard";

/**
 * Компонент для просмотра полей шаблона фильтра TOKEN_INPUT
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterInstanceData - объект filterInstance
 */
export default function TokenInputFieldsViewer(props) {

    let {localData} = convertTokenInputFilterToLocalData(props.filterInstanceData);

    const [datasetData, setDatasetData] = useState({});
    const datasetFieldsNameMap = useRef(new Map());

    const loadFuncDataset = dataHub.datasetController.get;
    const loadParamsDataset = [props.filterInstanceData.dataSetId];

    function buildDatasetFieldsNameMap(datasetData) {
        datasetFieldsNameMap.current = new Map();
        for (let f of datasetData.fields) {
            datasetFieldsNameMap.current.set(f.id, f.name);
        }
    }

    function handleDatasetLoaded(datasetData) {
        buildDatasetFieldsNameMap(datasetData);
        setDatasetData(datasetData);
    }

    function handleDataLoadFailed(message) {

    }

    return (
        <div>
            <DataLoader
                loadFunc={loadFuncDataset}
                loadParams={loadParamsDataset}
                onDataLoaded={handleDatasetLoaded}
                onDataLoadFailed={handleDataLoadFailed}
                disabledScroll={true}
            >
                {
                    <ViewerChildCard
                        id={localData.dataSetId}
                        name={datasetData.name}
                        itemType={FolderItemTypes.dataset}
                    />
                }


                {datasetData &&
                <div>
                    <Grid container>

                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <ViewerTextField
                                label="Название поля ID"
                                value={localData.datasetFields.idField.name}
                            />
                        </Grid>

                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <ViewerTextField
                                label="Описание поля ID"
                                value={localData.datasetFields.idField.description}
                            />
                        </Grid>

                        <Grid item xs={4}>
                            <ViewerTextField
                                label="Поле ID набора данных"
                                value={datasetFieldsNameMap.current.get(localData.datasetFields.idField.id)}
                            />
                        </Grid>

                    </Grid>

                    <Grid container>
                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <ViewerTextField
                                label="Название поля NAME"
                                value={localData.datasetFields.nameField.name}
                            />
                        </Grid>

                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <ViewerTextField
                                label="Описание поля NAME"
                                value={localData.datasetFields.nameField.description}
                            />
                        </Grid>

                        <Grid item xs={4}>
                            <ViewerTextField
                                label="Поле NAME набора данных"
                                value={datasetFieldsNameMap.current.get(localData.datasetFields.nameField.id)}
                            />
                        </Grid>
                    </Grid>
                </div>
                }
            </DataLoader>
        </div>
    );
}
