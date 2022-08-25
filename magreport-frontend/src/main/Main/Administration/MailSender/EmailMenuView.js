import React, {useState} from 'react';
import TextField from "@material-ui/core/TextField";
import DesignerPage from "../../Development/Designer/DesignerPage";
import StyleTextPanel from "../ServerMailTemplate/StyleTextPanel";
import ChooserDestinationWindow from "../../../FolderContent/ModalWindows/ChooserDestinationWindow";
import SenderEmailWindow from "../../../FolderContent/ModalWindows/SenderEmailWindow";
import dataHub from "../../../../ajax/DataHub";
import {useSnackbar} from "notistack";
import {DesignerCSS} from "../../Development/Designer/DesignerCSS";
import IconButton from '@material-ui/core/IconButton';
import ClearIcon from '@material-ui/icons/Clear';
import Tooltip from '@material-ui/core/Tooltip';
import InputAdornment from '@material-ui/core/InputAdornment';

function EmailMenuView(_props) {

    const classes = DesignerCSS();

    const [chooserDestination, setChooserDestination] = useState(false);
    const [showResult, setShowResult] = useState(false)

    const [TO, setTO] = useState([]);
    const [CC, setCC] = useState([]);
    const [BCC, setBCC] = useState([]);

    const [selectedDestinations, setSelectedDestinations] = useState([]);
    const [selectedTypeDestinations, setSelectedTypeDestinations] = useState([]);

    const [styleTag, setStyleTag] = useState("");

    const [subject, setSubject] = useState("");
    const [body, setBody] = useState("");

    const {enqueueSnackbar} = useSnackbar();

    function changeFlow() {
        setChooserDestination(!chooserDestination)
    }

    function showMailParam() {
        setShowResult(!showResult)
    }

    function updateStyleTag(tag) {
        setStyleTag(tag)
    }

    function editClick(type) {

        setSelectedTypeDestinations(type);

        switch (type) {
            case  "TO" :
                setSelectedDestinations(TO)
                break
            case "CC" :
                setSelectedDestinations(CC)
                break
            case "BCC" :
                setSelectedDestinations(BCC)
                break
            default :
                break
        }

        changeFlow();
    }

    function getDestinations(items) {

        let result = ""
        items.forEach(item => {
            result = result.concat(item.email + "; ")
        })
        return result
    }

    function okClickChooser(selectDestinations) {

        switch (selectedTypeDestinations) {
            case  "TO" :
                setTO(selectDestinations)
                break
            case "CC" :
                setCC(selectDestinations)
                break
            case "BCC" :
                setBCC(selectDestinations)
                break
            default    :
                break
        }

        changeFlow();
    }

    function insertTag(target) {
        if (target.selectionStart !== target.selectionEnd) {
            if (styleTag !== "" && styleTag !== null  ) {
                let message = target.value.slice(0, target.selectionStart) +
                    "<" + styleTag + ">" +
                    target.value.slice(target.selectionStart, target.selectionEnd) +
                    "</" + styleTag + ">" +
                    target.value.slice(target.selectionEnd)
                setBody(message)
            }
        }
    }

    function send() {
        dataHub.emailController.send(subject, body, TO, CC, BCC, saveResponse)
    }

    function updateBody() {
        return body.replaceAll('\n','<br/>')
    }

    function saveResponse(response) {

        if (response.ok) {
            setSubject("")
            setBody("")
            setTO([])
            setCC([])
            setBCC([])
            enqueueSnackbar("Письмо успешно отправлено",  {variant: "success"})
        } else {
            enqueueSnackbar("При отправке письма произошла ошибка",  {variant: "error"})
        }
        showMailParam()
    }

    function handleClearClick(key, event) {
        setSelectedDestinations([])
        switch (key) {
            case "TO":
                event.stopPropagation();
                setTO([]);
                break
            case "CC":
                event.stopPropagation();
                setCC([]);
                break
            case "BCC":
                event.stopPropagation();
                setBCC([]);
                break
            case "title":
                setSubject("");
                break
            case "body":
                setBody("");
                break
            default:
                break
        }

    }

    return <div style={{display: 'flex', flex: 1}}>
        <DesignerPage
            name="Рассылка писем"
            saveName = "Отправить"
            onSaveClick={showMailParam}
        >
            <TextField
                className={classes.field}
                fullWidth
                multiline
                minRows={1}
                maxRows={3}
                variant={"outlined"}
                label={"Кому"}
                onClick={() => {
                    editClick("TO")
                }}
                value={getDestinations(TO)}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <Tooltip title="Очистить" placement="top">
                                <IconButton
                                    aria-label="clear"
                                    color='primary'
                                    size='small'
                                    onClick={(event)=>handleClearClick("TO", event)}
                                >
                                    <ClearIcon fontSize='small' />
                                </IconButton>
                            </Tooltip>
                        </InputAdornment>
                    )
                }}
            />

            <TextField
                className={classes.field}
                fullWidth
                multiline
                minRows={1}
                maxRows={3}
                variant={"outlined"}
                label={"Копия"}
                onClick={() => {
                    editClick("CC")
                }}
                value={getDestinations(CC)}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <Tooltip title="Очистить" placement="top">
                                <IconButton
                                    aria-label="clear"
                                    color='primary'
                                    size='small'
                                    onClick={(event)=>handleClearClick("CC", event)}
                                >
                                    <ClearIcon fontSize='small' />
                                </IconButton>
                            </Tooltip>
                        </InputAdornment>
                    )
                }}
            />

            <TextField
                className={classes.field}
                fullWidth
                multiline
                minRows={1}
                maxRows={3}
                variant={"outlined"}
                label={"Скрытая копия"}
                onClick={() => {
                    editClick("BCC")
                }}
                value={getDestinations(BCC)}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <Tooltip title="Очистить" placement="top">
                                <IconButton
                                    aria-label="clear"
                                    color='primary'
                                    size='small'
                                    onClick={(event)=>handleClearClick("BCC", event)}
                                >
                                    <ClearIcon fontSize='small' />
                                </IconButton>
                            </Tooltip>
                        </InputAdornment>
                    )
                }}
            />

            <TextField
                className={classes.field}
                value={subject}
                label={"Тема письма"}
                variant={"outlined"}
                onChange={event => setSubject(event.target.value)}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end" >
                            <Tooltip title="Очистить" placement="top">
                                <IconButton
                                    aria-label="clear"
                                    color='primary'
                                    size='small'
                                    onClick={()=>handleClearClick("title")}
                                >
                                    <ClearIcon fontSize='small' />
                                </IconButton>
                            </Tooltip>
                        </InputAdornment>
                    )
                }}
            />
            <StyleTextPanel onClick={updateStyleTag}/>
            <TextField
                className={classes.textField}
                label={"Текст письма"}
                value={body}
                variant={"outlined"}
                onChange={event => setBody(event.target.value)}
                onClick={click => insertTag(click.target)}
                multiline
                minRows={2}
                maxRows={15}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end" className={classes.topInputAdornment}>
                            <Tooltip title="Очистить" placement="top">
                                <IconButton
                                    aria-label="clear"
                                    color='primary'
                                    size='small'
                                    onClick={()=>handleClearClick("body")}
                                >
                                    <ClearIcon fontSize='small' />
                                </IconButton>
                            </Tooltip>
                        </InputAdornment>
                    )
                }}
            />

            <ChooserDestinationWindow
                open = {chooserDestination}
                destinations={selectedDestinations}
                onExitClick={changeFlow}
                onOkClick={value => {
                    okClickChooser(value)
                }}
            />

            <SenderEmailWindow
                open ={showResult}
                to ={TO}
                cc={CC}
                bcc={BCC}
                subject={subject}
                body={ updateBody()}
                send={send}
                showMailParam={showMailParam}
            />
        </DesignerPage>
    </div>

}

export default EmailMenuView;
