import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import Toolbar from '@material-ui/core/Toolbar';
import DialogTitle from '@material-ui/core/DialogTitle';
import Typography from '@material-ui/core/Typography';
//styles
import { RoleWindowCSS } from './ModalWindowsCSS'

function RoleModalWindow(props){

    const isOpen=props.isOpen;
    let roleId=null;
    let nameRole="";
    let descRole="";

    if (props.data.id){
        roleId=props.data.id;
        nameRole=props.data.name;
        descRole=props.data.description;
    }    

    const handleOnChangeNameRole = e => {
        nameRole = e.target.value;
    }

    const handleOnChangeDescRole = e => {
        descRole = e.target.value;
    }
    
    const classes = RoleWindowCSS();  

    function handleClose(){
        props.onClose();
    }

    function handleSaveRole(){
        if (nameRole.length < 1){
            return;
        }
        if (descRole.length < 1){
            return;
        }
        
        props.onSave(roleId, nameRole.toUpperCase(), descRole);
    }

    return(
        <div>
            <Dialog open={isOpen} onClose={handleClose} aria-labelledby="form-dialog-title" PaperProps={{ classes: {root: classes.root }}}>
            <Toolbar position="fixed" elevation={5} className={classes.filterTitle}  variant="dense">
                <DialogTitle id="form-add-role">
                   <Typography className={classes.paramText}>{ roleId ? "Добавление роли" : "Изменение роли" } </Typography>
                </DialogTitle>
            </Toolbar>
                <DialogContent style={{display: 'flex'}}>
                    
                <TextField
                    autoFocus
                    margin="normal"
                    id="name"
                    label="Название роли"
                    type="text"
                    defaultValue={nameRole}
                    onChange={handleOnChangeNameRole}
                    fullWidth
                    required
                    variant="outlined"
                />
                <TextField
                    margin="normal"
                    id="description"
                    label="Описание роли"
                    type="text"
                    defaultValue={descRole}
                    onChange={handleOnChangeDescRole}
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
                    onClick={handleSaveRole}>Сохранить
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

export default RoleModalWindow;