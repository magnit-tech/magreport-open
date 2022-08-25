import React from 'react';

// material-ui
import Grid from '@material-ui/core/Grid';

// local
import ViewerTextField from 'main/Main/Development/Viewer/ViewerTextField';
import {convertDateRangeFilterToLocalData} from "./converters";
import {getRangeFieldsLabels} from "./helpers";


/**
 * Компонент просмотра полей шаблона фильтра RANGE, DATE_RANGE
 * @param {Object} props - свойства компонента
 * @param {string} props.filterTemplateType - тип шаблона фильтра (RANGE/DATE_RANGE)
 * @param {Object} props.filterInstanceData - объект filterInstance
 */
export default function RangeFieldsViewer(props) {

    let localData = convertDateRangeFilterToLocalData(props.filterInstanceData);
    const fieldLabels = getRangeFieldsLabels(props.filterTemplateType);

    return (
        <div>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <ViewerTextField
                        label={fieldLabels.fieldName}
                        value={localData.idField.name}
                    />
                </Grid>

                <Grid item xs={6}>
                    <ViewerTextField
                        label={fieldLabels.fieldDescription}
                        value={localData.idField.description}
                    />
                </Grid>
            </Grid>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <ViewerTextField
                        label={fieldLabels.startFieldName}
                        value={localData.fromField.name}
                    />
                </Grid>

                <Grid item xs={6}>
                    <ViewerTextField
                        label={fieldLabels.startFieldDescription}
                        value={localData.fromField.description}
                    />
                </Grid>
            </Grid>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <ViewerTextField
                        label={fieldLabels.endFieldName}
                        value={localData.toField.name}
                    />
                </Grid>

                <Grid item xs={6}>
                    <ViewerTextField
                        label={fieldLabels.endFieldDescription}
                        value={localData.toField.description}
                    />
                </Grid>
            </Grid>
        </div>
    );
}
