import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
//import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Paper from '@material-ui/core/Paper';
import Draggable from 'react-draggable';
import {Select, MenuItem, FormControl, InputLabel } from "@material-ui/core";
import Grid from '@material-ui/core/Grid';
//import DesignerSelectField from '../../Main/Development/Designer/DesignerSelectField';
import AddIcon from '@material-ui/icons/Add';

function PaperComponent(props) {
    return (
      <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
        <Paper {...props} />
      </Draggable>
    );
  }

export default function ThresholdDialog(props){

 //   const [style, setStyle] = useState({});
  return (
        <Dialog
            open={props.open}
            //onClose={props.onClose}
            PaperComponent={PaperComponent}
            aria-labelledby="draggable-dialog-title"
        >
            <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
                Условное форматирование
            </DialogTitle>
            <DialogContent>
                <Grid container>
                    <Grid item xs={12}>
                        <FormControl
                            //   style = {{ minWidth: props.minWidth}}
                            //   className = {clsx(classes.field, {[classes.displayBlock] : props.displayBlock})}
                            variant ="outlined"
                          //  fullWidth
                            size="small"
                        >
                            <InputLabel variant ="outlined" id = "selectInputLabel">Поля</InputLabel>
                            <Select
                                labelId = "selectInputLabel"
                                // value = {props.value === 0 || props.value ? props.value : ''}
                                id = "idSelectInputLabel"
                                // onChange = {(event) => {props.onChange(event.target.value)}}
                              //  fullWidth
                                label="Поля"      
                            >
                                {props.allFields.map((v) => (<MenuItem key={v.fieldId} value={v.id}>{v.fieldName}</MenuItem>))}
                            </Select>  
                        </FormControl>
                        <Button
                                color="primary"
                                startIcon={<AddIcon />}
                              //  onClick={() => props.onAddChildGroup(childGroupInfo.id, reportId)}
                            >
                            </Button>
                    </Grid>
                    <Grid item xs={4}>

                    </Grid>
                    <Grid item xs={4}>

                    </Grid>
                    <Grid item xs={4}>
                        <div /*style={style}*/>

                        </div>
                        
                    </Grid>
                </Grid>
            </DialogContent>
        <DialogActions>
          
          <Button onClick={() =>props.onCancel(false)} color="primary">
            Сохранить
          </Button>
          <Button autoFocus onClick={() =>props.onCancel(false)} color="primary">
            Отменить
          </Button>
        </DialogActions>
      </Dialog>
  );
}