import React, {useState} from "react";
import {connect} from "react-redux";
import {useSnackbar} from "notistack";

import Card from "@material-ui/core/Card";
import IconButton from "@material-ui/core/IconButton";
import AddCircle from "@material-ui/icons/AddCircle";

// components
import DesignerTextField from "main/Main/Development/Designer/DesignerTextField";
import DesignerFolderItemPicker from "main/Main/Development/Designer/DesignerFolderItemPicker";
import DesignerSelectField from "main/Main/Development/Designer/DesignerSelectField";
import DesignerFolderBrowser from "main/Main/Development/Designer/DesignerFolderBrowser";
import ASMSecurityFilterCard from "./ASMSecurityFilterCard";


// local
//import {ASMCSS as useStyles} from "./ASMCSS";
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";
import {
    actionAsmDesignerAddSecuritySourceField,
    actionAsmDesignerAddSecuritySourceSecurityFilter,
    actionAsmDesignerChangeSecuritySourceData,
    actionAsmDesignerSetSecuritySourceDataSet,
    actionAsmDesignerSetSecuritySourceField
} from "redux/actions/admin/actionAsmDesigner";
import {
    selectDataTypeName,
    selectDataSet,
    selectSecuritySourceDataValue,
    selectSecuritySourceErrorValue,
    selectSourceSecurityFilters,
    selectSecuritySourceFieldErrorValue
} from "redux/reducers/admin/asmDesigner/asmDesignerSelectors";
import {
    DATASET_FIELD_ID,
    DESCRIPTION,
    FIELD_TYPE,
    FIELDS,
    FILTER_VALUE_FIELD,
    NAME, PERMISSION_SOURCE,
    POST_SQL,
    PRE_SQL,
    SOURCE_TYPE
} from "utils/asmConstants";

/**
 * @callback changeData
 * @param {Number} index
 * @param {String} key
 * @param {*} value
 */

/**
 * @callback setDataSet
 * @param {Number} index
 * @param {Object} dataSet
 */

/**
 * @callback setField
 * @param {Number} sourceIndex
 * @param {Number} fieldIndex
 * @param {{fieldType, dataSetFieldId?: 0, filterInstanceFields?: []}} oldField
 * @param {{fieldType, dataSetFieldId?: 0, filterInstanceFields?: []}} newField
 */

/**
 * @callback addField
 * @param {Number} sourceIndex
 * @param {{fieldType, dataSetFieldId?: 0, filterInstanceFields?: []}} field
 */

/**
 * @callback addSecurityFilter
 * @param {Number} sourceIndex
 * @param {Object} securityFilter
 */

/**
 * Карточка с Security Source из объекта ASM
 * @param {Object} props.state - asmDesigner state object
 * @param {Number} props.index - индекс security source в State
 * @param {changeData} props.changeData - меняет значение поля source в state
 * @param {setDataSet} props.setDataSet - устанавливает dataSet в state для данного Source
 * @param {setField} props.setField - меняет выбор поля
 * @param {addField} props.addField - добавляет новое поле
 * @param {addSecurityFilter} props.addSecurityFilter - добавляет новый SecurityFilter и связанный с ним FilterInstance
 * @returns {JSX.Element}
 * @constructor
 */
