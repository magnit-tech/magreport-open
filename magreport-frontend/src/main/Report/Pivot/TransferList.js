import React, {useState} from 'react';
import {useSnackbar} from "notistack";
import Grid from '@material-ui/core/Grid';
import List from '@material-ui/core/List';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import Checkbox from '@material-ui/core/Checkbox';
import Button from '@material-ui/core/Button';
import Divider from '@material-ui/core/Divider';
import Pagination from '@material-ui/lab/Pagination';
import TextField from '@material-ui/core/TextField';
import clsx from "clsx";
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
//local
import DataLoader from 'main/DataLoader/DataLoader';
import dataHub from 'ajax/DataHub';
import {PivotCSS} from './PivotCSS';

function not(a, b) {
    return a.filter((value) => b.indexOf(value) === -1);
}

function intersection(a, b) {
    return a.filter((value) => b.indexOf(value) !== -1);
}

function union(a, b) {
    return [...a, ...not(b, a)];
}

export default function TransferList(props) {
    const {enqueueSnackbar} = useSnackbar();
    const classes = PivotCSS();
   // const classes = useStyles();

    const [checked, setChecked] = useState([]);
    const [left, setLeft] = useState([]);
    const [right, setRight] = useState(props.filterValues);

    const rowsPerPage = [10, 100, 200, 500, 1000];

    //const [fieldValues, setFieldValues] = useState([]);
    const [countValues, setCountValues] = useState(1);

    const leftChecked = intersection(checked, left);
    const rightChecked = intersection(checked, right);

    function handleFieldValuesLoaded(data){
        setCountValues(data.countValues - props.filterValues.length);
        //setFieldValues(data.valueList);
        setLeft(not(data.valueList,  props.filterValues));
    }

    const handleToggle = (value) => () => {
        const currentIndex = checked.indexOf(value);
        const newChecked = [...checked];

        if (currentIndex === -1) {
            newChecked.push(value);
        } else {
            newChecked.splice(currentIndex, 1);
        }

        setChecked(newChecked);
    };

    const numberOfChecked = (items) => intersection(checked, items).length;

    const handleToggleAll = (items) => () => {
        if (numberOfChecked(items) === items.length) {
            setChecked(not(checked, items));
        } else {
            setChecked(union(checked, items));
        }
    };

    const handleCheckedRight = () => {
        let arr = right.concat(leftChecked)
        props.onChange(arr);
        setRight(arr);
        setLeft(not(left, leftChecked));
        setChecked(not(checked, leftChecked));
    };

    const handleCheckedLeft = () => {
        let arr = not(right, rightChecked);
         props.onChange(arr);
        setLeft(left.concat(rightChecked));
        setRight(arr);
        setChecked(not(checked, rightChecked));
    };

    const handlePageChange = (event, value) => {
        props.onPageChange(value);
    };

    const handleChangeRowsNumVal= (value) => {
        props.onRowsNumChange(value);
    }

    const customList = (title, items, pagination) => (
        <Card>
            <CardHeader
                className={classes.cardHeaderTrList}
                avatar={
                    <Checkbox
                        onClick={handleToggleAll(items)}
                        checked={numberOfChecked(items) === items.length && items.length !== 0}
                        indeterminate={numberOfChecked(items) !== items.length && numberOfChecked(items) !== 0}
                        disabled={items.length === 0}
                        inputProps={{ 'aria-label': 'all items selected' }}
                    />
                }
                title={title}
                subheader={`${numberOfChecked(items)}/${items.length} отмечено`}
            />
            <Divider />
            <List  className={clsx({[classes.listTrList]: pagination}, {[classes.listWOPaginationTrList]: !pagination})} 
                dense component="div" role="list"
            >
                {items.map((value) => {
                    const labelId = `transfer-list-all-item-${value}-label`;

                    return (
                        <ListItem key={value} role="listitem" button onClick={handleToggle(value)}>
                            <ListItemIcon>
                                <Checkbox
                                    checked={checked.indexOf(value) !== -1}
                                    tabIndex={-1}
                                    disableRipple
                                    inputProps={{ 'aria-labelledby': labelId }}
                                />
                            </ListItemIcon>
                            <ListItemText id={labelId} primary={value} />
                        </ListItem>
                    );
                })}
               
            </List>
            { pagination &&
            <div>
                <FormControl className={classes.formControlTrList} size="small">
                    <Select
                        defaultValue = {props.cntPerPage}
                      //  value ={curRowsNumVal}
                        children={ rowsPerPage.map((value, index) => <MenuItem key={index} value={value}>{value}</MenuItem>) }
                        onChange={event =>handleChangeRowsNumVal(event.target.value)}
                        inputProps={{
                            name: 'Строк на странице',
                            id: 'row-per-page',
                        }}
                    />
                </FormControl>
           
                <Pagination 
                    count={Math.ceil(((countValues  )/ props.cntPerPage) )} 
                    page={props.page} 
                    showFirstButton 
                    showLastButton
                    onChange={handlePageChange}
                />
                </div>
            }
        </Card>
    );

    return (
        <Grid
            container
            spacing={2}
            justifycontent="center"
            alignItems="center"
            className={classes.rootTrList}
        >
            <Grid item xs={12}>
                <TextField 
                    label="Поиск" 
                    onChange={(event) => props.onSearch(event.target.value)}> 
                </TextField>
            </Grid>
            <Grid container
                spacing={2}
                justifycontent="center"
                alignItems="center"
                style={{display: 'grid', gridTemplateColumns: '1fr 80px 1fr'}}
            >
                <Grid item>
                    <DataLoader
                        loadFunc = {dataHub.olapController.getFieldValues}
                        loadParams = {[props.params]}
                        showSpinner
                        onDataLoaded = {handleFieldValuesLoaded}
                        onDataLoadFailed = {message => enqueueSnackbar(`Не удалось получить значения поля: ${message}`, {variant : "error"})}
                    >
                        <div> 
                            {customList('Выбрать', left, true)}  
                        </div>
                    </DataLoader>  
                </Grid>
            
                <Grid item>
                    <Grid container direction="column" alignItems="center">
                        <Button
                            variant="outlined"
                            size="small"
                            className={classes.buttonTrList}
                            onClick={handleCheckedRight}
                            disabled={leftChecked.length === 0}
                            aria-label="move selected right"
                        >
                            &gt;
                        </Button>
                        <Button
                            variant="outlined"
                            size="small"
                            className={classes.buttonTrList}
                            onClick={handleCheckedLeft}
                            disabled={rightChecked.length === 0}
                            aria-label="move selected left"
                        >
                            &lt;
                        </Button>
                    </Grid>
                </Grid>
                <Grid item>{customList('Выбрано', right, false)}</Grid>
            </Grid>
        </Grid>
    );
}