import React, {useState} from "react";
import {connect} from "react-redux";
import {useSnackbar} from "notistack";
import {useNavigateBack} from "main/Main/Navbar/navbarHooks";

// components
import DesignerPage from "main/Main/Development/Designer/DesignerPage";
import PageTabs from 'main/PageTabs/PageTabs';
import DesignerTextField from "main/Main/Development/Designer/DesignerTextField";
import ExternalSecuritySource from "./ASMSecuritySourceCard";
import CircularProgress from '@material-ui/core/CircularProgress';

// local
import dataHub from "ajax/DataHub";
import {hideAlertDialog, showAlertDialog} from "redux/actions/actionsAlertDialog";
import {
    actionAsmDesignerChangeRootData,
    actionAsmDesignerDataTypesLoadFailed,
    actionAsmDesignerDataTypesLoaded
} from "redux/actions/admin/actionAsmDesigner";
import {actionAsmAdded, actionAsmEdited, actionAsmListShow,
    actionAsmDataLoaded, actionAsmDataLoadFailed} from "redux/actions/admin/actionAsm";
import {
    selectData,
    selectHasErrors,
    selectDataValue,
    selectErrorValue,
} from "redux/reducers/admin/asmDesigner/asmDesignerSelectors";
import {
    DESCRIPTION,
    NAME,
    ROLE_TYPE_ID,
    SECURITY_SOURCES,
    ASM_DESIGNER_CREATE_MODE,
    ASM_DESIGNER_EDIT_MODE
} from "utils/asmConstants";
import DataLoader from "main/DataLoader/DataLoader";
import DesignerSelectField from "main/Main/Development/Designer/DesignerSelectField";
import { ASMCSS as useStyles} from "./ASMCSS";

/**
 * @callback actionAsmDesignerChangeRootData
 * @param {String} key
 * @param {(String|Number)} value
 */

/**
 * @callback actionAsmListShow
 */

/**
 * @callback actionAsmAdded
 * @param {Object} data
 */

/**
 * @callback actionAsmEdited
 * @param {Object} data
 */

/**
 * @callback showAlertDialog
 */

/**
 * @callback hideAlertDialog
 */

/**
 * @callback actionAsmDataLoaded
 * @param {Object} data
 */

/**
 * @callback actionAsmDataLoadFailed
 * @param {String} error
 */

/**
 * @callback actionAsmDesignerDataTypesLoaded
 * @param {Object} data
 */

/**
 * @callback actionAsmDesignerDataTypesLoadFailed
 * @param {String} error
 */

/**
 * Дизайнер объекта ASM для создания нового/редактирования старого
 * @param {Object} props - component properties
 * @param {Number} props.asmId - ID редактируемого объекта ASM
 * @param {String} props.designerMode - режим дизайнера - значение константы из utils/asmConstants
 * @param {Object} props.state - asmDesigner State
 * @param {actionAsmDesignerChangeRootData} props.actionAsmDesignerChangeRootData - меняет значение полей редактируемого ExternalSecurity
 * @param {actionAsmListShow} props.actionAsmListShow - возврат в список всех ASM
 * @param {actionAsmAdded} props.actionAsmAdded - выполняется в случае успешного добавления ASM
 * @param {actionAsmEdited} props.actionAsmEdited - выполняется в случае успешного сохранения изменений в ASM
 * @param {showAlertDialog} props.showAlertDialog - показать диалоговое окно
 * @param {hideAlertDialog} props.hideAlertDialog - скрыть диалоговое окно
 * @param {actionAsmDataLoaded} props.actionAsmDataLoaded - callback, добавляет в state успешно загруженные данных
 * @param {actionAsmDataLoadFailed} props.actionAsmDataLoadFailed - callback, добавляет в state загруженные данные
 * @param {actionAsmDesignerDataTypesLoaded} props.actionAsmDesignerDataTypesLoaded - callback, добавляет в state успешно загруженные типы данных
 * @param {actionAsmDesignerDataTypesLoadFailed} props.actionAsmDesignerDataTypesLoadFailed - callback, добавляет в state загруженные типы данных с ошибками
 * @returns {JSX.Element}
 * @constructor
 */
