import React, {useState} from 'react';
import {useSnackbar} from 'notistack';

// components
import {CircularProgress, Grid} from '@material-ui/core';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

// styles
import {LogsCSS} from "./LogsCSS";

// local
import logsHeader from 'images/logsHeader.jpg'

// dataHub
import dataHub from 'ajax/DataHub';
import Divider from "@material-ui/core/Divider";

function LogsMenuView() {
    const classes = LogsCSS()
    const {enqueueSnackbar} = useSnackbar();
    const [downloading, setDownloading] = useState(false)

    function handleDownLoadClick(type) {
        setDownloading(true)
        if (type === 'main') {
            dataHub.adminController.getMainLogs(handleDownloadResponse)
        } else {
            dataHub.adminController.getOlapLogs(handleDownloadResponse)
        }

    }

    function handleDownloadResponse(resp) {
        if (resp === '') {
            enqueueSnackbar("Не удалось получить файл с сервера", {variant: "error"});
        } else {
            resp.blob().then(blob => {
                const link = document.createElement('a');
                const url = window.URL.createObjectURL(new Blob([blob]));
                link.href = url;
                link.setAttribute('download', `logs.txt`);
                document.body.appendChild(link);
                link.click();
                link.parentNode.removeChild(link);
            })
                .catch(e => {
                    enqueueSnackbar("Не удалось сформировать файл логов. Возможно произошла сетевая ошибка, попробуйте еще раз.", {variant: "error"});
                })
        }
        setDownloading(false)
    }

    return (
        <Grid
            container
            direction="row"
            justifyContent="center"
            alignItems="center"
        >
            <Card className={classes.root}>
                <CardActionArea>
                    <CardMedia
                        component="img"
                        alt="Logo"
                        height="140"
                        image={logsHeader}
                        title="Logo"
                    />
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="h2">
                            Логи приложения
                        </Typography>
                        <Typography variant="body2" color="textSecondary" component="p">
                            Нажмите "Скачать" для получения основных логов приложения
                        </Typography>
                    </CardContent>
                </CardActionArea>
                <CardActions>
                    {downloading
                        ?
                        <CircularProgress
                            size={20}
                        />
                        :
                        <Button
                            size="small"
                            color="primary"
                            onClick={() => handleDownLoadClick('main')}
                        >
                            Скачать
                        </Button>
                    }
                </CardActions>
                <Divider/>
                <CardContent>

                    <Typography variant="body2" color="textSecondary" component="p">
                        Нажмите "Скачать" для получения логов Olap сервисов
                    </Typography>
                </CardContent>
                <CardActions>
                    {downloading
                        ?
                        <CircularProgress
                            size={20}
                        />
                        :
                        <Button
                            size="small"
                            color="primary"
                            onClick={() => handleDownLoadClick('olap')}
                        >
                            Скачать
                        </Button>
                    }
                </CardActions>
            </Card>

        </Grid>
    )
}

export default LogsMenuView;
