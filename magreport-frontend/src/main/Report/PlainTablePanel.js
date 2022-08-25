import React, {useState, Fragment, useRef } from 'react';
import Icon from '@mdi/react'
import Measure from 'react-measure';
import { Scrollbars } from 'react-custom-scrollbars'
import { useSnackbar } from 'notistack';
import PropTypes from 'prop-types';
import { useTheme } from '@material-ui/core/styles';
import { TableRow, TableHead, TableBody, TableCell, Table, InputBase } from '@material-ui/core';
import TableContainer from '@material-ui/core/TableContainer' 
import TablePagination from '@material-ui/core/TablePagination';
import Paper from '@material-ui/core/Paper';
import Tooltip from '@material-ui/core/Tooltip/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import FirstPageIcon from '@material-ui/icons/FirstPage';
import KeyboardArrowLeft from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRight from '@material-ui/icons/KeyboardArrowRight';
import LastPageIcon from '@material-ui/icons/LastPage';
import AssignmentIcon from '@material-ui/icons/Assignment';
import PlayArrowIcon from '@material-ui/icons/PlayArrow';
import Typography from '@material-ui/core/Typography';
import CircularProgress from '@material-ui/core/CircularProgress';
import CloseIcon from '@material-ui/icons/Close';
import { mdiTablePivot } from '@mdi/js';

// dataHub
import dataHub from 'ajax/DataHub';

// components
import DataLoader from 'main/DataLoader/DataLoader';
import ReportTemplatesList from './ReportTemplatesList'

// styles
import { ReportDataCSS } from "./ReportCSS";


/**
 * @param {*} props.jobId - id задания
 * @param {*} props.excelTemplates - массив шаблонов Excel
 * @param {*} props.onViewTypeChange - function() - callback смена вида с таблицы на сводную
 * @param {*} props.onRestartReportClick - function() - callback перезапуска отчёта
 */
