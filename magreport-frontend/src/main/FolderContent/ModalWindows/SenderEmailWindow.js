
import React from 'react';
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Button from "@material-ui/core/Button";
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Draggable from 'react-draggable';

// styles
import { ChooserDestinationWindowCSS } from './ModalWindowsCSS';

/**
 * Компонент предварительного показа письма перед отправкой
 * @param {Object} props - параметры компонента
 * @param {Boolean} props.open - признак открытого окна
 * @param {Array} props.to - кому
 * @param {Array} props.cc - копия
 * @param {Array} props.bcc - скрытая копия
 * @param {String} props.subject - тема
 * @param {String} props.body - тело письма
 * @param {send} props.send - callback, вызываемый при нажатии кнопки Отправить
 * @param {showMailParam} props.showMailParam - callback, вызываемый при нажатии кнопки Отмена
 * @return {JSX.Element}
 * @constructor
 */

function PaperComponent(props) {
    return (
      <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
        <Paper {...props} />
      </Draggable>
    );
  }

function SenderEmailWindow(props) {
    
    const classes = ChooserDestinationWindowCSS();

    return (
        <Dialog
            aria-labelledby="draggable-dialog-title"
            PaperComponent={PaperComponent}
            open={props.open}
        >
            <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
                <table>
                    <tbody>
                        <tr>
                            <td colSpan= "6">
                                <Typography variant="subtitle2">Количество получателей: </Typography>
                            </td>
                        </tr>
                        <tr>
                            <td colSpan="5"> <Typography variant="body2">Кому:</Typography></td>
                            <td colSpan ="1"><Typography variant="body2">{props.to.length}</Typography></td>
                        </tr>
                        <tr>
                            <td colSpan ="5"> <Typography variant="body2">Копия:</Typography></td>
                            <td colSpan ="1"><Typography variant="body2">{props.cc.length}</Typography></td>
                        </tr>
                        <tr>
                            <td colSpan ="5"> <Typography variant="body2">Скрытая копия:</Typography></td>
                            <td colSpan ="1"><Typography variant="body2">{props.bcc.length}</Typography></td>
                        </tr>
                    </tbody>
                </table>
            </DialogTitle>
            <DialogContent dividers>
                <table>
                    <tbody>
                        <tr>
                            <td colSpan ="6"> <Typography variant="subtitle2">Тема письма:</Typography></td>
                        </tr>
                        <tr>
                            <td colSpan="6"><Typography variant="body2">{props.subject}</Typography></td>
                        </tr>
                        <tr>
                            <td colSpan ="6"> <Typography variant="subtitle2">Тело письма:</Typography></td>
                        </tr>
                        <tr>
                            <td colSpan ="6">
                                <div dangerouslySetInnerHTML={{__html: props.body}}/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </DialogContent>
            <DialogActions className={classes.indent}>
                <Button
                    type="submit"
                    variant="contained"
                    size="small"
                    color="primary"
                    disabled={props.body.length === 0 || props.subject.length === 0 || (props.to.length === 0 && props.cc.length === 0 && props.bcc.length === 0) }
                    onClick={props.send}>Отправить
                </Button>
                <Button
                    type="submit"
                    variant="contained"
                    size="small"
                    color="primary"
                    onClick={props.showMailParam}>Отменить
                </Button>
            </DialogActions>                 
        </Dialog>
    )
}

export default SenderEmailWindow