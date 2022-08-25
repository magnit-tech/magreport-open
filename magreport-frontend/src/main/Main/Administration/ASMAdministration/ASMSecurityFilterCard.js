import React from "react";
import {connect} from "react-redux";

import CardHeader from "@material-ui/core/CardHeader";
import IconButton from "@material-ui/core/IconButton";
import ClearIcon from "@material-ui/icons/Clear";
import CardContent from "@material-ui/core/CardContent";
import Card from "@material-ui/core/Card";
import Grid from "@material-ui/core/Grid";


// components
import DesignerSelectField from "main/Main/Development/Designer/DesignerSelectField";

// local
import {ASMCSS as useStyles} from "./ASMCSS";
import {
    actionAsmDesignerAddSecurityFilterFieldMapping,
    actionAsmDesignerDeleteSecuritySourceSecurityFilter,
    actionAsmDesignerUpdateSecurityFilterFieldMapping
} from "redux/actions/admin/actionAsmDesigner";
import {DATASET_FIELD_ID, FILTER_INSTANCE_FIELD_ID} from "utils/asmConstants";
import {
    selectDataSetFieldsForSecurityFilter,
    selectDataTypeName,
    selectFieldMappings,
    selectFieldMappingsErrors,
    selectFilterInstanceFields,
    selectSecurityFilter,
} from "redux/reducers/admin/asmDesigner/asmDesignerSelectors";


/**
 * @callback addFieldMapping
 * @param {Number} sourceIndex
 * @param {Number} securityFilterIndex
 * @param {{dataSetFieldId: Number, filterInstanceFieldId: Number}} fieldMapping
 */

/**
 * @callback updateFieldMapping
 * @param {Number} sourceIndex
 * @param {Number} securityFilterIndex
 * @param {Number} fieldMappingIndex
 * @param {{dataSetFieldId: Number, filterInstanceFieldId: Number}} oldFieldMapping
 * @param {{dataSetFieldId: Number, filterInstanceFieldId: Number}} newFieldMapping
 */


/**
 * @callback deleteSecurityFilter
 * @param {Number} sourceIndex
 * @param {Number} securityFilterIndex
 * @param {Object[]} deletedFieldMappings
 */

/**
 * Карточка Security Filter, входящего в Security Source
 * @param {Object} props - component properties
 * @param {Object} props.state - redux state
 * @param {Number} props.sourceIndex - индекс объекта Security Source, в который входит данных Security Filter
 * @param {Number} props.securityFilterIndex - индекс данного объекта внутри Security Source
 * @param {addFieldMapping} props.addFieldMapping - добавляет новую связку поля набора данных и экземпляра фильтра
 * @param {updateFieldMapping} props.updateFieldMapping - меняет значение полей в связке
 * @param {deleteSecurityFilter} props.deleteSecurityFilter - удаляет этот SecurityFilter
 * @returns {JSX.Element}
 * @constructor
 */
function ASMSecurityFilterCard(props) {
    const classes = useStyles();
    const state = props.state;

    const sourceIndex = props.sourceIndex;
    const securityFilterIndex = props.securityFilterIndex;

    const securityFilter = selectSecurityFilter(state, sourceIndex, securityFilterIndex);
    const fieldMappings = selectFieldMappings(state, sourceIndex, securityFilterIndex);
    const fieldMappingsErrors = selectFieldMappingsErrors(state, sourceIndex, securityFilterIndex);

    // extract dataset fields and filter instance fields
    let dataSetFields = selectDataSetFieldsForSecurityFilter(state, sourceIndex, securityFilterIndex);
    let filterInstanceFields = selectFilterInstanceFields(state, sourceIndex, securityFilterIndex);

    dataSetFields = [
        {id: 0, name: "<Удалить>"},
        ...dataSetFields.map(field => ({
            id: field.id,
            name: `${field.name} (${selectDataTypeName(state, field.typeId)})`
        }))
    ];

    filterInstanceFields = [{id: 0, name: "<Удалить>"},
        ...filterInstanceFields.map(field => ({
            id: field.id,
            //TODO: display name with data Type
            name: field.name
        }))
    ];


    const fieldMappingItems = fieldMappings
        .map((fieldMapping, fieldMappingIndex) => {
            const dataSetFieldId = fieldMapping[DATASET_FIELD_ID];
            const filterInstanceFieldId = fieldMapping[FILTER_INSTANCE_FIELD_ID];
            const isDataSetFieldIdError = fieldMappingsErrors[fieldMappingIndex][DATASET_FIELD_ID];
            const isFilterInstanceFieldIdError = fieldMappingsErrors[fieldMappingIndex][FILTER_INSTANCE_FIELD_ID];

            return (
                <Grid container spacing={3} key={fieldMappingIndex}>
                    <Grid item xs={6}>
                        <DesignerSelectField
                            fullWidth
                            label="Поле набора данных"
                            value={dataSetFieldId ? dataSetFieldId : ""}
                            data={dataSetFields}
                            error={isDataSetFieldIdError}
                            onChange={newDataSetFieldId => props.updateFieldMapping(sourceIndex,
                                securityFilterIndex,
                                fieldMappingIndex,
                                fieldMapping,
                                {dataSetFieldId: newDataSetFieldId, filterInstanceFieldId})}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <DesignerSelectField
                            fullWidth
                            label="Поле фильтра"
                            value={filterInstanceFieldId ? filterInstanceFieldId : ""}
                            data={filterInstanceFields}
                            error={isFilterInstanceFieldIdError}
                            onChange={newFilterInstanceFieldId => props.updateFieldMapping(sourceIndex,
                                securityFilterIndex,
                                fieldMappingIndex,
                                fieldMapping,
                                {dataSetFieldId, filterInstanceFieldId: newFilterInstanceFieldId})}
                        />
                    </Grid>
                </Grid>
            )
        })

    return (
        <Card className={classes.panelSpacing}>
            <CardHeader
                title={"Настройка фильтра: " + securityFilter.name}
                action={
                    <IconButton
                        onClick={() => props.deleteSecurityFilter(sourceIndex, securityFilterIndex, fieldMappings)}>
                        <ClearIcon/>
                    </IconButton>
                }
            />
            <CardContent>
                {fieldMappingItems}
                <Grid container spacing={3}>
                    <Grid item xs={6}>
                        <DesignerSelectField
                            fullWidth
                            label="Поле набора данных"
                            value={""}
                            data={dataSetFields}
                            onChange={dataSetFieldId => props.addFieldMapping(sourceIndex, securityFilterIndex,
                                {dataSetFieldId, filterInstanceFieldId: 0})}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <DesignerSelectField
                            fullWidth
                            label="Поле фильтра"
                            value={""}
                            data={filterInstanceFields}
                            onChange={filterInstanceFieldId => props.addFieldMapping(sourceIndex, securityFilterIndex,
                                {dataSetFieldId: 0, filterInstanceFieldId})}
                        />
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}

const mapStateToProps = (state) => {
    return {
        state: state.asmDesigner
    }
}

const mapDispatchToProps = {
    addFieldMapping: actionAsmDesignerAddSecurityFilterFieldMapping,
    updateFieldMapping: actionAsmDesignerUpdateSecurityFilterFieldMapping,
    deleteSecurityFilter: actionAsmDesignerDeleteSecuritySourceSecurityFilter
}

export default connect(mapStateToProps, mapDispatchToProps)(ASMSecurityFilterCard);