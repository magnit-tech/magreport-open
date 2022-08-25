import React from 'react';

// components
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead'; 
import TableRow from '@material-ui/core/TableRow';
//import Button from '@material-ui/core/Button';
import Link from '@material-ui/core/Link';

import {FolderItemTypes} from 'main//FolderContent/FolderItemTypes';
import DesignerPage from "main/Main/Development/Designer/DesignerPage";
import PageTabs from 'main/PageTabs/PageTabs';
import Typography from '@material-ui/core/Typography';

// styles
import {DatasetMenuCSS} from './DatasetsMenu/DatasetMenuCSS'

export default function DependencyViewer(props){

    const classes = DatasetMenuCSS();

    const pagename = (props.itemsType === FolderItemTypes.dataset) ? 'Зависимости от набора данных: ' + props.data.name 
                   : (props.itemsType === FolderItemTypes.reportsDev) ? 'Зависимости от отчёта: ' +  props.data.name
                   : (props.itemsType === FolderItemTypes.datasource) ? 'Зависимости от источника данных: ' +  props.data.name 
                   : (props.itemsType === FolderItemTypes.filterInstance) ? 'Зависимости от источника данных: ' +  props.data.name : null ;

    const folders = {
        folders: {name: 'Отчёты', itemsType: 'folders' },
        reports: {name: 'Отчёты', itemsType: FolderItemTypes.reportsDev},
        filterInstances: {name: 'Экземпляры фильтров', itemsType: FolderItemTypes.filterInstance},
        asmSecurities: {name: 'AMS объекты', itemsType: null},
        securityFilters: {name: 'Фильтры безопасности', itemsType: FolderItemTypes.securityFilters},
        dataSets: {name: 'Наборы данных', itemsType: FolderItemTypes.dataset}
    } ;

    function handleLinkPathClick(itemType, pathArr, event){
        event.preventDefault()
        props.onLinkPathClick(itemType, pathArr[pathArr.length-1].id)
    }

    let tabs = [];

    for (const [key, value] of Object.entries(folders)) {
        if (props.data[key]?.length > 0 ){
            tabs.push({
                tablabel: value.name,
                tabcontent: 
                <DesignerPage
                    onCancelClick = {() => props.onExit()}
                    cancelName="Закрыть"
                    disabledPadding = {true}
                >
                    <TableContainer className={classes.dsdViewerTable}>
                        <Table stickyHeader size="small" aria-label="a dense table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="center" className={classes.tableCellHead}>ID</TableCell>
                                    <TableCell align="left"   className={classes.tableCellHead}>Наименование</TableCell>
                                    <TableCell align="left"   className={classes.tableCellHead}>Описание</TableCell>
                                    <TableCell align="left"   className={classes.tableCellHead}>Путь к объекту</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {props.data[key].map(row => (
                                    <TableRow key={`${row.id}_${key}`}>
                                        <TableCell align="center" component="th" scope="row">{row.id ? row.id : props.data.id}</TableCell>
                                        <TableCell align="left">{row.name ? row.name : props.data.name}</TableCell>
                                        <TableCell align="left">{row.description ? row.description: props.data.description}</TableCell>
                                        <TableCell align="left">
                                            {row.path && row.path.length > 0
                                                ?   
                                                <Link href="#" onClick={event => handleLinkPathClick(value.itemsType === 'folders' ? 'report' : value.itemsType, row.path, event)} color="inherit">
                                                    {`/ ${row.path.map(p => p.name).join(' / ')}`}
                                                </Link>                                                                
                                                : 
                                               ''
                                            }
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </DesignerPage>
            });
        };
    };

    return (
        <div style={{display: 'flex', flex: 1}}>
            {(tabs.length === 0) ?
                <DesignerPage
                    name = {pagename}
                    onCancelClick = {() => props.onExit()}
                    cancelName="Закрыть"
                    disabledPadding = {true}
                >
                    <Typography variant='h6' style={{margin: 'auto'}}> Нет зависимостей! </Typography>
                </DesignerPage>
                :
                <PageTabs
                    tabsdata={tabs}
                    pageName={pagename}               
                />
            }
        </div>
    );
}