function ASMDesigner(props) {

    const navigateBack = useNavigateBack();

    const designerMode = props.designerMode;

    const {enqueueSnackbar} = useSnackbar();
    const classes = useStyles();

    const dataTypesLoadFunc = dataHub.datasetController.getDataTypes;

    const roleTypesLoadFunc = dataHub.roleController.getAllTypes;

    const state = props.state;
    const data = selectData(state);
    const [roleTypes, setRoleTypes] = useState([]);

    const roleTypeId = selectDataValue(state, ROLE_TYPE_ID);
    const name = selectDataValue(state, NAME);
    const description = selectDataValue(state, DESCRIPTION);
    const securitySources = selectDataValue(state, SECURITY_SOURCES);

    let loadFunc;
    let loadParams = [];
    if(props.designerMode === ASM_DESIGNER_EDIT_MODE) {
        loadFunc = dataHub.asmController.get;
        loadParams = [props.asmId];
    }

    const sourceItems = [];

    const [uploading, setUploading] = useState(false);
    //const [ASMId, setASMId] = useState(props.state.data.id)

    function handleRoleTypesLoaded(data) {
        setRoleTypes(data);
    }

    function handleRoleTypesLoadFailed(error) {
        enqueueSnackbar(`Ошибка при загрузке типов ролей: ${error}`, {variant: "error"});
    }

    function handleSave(needExit=true /*e*/) {
        //console.log(data);
        const hasErrors = selectHasErrors(props.state);

        if (hasErrors) {
            enqueueSnackbar("Не заполнены все поля формы, или не выбран ни один фильтр безопасности", {variant: "error"});
            return;
        }

        if (designerMode === ASM_DESIGNER_CREATE_MODE) {
            dataHub.asmController.add(
                roleTypeId,
                name,
                description,
                securitySources,
                magrepResponse => handleAddEditAnswer(needExit, magrepResponse)
            );
            setUploading(true);
        } else if (designerMode === ASM_DESIGNER_EDIT_MODE) {
            dataHub.asmController.edit(
                data.id,
                roleTypeId,
                name,
                description,
                securitySources,
                magrepResponse => handleAddEditAnswer(needExit, magrepResponse)
            );
            setUploading(true);
        } else {
            enqueueSnackbar(`Неизвестный режим запуска дизайнера: ${designerMode}`, {variant: "error"});
        }
    }

    function handleAddEditAnswer(needExit, magrepResponse){
        setUploading(false);
        
        if(magrepResponse.ok){
            if (needExit){
                let actionWord = designerMode === ASM_DESIGNER_CREATE_MODE ? "создан" : "обновлён";
                enqueueSnackbar(`ASM ${name} ${actionWord} успешно`, {variant: "success"});
                if(designerMode === ASM_DESIGNER_CREATE_MODE) {
                    props.actionAsmListShow();
                } else {
                    navigateBack();
                }
            }
            /*  else {
                setASMId(magrepResponse.data.id)
            }
            */ 
        }
        else{
            let actionWord = designerMode === ASM_DESIGNER_CREATE_MODE ? "создании" : "обновлении";
            enqueueSnackbar("Ошибка при " + actionWord + " объекта: " + magrepResponse.data, {variant : "error"});
        }
    }

    function handleCancelButtonClick(e) {
        const handleAlertDialogAction = (isOk) => {
            if (isOk) {
                if(designerMode === ASM_DESIGNER_CREATE_MODE) {
                    props.actionAsmListShow();
                } else {
                    navigateBack();
                }
            }
            props.hideAlertDialog();
        };
        props.showAlertDialog("Отменить все изменения?", "External Security", "entity", handleAlertDialogAction);
    }

    securitySources.forEach((securitySource, index) => {
        sourceItems.push(<ExternalSecuritySource key={index} index={index} />);
    })
    function handleTabChange(newValue){
        handleSave(false);
    }

    let tabs = []
    let progressClasses = classes.progress;

    tabs.push({
        tablabel: 'Детали объекта ASM',
        tabcontent: uploading ? <CircularProgress className={progressClasses}/> :
        <DesignerPage
            onSaveClick={handleSave}
            onCancelClick={handleCancelButtonClick}
        >
            <DesignerSelectField
                label="Тип роли"
                value={roleTypeId}
                data={roleTypes}
                error={selectErrorValue(state, ROLE_TYPE_ID)}
                onChange={roleTypeId => props.actionAsmDesignerChangeRootData(ROLE_TYPE_ID, roleTypeId)}
                fullWidth
            />
            <DesignerTextField
                label="Название"
                value={name}
                //displayBlock
                fullWidth
                onChange={value => props.actionAsmDesignerChangeRootData(NAME, value)}
                error={selectErrorValue(state, NAME)}
            />
            <DesignerTextField
                label="Описание"
                value={description}
                displayBlock
                fullWidth
                onChange={value => props.actionAsmDesignerChangeRootData(DESCRIPTION, value)}
                error={selectErrorValue(state, DESCRIPTION)}
            />
        </DesignerPage>
    })


    securitySources.forEach((securitySource, index) => {
        tabs.push({
            tablabel: "Настройка " + securitySource.sourceType,
            tabcontent: uploading ? <CircularProgress  className={progressClasses}/> :
            <DesignerPage
                onSaveClick={handleSave}
                onCancelClick={handleCancelButtonClick}
            >
                <ExternalSecuritySource
                    key={index}
                    index={index}
                />
            </DesignerPage>});
    })

    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={loadParams}
            onDataLoaded={(data) => props.actionAsmDataLoaded(data)}
            onDataLoadFailed={(error) => props.actionAsmDataLoadFailed(error)}
        >
            <DataLoader
                loadFunc={dataTypesLoadFunc}
                loadParams={[]}
                onDataLoaded={(dataTypes) => props.actionAsmDesignerDataTypesLoaded(dataTypes)}
                onDataLoadFailed={(error) => props.actionAsmDesignerDataTypesLoadFailed(error)}
            >
                <DataLoader
                    loadFunc={roleTypesLoadFunc}
                    loadParams={[]}
                    onDataLoaded={roleTypes => handleRoleTypesLoaded(roleTypes)}
                    onDataLoadFailed={error => handleRoleTypesLoadFailed(error)}
                >
                    <PageTabs
                        tabsdata={tabs}
                        onTabChange={handleTabChange}
                        pageName={designerMode === ASM_DESIGNER_CREATE_MODE ? "Создание объекта ASM" : "Редактирование объекта ASM: " + name}
                    />
                </DataLoader>
            </DataLoader>
        </DataLoader>
    );
}

const mapStateToProps = (state) => {
    return {
        state: state.asmDesigner
    }
}

const mapDispatchToProps = {
    showAlertDialog,
    hideAlertDialog,
    actionAsmListShow,
    actionAsmAdded,
    actionAsmEdited,
    actionAsmDesignerChangeRootData,
    actionAsmDataLoaded,
    actionAsmDataLoadFailed,
    actionAsmDesignerDataTypesLoaded,
    actionAsmDesignerDataTypesLoadFailed
}

export default connect(mapStateToProps, mapDispatchToProps)(ASMDesigner);