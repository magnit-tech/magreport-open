import React from 'react';

// material-ui

import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';


const useStyles = makeStyles(theme => ({
    root: {
        minHeight: '290px',

    },
    filterTitle: {
        padding: theme.spacing(0,2),
        backgroundColor: theme.palette.primary.light,
        overflow: 'hidden',
        height: 50
    },
    paramText: {
        color: theme.palette.primary.contrastText,
        fontWeight: theme.typography.fontWeightMedium,
        overflow: 'hidden',
        whiteSpace: 'nowrap',
        marginLeft: '-16px',
        fontSize: 'large'
    },
    indent: {
        margin: 'auto 16px 16px'
    },
})); 

export default function FolderDescriptionWindow(props){   
    let isOpen = props.isOpen; 
    let folderId = null;
    let name = ""
    let desc = ""

    if  (props.data){
        folderId = props.data.id;
        name = props.data.name;
        desc = props.data.description;
    }

    const handleOnChangeName = e => {
        name = e.target.value;
    }

    const handleOnChangeDesc = e => {
        desc = e.target.value;
    }
    
    const classes = useStyles();  
    
    function handleClose(){
        props.onClose();
    }

    function handleSaveChanges(){
        if (name.length < 1){
            return;
        }
        if (desc.length < 1){
            return;
        }
        
        props.onSave(folderId, name, desc);
    }

    return(
        <div>
            <Dialog open={isOpen} onClose={handleClose} aria-labelledby="form-dialog-title" PaperProps={{ classes: {root: classes.root }}}>
            <Toolbar position="fixed" elevation={5} className={classes.filterTitle}  variant="dense">
                <DialogTitle id="formChange"> 
                  <Typography className={classes.paramText}>{ folderId === null ? "Добавление каталога" : "Изменение каталога"}</Typography>
                </DialogTitle>
            </Toolbar>
                <DialogContent>

                <TextField
                    autoFocus
                    margin="normal"
                    multiline
                    id="name"
                    label="Название каталога"
                    type="text"
                    defaultValue={name}
                    onChange={handleOnChangeName}
                    fullWidth
                    required
                    variant="outlined"
                />
                <TextField
                    margin="normal"
                    multiline
                    id="description"
                    label="Описание каталога"
                    type="text"
                    defaultValue={desc}
                    onChange={handleOnChangeDesc}
                    fullWidth
                    required
                    variant="outlined"
                />
                </DialogContent>
                <DialogActions className={classes.indent}>
                <Button 
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small"
                    onClick={handleSaveChanges}>Сохранить
                </Button>
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small"
                    onClick={handleClose}>Отменить
                </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}
