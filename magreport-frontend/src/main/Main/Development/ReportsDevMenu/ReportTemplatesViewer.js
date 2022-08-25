import React, {useRef, useState} from 'react'
import {connect} from 'react-redux';
import {useSnackbar} from 'notistack';

// components
import DescriptionIcon from '@material-ui/icons/Description';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import StarIcon from '@material-ui/icons/Star';
import GetApp from "@material-ui/icons/GetApp";
import StarOutlineIcon from '@material-ui/icons/StarOutline';

// local
import {ViewerCSS} from "../Viewer/ViewerCSS";
import IconButton from "@material-ui/core/IconButton";
import {CircularProgress, Tooltip} from "@material-ui/core";
import dataHub from "ajax/DataHub";

/**
 * Компонент просмотра шаблонов отчета
 * @param {Object} props - свойства компонента
 * @param {Number} props.reportId - id отчета
 * @param {Array} props.reportTemplates - шаблоны отчета
 * @return {JSX.Element}
 * @constructor
 */
function ReportTemplatesViewer(props) {

    const {enqueueSnackbar} = useSnackbar();

    const viewerClasses = ViewerCSS();

    const [downloading, setDownloading] = useState(false)

    const downloadingFileNameRef = useRef('');

    function handleDownLoadClick(id, name) {
        downloadingFileNameRef.current = name;
        dataHub.excelTemplateController.getFile(id, handleDownloadResponse);
        setDownloading(true);
    }

    function handleDownloadResponse(response) {
        if (!response) {
            enqueueSnackbar("Не удалось получить файл шаблона с сервера", {variant: "error"});
        } else {
            response.blob()
                .then(blob => {
                    const link = document.createElement('a');
                    link.href = window.URL.createObjectURL(new Blob([blob]));
                    link.setAttribute('download', `${downloadingFileNameRef.current}.xlsm`);
                    link.click();
                })
                .catch(e => {
                    enqueueSnackbar(`Не удалось скачать файл шаблона: ${e}`, {variant: "error"});
                })
                .finally(() => setDownloading(false))
        }

    }

    return (
        <div>
            {downloading && <CircularProgress/>}
            <List>
                {
                    props.reportTemplates.map(template =>
                        <ListItem
                            key={template.excelTemplateId}
                        >
                            <ListItemIcon>
                                <DescriptionIcon/>
                            </ListItemIcon>
                            <ListItemText
                                primary={template.name}
                                secondary={template.description}
                            />
                            <div className={viewerClasses.icon}>
                                {template.default ? <StarIcon color="secondary"/> : <StarOutlineIcon/>}
                            </div>
                            <Tooltip title="Скачать шаблон">
                                <IconButton
                                   // disabled={template.default}
                                    aria-label="download"
                                    onClick={() => handleDownLoadClick(template.excelTemplateId, template.name)}
                                >
                                    <GetApp/>
                                </IconButton>
                            </Tooltip>
                        </ListItem>
                    )
                }
            </List>
        </div>
    )
}


const mapStateToProps = state => {
    return {
        reportTemplates: state.reportTemplates.data,
    }
}

export default connect(mapStateToProps)(ReportTemplatesViewer)