export default function PlainTablePanel(props){

    const { enqueueSnackbar, closeSnackbar } = useSnackbar();
    const ScrollbarsRef = useRef(null);

    const classes = ReportDataCSS();

    const [headers, setHeaders] = useState([]);
    const [pageRows, setPageRows] = useState([]);

    const defaultRowsPerPage = 50;
    const [pageNumber, setPageNumber] = useState(1); // 1-based 
    const [rowsPerPage, setRowsPerPage] = useState(defaultRowsPerPage);
    const [rowCount, setRowCount] = useState(0);
    const [exportInProcess, setExportInProcess] = React.useState(false);

    const excelTemplates = props.excelTemplates

    const canExecute = props.canExecute

    const action = key => (
        <Fragment>
            <IconButton onClick={() => { closeSnackbar(key) }}>
                <CloseIcon/>
            </IconButton>
        </Fragment>
    );
    
    function handleChangePage(event, newPage){
        setPageNumber(newPage + 1);
    }
    
    function handleChangeRowsPerPage(event){
        let newRowsPerPage = parseInt(event.target.value);
        let rowsBeforeCurrentPage = (pageNumber - 1) * rowsPerPage;
        let newPageNumber = Math.floor(rowsBeforeCurrentPage / newRowsPerPage) + 1;
        setRowsPerPage(newRowsPerPage);
        setPageNumber(newPageNumber);
    };

    function handleExcelExport(event, id){
        let excelTemplateId = id
        if (id === null || id === undefined) excelTemplateId = excelTemplates.filter(i => i.default)[0].excelTemplateId

        // setErrorExport(0);
        setExportInProcess(true);
        enqueueSnackbar("Запущен экспорт в Excel. Формирование файла может происходить достаточно ДОЛГО " +
                        "в виду необходимости его криптографической обработки в целях информационной безопасности", {variant : "info"});
        dataHub.reportJobController.getExcelReport(excelTemplateId, props.jobId, handleExcelFileResponseNew);
    }

    function handleExcelFileResponseNew(resp){
        if (resp.ok){
            const url = resp.data.urlFile + resp.data.token
            const link = document.createElement('a');
            link.href = url;
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        }
        else {
            enqueueSnackbar("Не удалось получить файл с сервера", {
                variant: 'error',
                action,
                persist: true
            });
        }
        setExportInProcess(false)
    }

    function handleRestartReport(){
        props.onRestartReportClick();
    }

    function handleViewTypeChange(){
        props.onViewTypeChange('PivotTable');
    }
    
    function TablePaginationActions(props) {
        const classes = ReportDataCSS();
        const theme = useTheme();
        const { count, page, rowsPerPage, onPageChange } = props;

        const handleFirstPageButtonClick = event => {
            onPageChange(event, 0);
        };

        const handleBackButtonClick = event => {
            onPageChange(event, page - 1);
        };

        const handleNextButtonClick = event => {
            onPageChange(event, page + 1);
        };

        const handleLastPageButtonClick = event => {
            onPageChange(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
        };

        const handleCustomPage = e => {
            if (e.key === 'Enter') {
                let page_num = parseInt(e.target.value);
                let page_count = Math.ceil(count / rowsPerPage);
                if (!isNaN(page_num) && page_num <= page_count && page_num!==0){
                    onPageChange(e, page_num-1);
                }
            }            
        };

        return (
            <div className={classes.pagination} display="block">
                <Tooltip title="К первой странице">
                    <span>
                        <IconButton className={classes.iconButton}
                            size="small"
                            onClick={handleFirstPageButtonClick}
                            disabled={page === 0}
                            aria-label="first page"
                        >
                            {theme.direction === 'rtl' ? <LastPageIcon /> : <FirstPageIcon />}
                        </IconButton>
                    </span>
                </Tooltip>
                <IconButton className={classes.iconButton}
                    size="small"
                    onClick={handleBackButtonClick}
                    disabled={page === 0}
                    aria-label="previous page"  
                >
                    {theme.direction === 'rtl' ? <KeyboardArrowRight /> : <KeyboardArrowLeft />}
                </IconButton>
                <InputBase className={classes.pageNumber}
                    variant="outlined"
                    size="small"                    
                    defaultValue={pageNumber}
                    onKeyDown={handleCustomPage}
                    inputProps={{ style: {textAlign: 'center'}}}              
                />
                <IconButton className={classes.iconButton}
                    size="small"
                    onClick={handleNextButtonClick}
                    disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                    aria-label="next page"
                >
                    {theme.direction === 'rtl' ? <KeyboardArrowLeft /> : <KeyboardArrowRight />}
                </IconButton>
                <Tooltip title="К последней странице">
                    <span>
                        <IconButton className={classes.iconButton}
                            size="small"
                            onClick={handleLastPageButtonClick}
                            disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                            aria-label="last page"
                        >
                            {theme.direction === 'rtl' ? <FirstPageIcon /> : <LastPageIcon />}
                        </IconButton>
                    </span>
                </Tooltip>
                <Tooltip title="Экспорт Excel">
                    <span>
                        <IconButton className={classes.iconButton}
                            size="small"
                            aria-label="export"
                            onClick={handleExcelExport}
                            disabled={exportInProcess}
                        >
                            {exportInProcess 
                            ? 
                                <CircularProgress size="20px" />
                            :
                                <AssignmentIcon/>
                            }
                        </IconButton>
                    </span>
                </Tooltip>
                {
                    excelTemplates && excelTemplates.length && 
                    <ReportTemplatesList 
                        excelTemplates={excelTemplates}
                        exportInProcess={exportInProcess}
                        onSelectTemplate={id => handleExcelExport(null, id)}
                    />
                }
                {
                    canExecute 
                    && 
                    <Tooltip title="Перезапустить отчет">
                        <span>
                            <IconButton className={classes.iconButton}
                                size="small"
                                aria-label="restart"
                                onClick={handleRestartReport}
                            >
                                <PlayArrowIcon/>
                            </IconButton>
                        </span>
                    </Tooltip>
                }
                {/*<Tooltip title="Сводная таблица">
                    <span>
                        <IconButton className={classes.iconButton}
                            size="small"
                            aria-label="pivot"
                            onClick={handleViewTypeChange}
                        >
                            <Icon path={mdiTablePivot}
                                size={0.9}
                           />
                        </IconButton>
                    </span>
                </Tooltip>
                */}
            </div>
        );
        
    }

    TablePaginationActions.propTypes = {
        count: PropTypes.number.isRequired,
        onPageChange: PropTypes.func.isRequired,
        page: PropTypes.number.isRequired,
        rowsPerPage: PropTypes.number.isRequired
    };

    function handleDataLoaded(data){
        setRowCount(data.rowCount);
        let headers = [];
        let rows = [];

        if(data.records && data.records.length > 0){
            for(let col in data.records[0]){
                headers.push(col);
            }

            for(let rec of data.records){
                rows.push(rec);
            }
        }
        setHeaders(headers);
        setPageRows(rows);
    }

    return(
        <div className={classes.root}>
            <Paper className={classes.buttonPaper} elevation={5}>
                <Scrollbars ref={ScrollbarsRef} autoHeight style={{width: 'auto'}}>
                    {/*Без Measure скролл не реагирует на изменение размера sidebar*/}
                    <Measure
                        bounds
                        onResize={() => {
                        ScrollbarsRef.current && ScrollbarsRef.current.forceUpdate();
                        }}
                    >
                        {({ measureRef }) => 
                            <div ref={measureRef}>
                                <TablePagination 
                                    rowsPerPageOptions={[10, 25, 50, 100, 500, 1000]}
                                    labelRowsPerPage="Строк на странице:"
                                    labelDisplayedRows={({ from, to, count }) => `${from}-${to} из ${count}`}
                                    component="div"
                                    count={rowCount}
                                    rowsPerPage={rowsPerPage}
                                    page={pageNumber-1}
                                    ActionsComponent={TablePaginationActions}
                                    backIconButtonProps={{
                                        'aria-label': 'previous page',
                                    }}
                                    nextIconButtonProps={{
                                        'aria-label': 'next page',
                                    }}
                                    onPageChange={handleChangePage}
                                    onRowsPerPageChange={handleChangeRowsPerPage}
                                    style={{minWidth: '730px'}}
                    
                                />
                            </div>
                        }
                    </Measure>
                </Scrollbars>
            </Paper>

            <DataLoader
                loadFunc = {dataHub.reportJobController.getDataPage}
                loadParams = {[props.jobId, rowsPerPage, pageNumber]}
                onDataLoaded = {handleDataLoaded}
                disabledScroll = {true}
            >
                {rowCount === 0 ?  <Typography className={classes.text}>По заданным фильтрам данных нет!</Typography> 
                :<div className={classes.relativeDiv}>                    
                    <TableContainer className={classes.container}>
                        <Table size="small" stickyHeader>
                            <TableHead>
                                <TableRow key="headRow">
                                    {headers.map( header => (
                                        <TableCell key={header} className={classes.tableHead}>
                                            {header}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {
                                    pageRows.map( (row, ind) => {
                                        let rows = [];
                                        for(let colName of headers){
                                            let colData = row[colName];
                                            rows.push(<TableCell key={colName}>{colData}</TableCell>);
                                        }
                                        return(
                                            <TableRow className={classes.tablerow} key={ind}>
                                                {rows}
                                            </TableRow>
                                        );
                                    })
                                }
                            </TableBody>
                        </Table>
                    </TableContainer>
                </div>
                }
            </DataLoader>

        </div>
    );
        
}
