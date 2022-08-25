import React from 'react';
import {useState} from 'react';
import { useSnackbar } from 'notistack';
import { connect } from 'react-redux';
import { showAlert, hideAlert } from '../../../../redux/actions/actionsAlert'


// local
import DesignerPage from '../Designer/DesignerPage';
import DesignerTextField from '../Designer/DesignerTextField';
import DesignerSelectField from '../Designer/DesignerSelectField';
import StyleConsts from '../../../../StyleConsts';
import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';
import { CircularProgress } from '@material-ui/core';

import Button from '@material-ui/core/Button';
import Alerts from "main/Alerts/Alerts";

import Icon from '@mdi/react'  
import { mdiCheckDecagram } from '@mdi/js';
import { mdiCloseOctagon } from '@mdi/js';

/**
 * 
 * @param {*} props.mode : 'edit', 'create' - режим редактирования или создания нового объекта
 * @param {*} props.datasourceId : id объекта при редактировании (имеет значение только при mode == 'edit')
 * @param {*} props.folderId : id папки в которой размещается объект при создании (имеет значение только при mode == 'create')
 * @param {*} props.onExit : callback при выходе
 */
function DatasourceDesigner(props){

    const classes = ViewerCSS();

    const { enqueueSnackbar } = useSnackbar();

    const [data, setData] = useState({
        datasourceName : null,
        datasourceDescription : null,
        datasourceTypeId : null,
        datasourceUrl : null,
        datasourceUserName : null,
        datasourcePassword : "",
        datasourcePoolSize: 10,
    });

    const [typesData, setTypesData] = useState([]);

    const fieldLabels = {
        datasourceName : "Название",
        datasourceDescription : "Описание",
        datasourceTypeId : "Тип источника",
        datasourceUrl : "Строка подключения",
        datasourceUserName : "Имя пользователя",
        datasourcePassword : "Пароль",
        datasourcePoolSize : "Размер пула коннектов"
    }

    const [pageName, setPagename] = useState(props.mode === 'create' ? "Создание источника данных" : "Редактирование источника данных");

    const [uploading, setUploading] = useState(false);
    const [errorField, setErrorField] = useState({});

    let loadFunc;
    let loadParams = [];
    

    if(props.mode === 'edit'){
        loadFunc = dataHub.datasourceController.get;
        loadParams = [props.datasourceId];
    }

    /* Data loading */
    function handleDataLoaded(loadedData){
        setData({
            ...data,
            datasourceName : loadedData.name,
            datasourceDescription : loadedData.description,
            datasourceTypeId : loadedData.type.id,
            datasourceUrl : loadedData.url,
            datasourceUserName : loadedData.userName,
            datasourcePoolSize : loadedData.poolSize,
        });
        setPagename("Редактирование источника данных: " + loadedData.name);
    }
    function handleTypesLoaded(data){
        setTypesData(data.map((v) => ({id: v.id, name: v.name})));
    }
    function handleDataLoadFailed(message){

    }

    /* Data editing */
    function handleChange(key, value){
        setData({
            ...data,
            [key]: value
        });
        setErrorField({
            ...errorField,
            [key] : false
        });
    }

    /* Save and cancel */
    function handleSave(){
        
        let errors = {};
        let errorExists = false;
        
        // Проверка корректности заполнения полей
        Object.entries(data)
            .filter( ([fieldName, fieldValue]) => 
                ( fieldName !== "datasourcePassword" && 
                    (fieldValue === null || 
                    (typeof fieldValue === "string" && fieldValue.trim() === "") || 
                    (fieldName === "datasourcePoolSize" && (fieldValue < 1 || fieldValue > 100)) ) ) 
                )
            .reverse()
            .forEach( ([fieldName, fieldValue]) => 
                {
                    errors[fieldName] = true;
                    enqueueSnackbar("Недопустимое значение в поле " + fieldLabels[fieldName], {variant : "error"});
                    errorExists = true;
                } );
        
        if(errorExists){
            setErrorField(errors);
        }
        else{
            if(props.mode === 'create'){
                dataHub.datasourceController.add(
                    props.folderId, 
                    data.datasourceName, 
                    data.datasourceDescription,
                    data.datasourceTypeId,
                    data.datasourceUrl,
                    data.datasourceUserName,
                    data.datasourcePassword,
                    data.datasourcePoolSize,
                    handleAddEditAnswer);
            }
            else{
                dataHub.datasourceController.edit(
                    props.folderId,
                    props.datasourceId,
                    data.datasourceName, 
                    data.datasourceDescription,
                    data.datasourceTypeId,
                    data.datasourceUrl,
                    data.datasourceUserName,
                    data.datasourcePassword,
                    data.datasourcePoolSize,
                    handleAddEditAnswer);
            }
            setUploading(true);
        }
    }
    function handleCancel(){
        props.onExit();
    }
    function handleAddEditAnswer(magrepResponse){
        setUploading(false);
        if(magrepResponse.ok){
            props.onExit();
        }
        else{
            let actionWord = props.mode === 'create' ? "создании" : "обновлении";
            enqueueSnackbar("Ошибка при " + actionWord + " объекта: " + magrepResponse.data, {variant : "error"});
        }
    }

    /* Check-connection */
    function checkConnection() {
        dataHub.datasourceController.checkConnect(data.datasourcePassword, data.datasourceUrl, data.datasourceUserName,  checkConnectionAnswer);
    
    }
    function checkConnectionAnswer(magrepResponse){

        function callback(){
            props.hideAlert();
        }
        
        const buttons = [{'text':'OK','onClick': callback}],
              text = <span>Не удалось подключиться! <br/> Ошибка: {magrepResponse.data}</span>;

        if(magrepResponse.ok) {
            props.showAlert(<Icon path={mdiCheckDecagram} size={1.5} color="#7CB342"/>, "Подключение успешно!", buttons, callback)
        } else {
            props.showAlert(<Icon path={mdiCloseOctagon} size={1.5} color="#FF0000"/>, text, buttons, callback)
        }
    }

    return(
        <DataLoader
            loadFunc = {loadFunc}
            loadParams = {loadParams}
            onDataLoaded = {handleDataLoaded}
            onDataLoadFailed = {handleDataLoadFailed}
        >
            <DataLoader
                loadFunc = {dataHub.datasourceController.getTypes}
                loadParams = {[]}
                onDataLoaded = {handleTypesLoaded}
                onDataLoadFailed = {handleDataLoadFailed}
            >
                {uploading ? <CircularProgress/> :

                <DesignerPage 
                    onSaveClick={handleSave}
                    onCancelClick={handleCancel}
                    name = {pageName}
                >
                    <DesignerTextField
                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = {fieldLabels.datasourceName}
                        value = {data.datasourceName}
                        onChange = {value => handleChange("datasourceName", value)}
                        displayBlock
                        fullWidth
                        error = {errorField.datasourceName}
                    />

                    <DesignerTextField
                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = {fieldLabels.datasourceDescription}
                        value = {data.datasourceDescription}
                        onChange = {value => handleChange("datasourceDescription", value)}
                        displayBlock
                        fullWidth
                        error = {errorField.datasourceDescription}
                    />

                    <DesignerSelectField
                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = {fieldLabels.datasourceTypeId}
                        data = {typesData}
                        value = {data.datasourceTypeId}
                        onChange = {value => handleChange("datasourceTypeId", value)}
                        displayBlock
                        fullWidth
                        error = {errorField.datasourceTypeId}
                    />

                    <DesignerTextField
                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = {fieldLabels.datasourceUrl}
                        value = {data.datasourceUrl}
                        onChange = {value => handleChange("datasourceUrl", value)}
                        displayBlock
                        fullWidth
                        error = {errorField.datasourceUrl}
                    />

                    <DesignerTextField
                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = {fieldLabels.datasourceUserName}
                        value = {data.datasourceUserName}
                        onChange = {value => handleChange("datasourceUserName", value)}
                        displayBlock
                        fullWidth
                        error = {errorField.datasourceUserName}
                    />

                    <DesignerTextField
                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = {fieldLabels.datasourcePassword}
                        value = {data.datasourcePassword}
                        onChange = {value => handleChange("datasourcePassword", value)}
                        type = "password"
                        displayBlock
                        fullWidth
                        error = {errorField.datasourcePassword}
                    />
                    <DesignerTextField
                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = {fieldLabels.datasourcePoolSize}
                        value = {data.datasourcePoolSize}
                        onChange = {value => handleChange("datasourcePoolSize", value)}
                        type = "number"
                        displayBlock
                        fullWidth
                        error = {errorField.datasourcePoolSize}
                    />
                    {data.datasourceUrl && data.datasourceUserName ?
                        <Button
                            className={classes.pageBtnConnection}
                            type="submit"
                            variant="contained"
                            size="small"
                            color="primary"
                            onClick={() => checkConnection()}
                        >
                            Проверить подключение
                        </Button>
                        :
                        <Button
                            className={classes.pageBtnConnection}
                            type="submit"
                            variant="contained"
                            size="small"
                            color="primary"
                            disabled
                        >
                            Проверить подключение
                        </Button>
                    }
                </DesignerPage>
                }
            </DataLoader>
            <Alerts/>
        </DataLoader>
    );
}

const mapDispatchToProps = {
    showAlert,
    hideAlert
}

export default connect(null, mapDispatchToProps)(DatasourceDesigner);

