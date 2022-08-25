import React from 'react';

// components
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Button from '@material-ui/core/Button';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

// styles
import {HistorySettingsDialogCSS} from './ServerSettingsCSS'

export default function HistorySettingsDialog(props){
    const classes = HistorySettingsDialogCSS();

    const descriptionElementRef = React.useRef(null);

    React.useEffect(() => {
        if (props.open) {
            const { current: descriptionElement } = descriptionElementRef;
            if (descriptionElement !== null) {
                descriptionElement.focus();
            }
        }
    }, [props.open]);

    return (
        <Dialog
            open={props.open}
            onClose={props.onClose}
            scroll="paper"
            fullScreen
            aria-labelledby="scroll-dialog-title"
            aria-describedby="scroll-dialog-description"
        >
            <DialogTitle id="scroll-dialog-title">
                История изменений
            </DialogTitle>
            <DialogContent dividers={true}>
                <TableContainer component={Paper}>
                    <Table stickyHeader  className={classes.table} size="small" aria-label="a dense table">
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell align="right">Код</TableCell>
                                <TableCell align="right">Наименование</TableCell>
                                <TableCell align="right">Пользователь</TableCell>
                                <TableCell align="right">Дата изменения</TableCell>
                                <TableCell align="right">Значение до</TableCell>
                                <TableCell align="right">Значение после</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                        {props.data.map((row, index) => (
                            <TableRow key={`${row.code}_${index}`}>
                                <TableCell component="th" scope="row">{row.id}</TableCell>
                                <TableCell align="right">{row.code}</TableCell>
                                <TableCell align="right">{row.name}</TableCell>
                                <TableCell align="right">{row.user}</TableCell>
                                <TableCell align="right">{row.changeDate}</TableCell>
                                <TableCell align="right">{row.code === 'emailServerPassword' ? '******' : row.valueBefore}</TableCell>
                                <TableCell align="right">{row.code === 'emailServerPassword' ? '******' : row.valueAfter}</TableCell>
                            </TableRow>
                        ))}
                        </TableBody>
                    </Table>
                    </TableContainer>
            </DialogContent>
            <DialogActions>
                <Button onClick={props.onClose} color="primary">
                    Закрыть
                </Button>
            </DialogActions>
        </Dialog>
    )
}