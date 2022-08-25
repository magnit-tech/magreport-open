import React, {useState} from "react";
import { useSnackbar } from 'notistack';

// local components
import DesignerSelectField from "main/Main/Development/Designer/DesignerSelectField";

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';

export default function ScheduledTasksExcelTemplatesSelect(props) {
    const { enqueueSnackbar } = useSnackbar();

    const [data, setData] = useState([]);
    const [excelTemplateId, setExcelTemplateId] = useState(null);

    let loadFunc = dataHub.excelTemplateController.get;

    function handleExcelTemplatesLoaded(dataLoaded){
        setData(dataLoaded.map((v) => ({id: v.excelTemplateId, name: v.name, default: v.default})));
        setExcelTemplateId(props.value ? props.value :
            dataLoaded.length !== 0 ? dataLoaded.filter(item => item.default)[0].excelTemplateId : 
            null);
        
        if (!Boolean(props.value)){
            props.onChange("excelTemplateId", 
                dataLoaded.length !== 0 ? dataLoaded.filter(item => item.default)[0].excelTemplateId : null)
        }
    }
    
    function handleChange(data){
        props.onChange("excelTemplateId", data)
        setExcelTemplateId(data);
    }

    return (
        <DataLoader
            loadFunc = {loadFunc}
            loadParams = {[props.reportId]}
            onDataLoaded = {handleExcelTemplatesLoaded}
            onDataLoadFailed = {message => enqueueSnackbar(`Не удалось получить информацию о шаблонах отчёта: ${message}`, {variant : "error"})}
            disabledScroll = {true}
        >     
            <DesignerSelectField
                //minWidth = {StyleConsts.designerTextFieldMinWidth}
                label = {props.label}
                value = {excelTemplateId}
                data = {data}
                onChange = {data => handleChange(data)}
                displayBlock
                fullWidth
                error = {props.error}
            /> 
        </DataLoader>
    )
}