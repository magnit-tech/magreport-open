import React from 'react';
// хук вывода всплывающих сообщений
import { useSnackbar } from 'notistack';

// components
import IconButton from '@material-ui/core/IconButton';
import EditIcon from '@material-ui/icons/Edit';
import CheckIcon from '@material-ui/icons/Check';
import CloseIcon from '@material-ui/icons/Close';
import LibraryBooksIcon from '@material-ui/icons/LibraryBooks';
import CircularProgress from '@material-ui/core/CircularProgress';
import Button from '@material-ui/core/Button';
import Tooltip from '@material-ui/core/Tooltip';
import InputAdornment from '@material-ui/core/InputAdornment';
//import HistoryIcon from '@material-ui/icons/History';

// local components
import DesignerTextField from 'main/Main/Development/Designer/DesignerTextField'
import HistorySettingsDialog from './HistorySettingsDialog';
import DesignerPage from "main/Main/Development/Designer/DesignerPage";
import PageTabs from 'main/PageTabs/PageTabs';
import StyleConsts from '../../../../StyleConsts';

// styles
import {ServerSettingsCSS} from './ServerSettingsCSS'

export default function SettingsMenuViewer(props){

    const classes = ServerSettingsCSS();

    const { enqueueSnackbar } = useSnackbar();

    let tabs = [];

    for (const [foldIndex, f] of Object.entries(props.folders)) {
        tabs.push({ 
            tablabel: f.name,
            tabcontent: //uploading ? <CircularProgress /> :
                <DesignerPage
                 //   onCancelClick = {() => props.onExit()}
                  //  cancelName="Закрыть"
                >
                    <div>
                        <Button 
                            variant="outlined" 
                            color="primary"
                            style={{ margin:'10px 0px', minWidth: '240px'}}
                            disabled={props.getFullJournal || !!props.history}
                            onClick={() => props.actionGetFullJournal(enqueueSnackbar)}
                            startIcon={<LibraryBooksIcon/>}
                        >
                            История изменения{props.getFullJournal && <span style={{paddingLeft:'10px'}}><CircularProgress size={15}/></span>}
                        </Button> 
                    </div>
                    {f.parameters.map( (p, paramIndex) => {
                        let saveDisabled = Boolean(!p.hasOwnProperty('tempValue') || p.tempValue === p.value /*||p.tempValue.trim().length < 1*/ || p.saving );
                        return(    
                            <div key={p.id} className={classes.details}>
                                <div className={classes.column}>
                                    <DesignerTextField 
                                        minWidth = {StyleConsts.designerTextFieldMinWidth}
                                        label = {p.name}
                                        //InputLabelProps={{style: {whiteSpace: 'noWrap'}}}
                                        value = {p.hasOwnProperty('tempValue') ? p.tempValue : p.value}
                                        onChange = {value => props.actionSettingValueChanged( foldIndex, f.code, /*f.id*/ paramIndex, p.id, value)}
                                        displayBlock
                                        fullWidth
                                        multiline
                                        disabled={!p.disabled}
                                        type = {p.encoded ? "password" : "text"}
                                        error = {false}
                                        inputProps={{ endAdornment: 
                                            <InputAdornment position="start">
                                                <div className={classes.columnButtons}>
                                                    <div>
                                                        {p.disabled
                                                         ?
                                                            <div>
                                                                <Tooltip title="Сохранить">
                                                                    {/* span не убирать, иначе сыпятся Warnings если disabled=true */}
                                                                    <span> 
                                                                        <IconButton 
                                                                            aria-label="save" 
                                                                            disabled={saveDisabled}
                                                                            onClick={() => props.actionSetSetting(foldIndex, f.code, paramIndex, p.id, p.tempValue, enqueueSnackbar)}
                                                                        >
                                                                            <CheckIcon style={saveDisabled ? {} : {color: 'Lime'}} />
                                                                        </IconButton>
                                                                    </span>
                                                                </Tooltip>
                                                                <Tooltip title="Отменить">
                                                                    {/* span не убирать, иначе сыпятся Warnings если disabled=true */}
                                                                    <span>
                                                                        <IconButton 
                                                                            aria-label="cancel" 
                                                                            disabled={Boolean(p.saving) }
                                                                            onClick={() => props.actionSettingDisableChanged(foldIndex, f.code, paramIndex, p.id, false)}
                                                                        >
                                                                            <CloseIcon color="secondary" />
                                                                        </IconButton>
                                                                    </span>
                                                                </Tooltip>
                                                            </div>
                                                        :
                                                                <Tooltip title="Редактировать">
                                                                    <IconButton aria-label="edit" onClick={() => props.actionSettingDisableChanged(foldIndex, f.code, paramIndex, p.id, true)}>
                                                                        <EditIcon /*color="primary"*/ />
                                                                    </IconButton>
                                                                </Tooltip>
                                                        }
                                                    </div>
                                                </div>
                                            
                                            </InputAdornment>
                                        }}
                                    />
                                    <div className={classes.columnButtons}>
                                        {(p.saving || p.getJournal)
                                        ?    
                                            <CircularProgress size={24} className={classes.settingsCircular}/>
                                        :
                                            <Tooltip title="Показать историю изменений">
                                            {/* span не убирать, иначе сыпятся Warnings если disabled=true */}
                                                <span>
                                                    <IconButton 
                                                        aria-label="history"
                                                        disabled={p.getJournal}
                                                        onClick={() => props.actionGetJournal(foldIndex, f.code, paramIndex, p.id, enqueueSnackbar)}
                                                    >
                                                        <LibraryBooksIcon color="primary" />
                                                    </IconButton>
                                                </span>
                                            </Tooltip>
                                        }
                                        
                                    </div>
                                </div>
                            </div>
                        )
                    })}
                    { 
                        props.history && 
                            <HistorySettingsDialog 
                                open={!!props.history}
                                data={props.history}
                                onClose={props.actionCloseHistory}
                            /> 
                    }
        
                </DesignerPage>
        })
    }

    return(
        <PageTabs
                tabsdata={tabs}
                pageName="Настройки"               
        />
    )
}