function ASMSecuritySourceCard(props) {
    const {enqueueSnackbar} = useSnackbar();
    //const classes = useStyles();

    const state = props.state;

    const index = props.index;

    const selectDataValue = (key) => selectSecuritySourceDataValue(state, index, key);
    const selectErrorValue = (key) => selectSecuritySourceErrorValue(state, index, key);

    const dataSet = selectDataSet(state, index);
    const securityFilters = selectSourceSecurityFilters(state, index);

    const sourceType = selectDataValue(SOURCE_TYPE);
    const name = selectDataValue(NAME);
    const description = selectDataValue(DESCRIPTION);
    const fields = selectDataValue(FIELDS);
    const preSql = selectDataValue(PRE_SQL);
    const postSql = selectDataValue(POST_SQL);


    let dataSetFields = [];
    if ("fields" in dataSet) {
        dataSetFields.push({id: 0, name: "<Удалить>"});

        dataSet.fields.forEach((field) => {
            dataSetFields.push({
                id: field.id,
                name: `${field.name} (${selectDataTypeName(state, field.typeId)})`
            });
        });
    }


    const [securityFilterDialogOpened, setSecurityFilterDialogOpened] = useState(false);

    let fieldItems = [];

    let securityFilterItems = [];


    function handleAddSecurityFilterClick() {
        setSecurityFilterDialogOpened(true);
    }

    function handleCloseSecurityFilterDialog() {
        setSecurityFilterDialogOpened(false);
    }

    function handleSelectSecurityFilterClick(securityFilterId, securityFilter) {
        if (securityFilters.indexOf(securityFilterId) > -1) {
            enqueueSnackbar(`Фильтр безопасности ${securityFilter.name} уже добавлен.`, {variant: "error"});
            return;
        }

        props.addSecurityFilter(index, securityFilter);
        setSecurityFilterDialogOpened(false);
    }

    fields.forEach((field, fieldIndex) => {
        const isError = selectSecuritySourceFieldErrorValue(state, index, fieldIndex, DATASET_FIELD_ID);

        fieldItems.push(
            <div key={fieldIndex}>
                <DesignerSelectField
                    fullWidth
                    label={field[FIELD_TYPE]}
                    value={field[DATASET_FIELD_ID] ? field[DATASET_FIELD_ID] : ""}
                    data={dataSetFields}
                    error={isError}
                    onChange={(dataSetFieldId) => props.setField(index, fieldIndex, field, {...field, dataSetFieldId})}
                />
            </div>
        );
    });

    securityFilters.forEach((securityFilter, securityFilterIndex) => {
        securityFilterItems.push(
            <ASMSecurityFilterCard
                key={securityFilterIndex}
                sourceIndex={index}
                securityFilterIndex={securityFilterIndex}
            />
        );
    });


    return (
        <div /*className={classes.panelSpacing}*/>
            <DesignerTextField
                label="Название"
                value={name}
                //displayBlock
                fullWidth
                onChange={value => props.changeData(index, NAME, value)}
                error={selectErrorValue(NAME)}
            />
            <DesignerTextField
                label="Описание"
                value={description}
                //displayBlock
                fullWidth
                onChange={value => props.changeData(index, DESCRIPTION, value)}
                error={selectErrorValue(DESCRIPTION)}
            />
            <DesignerFolderItemPicker
                label={"Набор данных"}
                value={dataSet ? dataSet.name : null}
                itemType={FolderItemTypes.dataset}
                onChange={(itemId, item) => props.setDataSet(index, item)}
                displayBlock
                fullWidth
                error={selectErrorValue(DATASET_FIELD_ID)}
                disabled={false}
            />
            <DesignerTextField
                label="PreSQL"
                value={preSql}
                displayBlock
                fullWidth
                multiline
                onChange={value => props.changeData(index, PRE_SQL, value)}
                error={selectErrorValue(PRE_SQL)}
            />
            <DesignerTextField
                label="PostSQL"
                value={postSql}
                displayBlock
                fullWidth
                multiline
                onChange={value => props.changeData(index, POST_SQL, value)}
                error={selectErrorValue(POST_SQL)}
            />

            {fieldItems}

            {sourceType === PERMISSION_SOURCE ?
                <div>
                    <div>
                        <DesignerSelectField
                            fullWidth
                            label={FILTER_VALUE_FIELD}
                            value={""}
                            data={dataSetFields}
                            onChange={dataSetFieldId => props.addField(index, {fieldType: FILTER_VALUE_FIELD, dataSetFieldId})}
                        />
                    </div>
                    {securityFilterItems}
                    <Card>
                        <DesignerFolderBrowser
                            itemType={FolderItemTypes.securityFilters}
                            dialogOpen={securityFilterDialogOpened}
                            onChange={handleSelectSecurityFilterClick}
                            onClose={handleCloseSecurityFilterDialog}
                        />
                    </Card>
                    <div style={{textAlign: 'center'}}>
                    <IconButton
                        aria-label="add"
                        color="primary"
                        onClick={handleAddSecurityFilterClick}
                        disabled={false}
                    >
                        <AddCircle
                            fontSize='large'
                        />
                    </IconButton>
                    </div>
                </div>
                : null
            }
        </div>
    );
}

const mapStateToProps = (state) => {
    return {
        state: state.asmDesigner
    }
}

const mapDispatchToProps = {
    changeData: actionAsmDesignerChangeSecuritySourceData,
    setDataSet: actionAsmDesignerSetSecuritySourceDataSet,
    setField: actionAsmDesignerSetSecuritySourceField,
    addField: actionAsmDesignerAddSecuritySourceField,
    addSecurityFilter: actionAsmDesignerAddSecuritySourceSecurityFilter
}

export default connect(mapStateToProps, mapDispatchToProps)(ASMSecuritySourceCard);