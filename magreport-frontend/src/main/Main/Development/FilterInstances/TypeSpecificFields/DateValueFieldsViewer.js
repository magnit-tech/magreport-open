import React from 'react';

// material-ui
import Grid from '@material-ui/core/Grid';

// local
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";
import {convertDateValueFilterToLocalData} from "./converters";

/**
 * Компонент для просмотра полей шаблона фильтра DATE_VALUE
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterInstanceData - объект filterInstance
 */
export default function DateValueFieldsViewer(props) {

    let localData = convertDateValueFilterToLocalData(props.filterInstanceData);

    return (
        <div>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <ViewerTextField
                        label="Название поля даты в отчёте"
                        value={localData.idField.name}
                    />
                </Grid>

                <Grid item xs={6}>
                    <ViewerTextField
                        label="Описание поля даты в отчёте"
                        value={localData.idField.description}
                    />
                </Grid>
            </Grid>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <ViewerTextField
                        label="Название поля даты"
                        value={localData.dateField.name}
                    />
                </Grid>

                <Grid item xs={6}>
                    <ViewerTextField
                        label="Описание поля даты"
                        value={localData.dateField.description}
                    />
                </Grid>
            </Grid>

        </div>
    );
}
