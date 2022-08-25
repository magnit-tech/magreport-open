import React, {useRef, useState} from 'react';

// material-ui
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import FormGroup from '@material-ui/core/FormGroup';
import {Typography} from '@material-ui/core';

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';

//local
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";
import ViewerChildCard from "main/Main/Development/Viewer/ViewerChildCard";
import StyleConsts from 'StyleConsts';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

import {convertHierTreeFilterToLocalData} from "./converters";
// styles
import {HierTreeFieldsCSS} from '../FilterInstanceCSS';
import List from "@material-ui/core/List";

/**
 * Компонент для просмотра полей шаблона фильтра  типа HIERARCHY
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterInstanceData - объект filterInstance
 */
export default function HierTreeFieldsViewer(props) {
    const classes = HierTreeFieldsCSS();
    let localData = convertHierTreeFilterToLocalData(props.filterInstanceData);

    const [datasetData, setDatasetData] = useState({});
    const datasetFieldsNameMap = useRef(new Map());

    const loadFuncDataset = dataHub.datasetController.get;
    const loadFuncParams = [props.filterInstanceData.dataSetId];

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

    const items = localData.levels.map((v, i) => {
        let levelsLength = localData.levels.length;
        let checkedExpand = (i + 1 !== levelsLength) || v.idField.expand ||
            (localData.levels[i + 1] !== undefined && localData.levels[i + 1].idField.expand);

        return (
            <Paper elevation={3} key={i} className={classes.levelFields}>
                <Grid container>
                    <Grid container justifyContent="space-between" spacing={2} style={{marginBottom: '4px'}}>
                        <Grid item xs={6} style={{display: 'flex', alignItems: 'center'}}>
                            <Typography variant='h6'>
                                Уровень {parseInt(i) + 1}
                            </Typography>
                            {props.filterTemplateType === 'HIERARCHY' &&
                            <Typography color="textSecondary">
                                {checkedExpand ? "Прокидываемый" : "Не прокидываемый"}
                            </Typography>
                            }
                        </Grid>
                    </Grid>


                    <Grid item xs={4} style={{paddingRight: '16px'}}>
                        <ViewerTextField
                            label="Название поля ID"
                            value={v.idField.name}
                        />
                    </Grid>

                    <Grid item xs={4} style={{paddingRight: '16px'}}>
                        <ViewerTextField
                            label="Описание поля ID"
                            value={v.idField.description}
                        />
                    </Grid>

                    <Grid item xs={4}>
                        <ViewerTextField
                            label="Поле ID набора данных"
                            value={datasetFieldsNameMap.current.get(v.idField.dataSetFieldId)}
                        />
                    </Grid>

                </Grid>

                <Grid container>

                    <Grid item xs={4} style={{paddingRight: '16px'}}>
                        <ViewerTextField
                            label="Название поля NAME"
                            value={v.nameField.name}
                        />
                    </Grid>

                    <Grid item xs={4} style={{paddingRight: '16px'}}>
                        <ViewerTextField
                            label="Описание поля NAME"
                            value={v.nameField.description}
                        />
                    </Grid>

                    <Grid item xs={4}>
                        <ViewerTextField
                            label="Поле NAME набора данных"
                            value={datasetFieldsNameMap.current.get(v.nameField.dataSetFieldId)}
                        />
                    </Grid>

                </Grid>

            </Paper>)
    });

    return (
        <div style={{minWidth: StyleConsts.designerTextFieldMinWidth}}>
            <DataLoader
                loadFunc={loadFuncDataset}
                loadParams={loadFuncParams}
                onDataLoaded={handleDatasetLoaded}
                onDataLoadFailed={handleDataLoadFailed}
            >
                {
                    <ViewerChildCard
                        id={localData.dataSetId}
                        name={datasetData.name}
                        itemType={FolderItemTypes.dataset}
                    />
                }
                {datasetData &&
                <div style={{display: 'flex', flex: 1, flexDirection: 'column'}}>
                    <FormGroup>
                        <List>
                            {items}
                        </List>
                    </FormGroup>
                </div>
                }

            </DataLoader>
        </div>
    );
}
