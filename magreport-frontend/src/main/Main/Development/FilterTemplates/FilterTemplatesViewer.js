import React, {useState} from 'react';
import {useSnackbar} from 'notistack';

// components
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import Divider from '@material-ui/core/Divider';

// local
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";
import ViewerPage from "../Viewer/ViewerPage";

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';
import {createViewerPageName} from "../Viewer/viewerHelpers";
import {FolderItemTypes} from "../../../FolderContent/FolderItemTypes";

/**
 * Просмотрщик шаблона фильтра
 * @param {Object} props - свойства компонента
 * @param {Number} props.filterTemplateId - ID просматриваемого шаблона фильтра
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки "ОК"
 * @return {JSX.Element}
 * @constructor
 */
export default function FilterTemplatesViewer({filterTemplateId = -1, onOkClick = f => f}) {

    const {enqueueSnackbar} = useSnackbar();

    const [data, setData] = useState({});

    let loadFunc = dataHub.filterTemplateController.get;
    let loadParams = [filterTemplateId];

    /*
        Data loading
    */

    function handleDataLoaded(loadedData) {
        setData({
            ...data,
            ...loadedData
        });
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При получении данных возникла ошибка: ${message}`,
            {variant: "error"});
    }

    // fields components
    const fields = (data.fields ? data.fields : []).map((field, i) =>
        <div
            key={i}
            style={{marginTop: '4px' }} //, borderBottom: '2px solid #CCC'}}
        >
            <Grid container spacing={2}>
                <Grid item xs={12} md={2}>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <ViewerTextField
                                woStyle
                                label="ID"
                                value={field.id}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <ViewerTextField
                                woStyle
                                label="Уровень"
                                value={field.level}
                            />
                        </Grid>
                    </Grid>
                </Grid>
                <Grid item xs={12} md={6}>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <ViewerTextField
                                woStyle
                                label="Название"
                                value={field.name}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <ViewerTextField
                                woStyle
                                label="Описание"
                                value={field.description}
                            />
                        </Grid>
                    </Grid>
                </Grid>
                <Grid item xs={12} md={4}>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <ViewerTextField
                                woStyle
                                label="Тип поля"
                                value={field.type}
                            />
                        </Grid> 
                        <Grid item xs={12}>
                            <ViewerTextField
                                woStyle
                                label="Связанное поле"
                                value={field.linkedId}
                            />
                        </Grid>
                    </Grid>
                </Grid>  
                
            </Grid>
            <Divider style={{margin: '16px 0px', height: '2px'}}/>
        </div>
    )

    // building component
    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={loadParams}
            onDataLoaded={handleDataLoaded}
            onDataLoadFailed={handleDataLoadFailed}
        >
                <ViewerPage
                    pageName={createViewerPageName(FolderItemTypes.filterTemplate, data.name)}
                    id={data.id}
                    itemType={FolderItemTypes.filterTemplate}
                    readOnly
                    onOkClick={onOkClick}
                >
                    <ViewerTextField
                        label={"Название"}
                        value={data.name}
                    />

                    <ViewerTextField
                        label={"Описание"}
                        value={data.description}
                    />

                    <ViewerTextField
                        label={"Тип шаблона фильтра"}
                        value={data.type ? data.type.name : ""}
                    />

                    <ViewerTextField
                        multiline
                        label={"Поддерживаемые операции"}
                        value={(data.supportedOperations ? data.supportedOperations : []).join(" | ")}
                    />

                    <Typography
                        align="left"
                        color="textSecondary"
                    >
                        Поля шаблона фильтра:
                    </Typography>

                    {fields}

                </ViewerPage>
        </DataLoader>
    );
}
