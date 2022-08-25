import React, {useState} from "react";
import {useSnackbar} from "notistack";

// dataHub
import dataHub from "ajax/DataHub";

// components
import {CircularProgress} from "@material-ui/core";

// local components
import DataLoader from "main/DataLoader/DataLoader";
import PageTabs from "main/PageTabs/PageTabs";
import DesignerPage from "main/Main/Development/Designer/DesignerPage";
import DesignerTextField from "main/Main/Development/Designer/DesignerTextField";
import ScheduleParameters from "./ScheduleParameters";


/**
 * @callback onExit
 */

/**
 * Компонент создания и редактирования расписаний
 * @param {Object} props - параметры компонента
 * @param {String} props.mode - "create" - создание; "edit" - редактирование
 * @param {Number} props.scheduleId - идентификатор расписания
 * @param {onExit} props.onExit - callback, вызываемый при закрытии формы
 * @return {JSX.Element}
 * @constructor
 */
export default function ScheduleDesigner(props) {

    const {enqueueSnackbar} = useSnackbar();

    const [uploading, setUploading] = useState(false);

    const [id, setId] = useState(props.scheduleId);
    const [name, setName] = useState();
    const [description, setDescription] = useState();
    const [scheduleType, setScheduleType] = useState();
    const [parameters, setParameters] = useState({});
    //const [tasks, setTasks] = useState([]);

    const [nameError, setNameError] = useState(true);
    const [descriptionError, setDescriptionError] = useState(true);
    const [parametersErrors, setParametersErrors] = useState({scheduleTypeId: true})

    const pageName = props.mode === "create" ? "Создание расписания" : "Редактирование расписания";


    let loadFunc;
    let loadParams = [];

    if (props.mode === "edit") {
        loadFunc = dataHub.scheduleController.get;
        loadParams = [id];
    }

    function hasErrors() {
        let result = nameError || descriptionError;
        Object.entries(parametersErrors).forEach(([_, value]) => result = result || value);
        return result;
    }

    function handleChangeName(newName) {
        setName(newName);
        setNameError(!Boolean(newName));
    }

    function handleChangeDescription(newDescription) {
        setDescription(newDescription);
        setDescriptionError(!Boolean(newDescription));
    }

   /* function handleChangeTasks(newTasks) {
        setTasks(newTasks || []);
    }*/

    function handleChangeParameters(newParameters = {}, newErrors = {scheduleTypeId: true}) {
        setParameters(newParameters);
        setParametersErrors(newErrors);
    }

    function handleDataLoaded(loadedData) {
        setId(loadedData.id);
        setScheduleType(loadedData.type);
        handleChangeName(loadedData.name);
        handleChangeDescription(loadedData.description);
        //handleChangeTasks(loadedData.tasks);
        handleChangeParameters(loadedData);
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При получении данных возникла ошибка: ${message}`,
            {variant: "error"});
    }

    function handleSave() {
        if(hasErrors()) {
            enqueueSnackbar(`Форма содержит ошибки`, {variant: "error"});
            return;
        }
        if (props.mode === "create") {
            dataHub.scheduleController.add(
                name,
                description,
                parameters,
                magResponse => handleAddedEdited(magResponse)
            );
            setUploading(true);
        } else {
            dataHub.scheduleController.edit(
                id,
                name,
                description,
                parameters,
                magRepResponse => handleAddedEdited(magRepResponse)
            );
            setUploading(true);
        }
    }

    function handleAddedEdited(magRepResponse) {
        
        if (magRepResponse.ok) {
            props.onExit();
            enqueueSnackbar("Расписание успешно сохранено", {variant : "success"});
        } else {
            setUploading(false);
            const actionWord = props.mode === "create" ? "создании" : "обновлении";
            enqueueSnackbar(`При ${actionWord} возникла ошибка: ${magRepResponse.data}`,
                {variant: "error"});
        }
    }

    function handleCancel() {
        props.onExit();
    }

    // building component
    const tabs = [];

    // general
    tabs.push({
        tablabel: "Общие",
        tabcontent: uploading ? <CircularProgress/> :
            <DesignerPage
                onSaveClick={handleSave}
                onCancelClick={handleCancel}
            >
                <DesignerTextField
                    label="Название"
                    value={name}
                    onChange={handleChangeName}
                    displayBlock
                    fullWidth
                    error={nameError}
                />
                <DesignerTextField
                    label="Описание"
                    value={description}
                    onChange={handleChangeDescription}
                    displayBlock
                    fullWidth
                    error={descriptionError}
                />
                <ScheduleParameters
                    scheduleType={scheduleType}
                    data={parameters}
                    errors={parametersErrors}
                    onChange={handleChangeParameters}
                />
            </DesignerPage>
    })

    return <DataLoader
        loadFunc={loadFunc}
        loadParams={loadParams}
        onDataLoaded={handleDataLoaded}
        onDataLoadFailed={handleDataLoadFailed}
    >
        <PageTabs
            tabsdata={tabs}
            pageName={pageName}
        />
    </DataLoader>
}
