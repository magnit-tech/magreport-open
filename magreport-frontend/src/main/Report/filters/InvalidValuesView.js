import React from 'react';

// components
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';


/**
 * 
 * @param {*} props.values - массив некорректных значений
 * @param {*} props.onClose - function(filterValue) - callback для передачи значения изменившегося параметра фильтра
 */
export default function InvalidValuesView({values,  onClose}){

    const rows = values.slice(Math.max(values.length - 10, 0))

    return (
        <Dialog
            open={true}
            onClose={onClose}
            aria-labelledby="dialog-title"
            aria-describedby="dialog-description"
        >
            <DialogContent>
                <TableContainer component={Paper}>
                    <Table size="small" aria-label="a dense table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Некорректные значения</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                        {rows.map(row => (
                            <TableRow key={row}>
                                <TableCell component="th" scope="row">{row}</TableCell>
                            </TableRow>
                        ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                <div>
                    <Typography variant="caption" display="block" gutterBottom>
                        Отображается не более 10 значений
                    </Typography>
                </div>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} color="primary">
                    Закрыть
                </Button>
            </DialogActions>
        </Dialog>
    )
}