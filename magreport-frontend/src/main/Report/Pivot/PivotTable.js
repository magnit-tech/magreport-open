import React, { useRef} from 'react';
import clsx from 'clsx';
import Measure from 'react-measure';
import Box from '@material-ui/core/Box';
import { Scrollbars } from 'react-custom-scrollbars';
// magreport
import {AggFunc} from '../../FolderContent/JobFilters/JobStatuses';
import {PivotCSS} from './PivotCSS';

/**
 * @param {*} props.tableData - данные таблицы
 * @param {*} props.pivotConfiguration - конфигурация сводной
 * @param {*} props.onDimensionValueFilter - колбэк по фильтрации по значению измерения (возникает при нажатии на это значение в таблице)
 * @param {*} props.onChangeInnerTableSize - колбэк по изменению внутреннего размера таблицы
 * @param {*} props.onMetricNameCellClick
 */
export default function(props){

    const styles = PivotCSS();
    const ScrollbarsRef = useRef(null);

    /*
        Некоторые важные значения из объекта конфигурации для сокращения обозначений
    */
    const metricPlacement = props.pivotConfiguration.columnsMetricPlacement ? "COLUMNS" : "ROWS";

    /*
        *************************************************************************
        Подготовка данных для отображения
        Сначала подготавливается таблица с необъединёнными ячейками
        Потом при необходимости происходит merge ячеек с одинаковыми значениями
        *************************************************************************
    */

    // На сколько умножается высота строк для измерений
    let rowsMetricFactor = metricPlacement === "ROWS" ? Math.max(props.tableData.metrics.length, 1) : 1;

    // На сколько умножается ширина столбцов измерений
    let colsMetricFactor = metricPlacement === "COLUMNS" ? Math.max(props.tableData.metrics.length, 1) : 1;

    /*
        Инфтсрументарий для формирования строк
    */

    function printBlankLeftTopIfNeeded(tableRow, leftCornerCellWidth, leftCornerCellHeight) {
        if(leftCornerCellWidth > 0 && leftCornerCellHeight > 0){
            tableRow.push({
                data : "",
                type : "leftTopCorner",
                colSpan : leftCornerCellWidth,
                rowSpan : leftCornerCellHeight
            });
        }        
    }

    function printColumnDimension(tableRow, dimNum) {
        // Название измерения по столбцам
        tableRow.push({
            data : props.tableData.columnDimensionsFields[dimNum].fieldName,
            type : "dimensionName",
            colSpan : 1,
            rowSpan : 1
        });
        // Значения измерения по столбцам
        for(let j = 0; j < props.tableData.columnDimensionsValues.length; j++){
            tableRow.push({
                data : props.tableData.columnDimensionsValues[j][dimNum],
                fieldId : props.tableData.columnDimensionsFields[dimNum].fieldId,
                type : "dimensionValue",
                colSpan : colsMetricFactor,
                rowSpan : 1
            });
        }
    }

    function printRowDimensionNames(tableRow) {
        // Названия измерений по строкам
        for(let f of props.tableData.rowDimensionsFields){
            tableRow.push({
                data : f.fieldName,
                fieldId : f.fieldId,
                type : "dimensionName",
                colSpan : 1,
                rowSpan : 1
            });
        }
    }

    function printRowDimensionValues(tableRow, rowNum) {
        for(let j = 0; j < props.tableData.rowDimensionsValues[rowNum].length; j++){
            tableRow.push({
                data : props.tableData.rowDimensionsValues[rowNum][j],
                fieldId : props.tableData.rowDimensionsFields[j].fieldId,
                type : "dimensionValue",
                colSpan : 1,
                rowSpan : rowsMetricFactor
            });
        }
    }

    function printMetricNamesRow(tableRow) {
        for(let j = 0; j < props.tableData.columnDimensionsValues.length; j++){
            for(let mIndex in props.tableData.metrics){
                let m = props.tableData.metrics[mIndex];
                tableRow.push({
                    fieldIndex : mIndex,
                    data : AggFunc.get(m.aggregationType) + " " + m.metricName,
                    type : "metricName",
                    colSpan : 1,
                    rowSpan : 1
                });
            }
        }        
    }

    function printMetricInRow(tableRow, metricNum, rowNum) {
        tableRow.push({
            fieldIndex : metricNum,
            data : AggFunc.get(props.tableData.metrics[metricNum].aggregationType) + " " + props.tableData.metrics[metricNum].metricName,
            type : "metricName",
            style: props.pivotConfiguration.fieldsLists.metricFields[metricNum]?.formatting || '',
            colSpan : 1,
            rowSpan : 1
        });
        for(let j = 0; j < props.tableData.columnDimensionsValues.length; j++){
            tableRow.push({
                fieldIndex : metricNum,
                data : props.pivotConfiguration.fieldsLists.metricFields[metricNum]?.changeData(props.tableData.metrics[metricNum].values[j][rowNum]) || props.tableData.metrics[metricNum].values[j][rowNum],
                type : "metricValues",
                style: props.pivotConfiguration.fieldsLists.metricFields[metricNum]?.formatting || '',
                colSpan : 1,
                rowSpan : 1
            });
        }
    }

    function printAllMetricInColumnsValues(tableRow, rowNum) {

        for(let j = 0; j < props.tableData.columnDimensionsValues.length; j++){
            for(let m = 0; m < props.tableData.metrics.length; m++){
                
                tableRow.push({
                    data : props.tableData.metrics[m].values[j][rowNum],
                    type : "metricValues",
                    style: props.pivotConfiguration.fieldsLists.metricFields[m].formatting,
                    colSpan : 1,
                    rowSpan : 1
                });
            }
        }
    }

    /*
        Формируем строки
    */

    // Количество строк в заголовочной части таблицы (атрибуты по столбцам и строка названий метрик)
    let nHeaderRows = 0;

    // Размеры пустой ячейки левого верхнего угла
    let leftCornerCellWidth = 0;
    let leftCornerCellHeight = 0;

    // Массив строк таблицы
    let tableRows = [];
    let rowNum = -1;

    if(props.tableData.columnDimensionsFields.length > 0 && props.tableData.rowDimensionsFields.length > 0){
        if(metricPlacement === "ROWS"){
            nHeaderRows = props.tableData.columnDimensionsFields.length;
            leftCornerCellWidth = props.tableData.rowDimensionsFields.length;
            leftCornerCellHeight = props.tableData.columnDimensionsFields.length - 1;

            // Строки с измерениями по столбцам кроме последней
            for(let i = 0; i < props.tableData.columnDimensionsFields.length - 1; i++){
                tableRows.push([]);
                rowNum++;
                if(i === 0){
                    printBlankLeftTopIfNeeded(tableRows[rowNum], leftCornerCellWidth, leftCornerCellHeight);
                }
                printColumnDimension(tableRows[rowNum], i);
            }

            // Последняя строка измерений по столбцам содержит в начале названия измерений по строкам
            tableRows.push([]);
            rowNum++;
            printRowDimensionNames(tableRows[rowNum]);
            printColumnDimension(tableRows[rowNum], rowNum);
            
            // Далее строки со значениями измерений по столбцам в начале, затем название метрики и затем значения данной метрики
            for(let i = 0; i < props.tableData.rowDimensionsValues.length; i++){
                tableRows.push([]);
                rowNum++;
                printRowDimensionValues(tableRows[rowNum], i);
                for(let m = 0; m < props.tableData.metrics.length; m++){
                    if(m > 0){
                        tableRows.push([]);
                        rowNum++;
                    }
                    printMetricInRow(tableRows[rowNum], m, i);
                }
            }
        }
        else{
            nHeaderRows = props.tableData.columnDimensionsFields.length + 1;
            leftCornerCellWidth = props.tableData.rowDimensionsFields.length - 1;
            leftCornerCellHeight = props.tableData.columnDimensionsFields.length;

            let rowNum = -1;

            // Строки с измерениями по столбцам
            for(let i = 0; i < props.tableData.columnDimensionsFields.length; i++){
                tableRows.push([]);
                rowNum++;
                if(i === 0){
                    printBlankLeftTopIfNeeded(tableRows[rowNum], leftCornerCellWidth, leftCornerCellHeight);
                }
                printColumnDimension(tableRows[rowNum], i);
            }

            // Строка с названиями измерений по строкам и с названиями метрик
            tableRows.push([]);
            rowNum++;
            printRowDimensionNames(tableRows[rowNum]);
            printMetricNamesRow(tableRows[rowNum]);

            // Далее строки со значениями измерений по столбцам в начале, затем метрики по столбцам
            for(let i = 0; i < props.tableData.rowDimensionsValues.length; i++){
                tableRows.push([]);
                rowNum++;
                printRowDimensionValues(tableRows[rowNum], i);
                printAllMetricInColumnsValues(tableRows[rowNum], i);
            }
        }
    }
    else if(props.tableData.columnDimensionsFields.length > 0){
        // Только измерения по столбцам
        if(metricPlacement === "ROWS"){
            nHeaderRows = props.tableData.columnDimensionsFields.length;
            // Строки с измерениями по столбцам
            for(let i = 0; i < props.tableData.columnDimensionsFields.length; i++){
                tableRows.push([]);
                rowNum++;
                printColumnDimension(tableRows[rowNum], i);
            }
            for(let m = 0; m < props.tableData.metrics.length; m++){
                tableRows.push([]);
                rowNum++;
                printMetricInRow(tableRows[rowNum], m, 0);
            }
        }
        else{
            nHeaderRows = props.tableData.columnDimensionsFields.length + 1;
            // Строки с измерениями по столбцам
            for(let i = 0; i < props.tableData.columnDimensionsFields.length; i++){
                tableRows.push([]);
                rowNum++;
                printColumnDimension(tableRows[rowNum], i);
            }
            if(props.tableData.metrics.length > 0)
            {
                // Строка с названиями метрик
                tableRows.push([]);
                rowNum++;
                printBlankLeftTopIfNeeded(tableRows[rowNum], 1, 2);            
                printMetricNamesRow(tableRows[rowNum]);
                tableRows.push([]);
                rowNum++;
                printAllMetricInColumnsValues(tableRows[rowNum], 0);     
            }
        }
    }
    else if(props.tableData.rowDimensionsFields.length > 0){
        // Только измерения по строкам
        if(metricPlacement === "ROWS"){
            nHeaderRows = 1;
            // Строка с названиями измерений по строкам и с названиями метрик
            tableRows.push([]);
            rowNum++;
            printRowDimensionNames(tableRows[rowNum]);
            // Далее строки со значениями измерений по столбцам в начале, затем название метрики и затем значения данной метрики
            for(let i = 0; i < props.tableData.rowDimensionsValues.length; i++){
                tableRows.push([]);
                rowNum++;
                printRowDimensionValues(tableRows[rowNum], i);
                for(let m = 0; m < props.tableData.metrics.length; m++){
                    if(m > 0){
                        tableRows.push([]);
                        rowNum++;
                    }
                    printMetricInRow(tableRows[rowNum], m, i);
                }
            }
        }
        else{
            nHeaderRows = 1;
            tableRows.push([]);
            rowNum++;
            printRowDimensionNames(tableRows[rowNum]);
            printMetricNamesRow(tableRows[rowNum]);

            // Далее строки со значениями измерений по столбцам в начале, затем метрики по столбцам
            for(let i = 0; i < props.tableData.rowDimensionsValues.length; i++){
                tableRows.push([]);
                rowNum++;
                printRowDimensionValues(tableRows[rowNum], i);
                printAllMetricInColumnsValues(tableRows[rowNum], i);
            }
        }
    }
    else{
        // Только метрики
        if(props.tableData.metrics.length > 0){
            if(metricPlacement === "ROWS"){
                nHeaderRows = 0;
                for(let m = 0; m < props.tableData.metrics.length; m++){
                    tableRows.push([]);
                    rowNum++;
                    printMetricInRow(tableRows[rowNum], m, 0);
                }
            }
            else{
                nHeaderRows = 1;
                tableRows.push([]);
                rowNum++;
                printRowDimensionNames(tableRows[rowNum]);
                printMetricNamesRow(tableRows[rowNum]);

                // Далее - значения метрик
                tableRows.push([]);
                rowNum++;
                printAllMetricInColumnsValues(tableRows[rowNum], 0);
            }
        }
    }     

    if(props.pivotConfiguration.mergeMode){
        mergeCells(tableRows, props.tableData.columnDimensionsFields.length, nHeaderRows, colsMetricFactor, rowsMetricFactor);
    }

    /*
    ******************
        Обработчики
    ******************
    */
    function handleDimensionValueCellClick(fieldId, fieldValue) {
        props.onDimensionValueFilter(fieldId, fieldValue);
    }

    function handleMetricNameCellClick(fieldIndex){
        props.onMetricNameCellClick(fieldIndex);
    }

    function handleContextClick(event, type, cell){
        props.onContextMenu(event, type, cell);
    }

    return(
        <div className={clsx(styles.pivotTable)}>
             <Scrollbars 
                ref={ScrollbarsRef}
            >
                {/*Без Measure скролл не реагирует на изменение размера других компонентов*/}
                
                <table style={{borderCollapse: 'collapse'}} className={styles.table}>
                    <Measure
						bounds
						onResize={contentRect => {
							props.onChangeInnerTableSize({ dimensions: contentRect.bounds });
						}}
					>
					{({ measureRef }) => {
						return (
							
						<tbody  ref={measureRef} >
							{tableRows.map((r, ind) => (
								<tr key = {ind} className={styles.tr}>
									{r.filter((cell) => (cell.colSpan > 0 && cell.rowSpan > 0)).map( (cell, ind) => (
                                        <td key = {ind} colSpan = {cell.colSpan} rowSpan = {cell.rowSpan}
											onClick = {
												cell.type === "dimensionValue" ?
													() => {handleDimensionValueCellClick(cell.fieldId, cell.data)}
                                                : cell.type === "metricName" ? () => {handleMetricNameCellClick(cell)}
												: () => {}
                                            }
                                            onContextMenu={ (event) => { handleContextClick(event, cell.type, cell) } }
											className = {clsx({
												[styles.cell] : true,
												[styles.leftTopCell] : cell.type === "leftTopCorner",
												[styles.blanc] : cell.type === "blanc",
												[styles.dimensionNameCell] : cell.type === "dimensionName",
												[styles.dimensionValueCell] : cell.type === "dimensionValue",
												[styles.metricNameCell] : cell.type === "metricName",
												[styles.metricValueCell] : cell.type === "metricValue",
											})}
										>
											<Box 
												fontSize={10} 
												fontWeight={cell.type === "dimensionName" ||
													cell.type === "metricName" || (cell.style && cell.style.bold) ? 
													"fontWeightBold" : 
													"fontWeightMedium"}
												style={{margin: '2px'}}
											>
												{cell.data}
											</Box>
										</td>
									))}
								</tr>
							))}
						</tbody>
						)}
					}
				</Measure>
      
            </table>
            </Scrollbars>
        </div>
        
    )
}

function mergeCells(tableRows, columnDimensionsNum, nHeaderRows, colsMetricFactor, rowsMetricFactor){

    // merge columns
    let colSpan = 1;
    for(let i = 0; i < columnDimensionsNum - 1; i++){
        for(let j = tableRows[i].length-1; j >= 0 ; j--){
            if(tableRows[i][j].type === "dimensionValue"){
                let jUp = j;
                if(i === 1 && tableRows[0][0].type === "leftTopCorner" && tableRows[0][0].rowSpan > 1){
                    jUp = j + 1;
                }
                if( (j > 1 || ( i === 0 && tableRows[0][0].type === "leftTopCorner" && j > 2) ) && 
                    tableRows[i][j].data === tableRows[i][j-1].data &&
                    (i === 0 || tableRows[i - 1][jUp].colSpan === 0)){
                    tableRows[i][j].colSpan = 0;
                    colSpan++;
                }
                else{
                    tableRows[i][j].colSpan = colSpan * colsMetricFactor;
                    colSpan = 1;
                }
            }
        }
    }

    // merge rows
    let rowSpan = new Array();
    for(let i = tableRows.length - rowsMetricFactor; i >= nHeaderRows; i -= rowsMetricFactor){
        for(let j = 0; j < tableRows[i].length; j++){
            if(tableRows[i][j].type === "dimensionValue"){
                if(j >= rowSpan.length){
                    rowSpan.push(1);
                }
                if( i > nHeaderRows &&
                    tableRows[i][j].data === tableRows[i - rowsMetricFactor][j].data &&
                    (j === 0 || tableRows[i][j-1].rowSpan === 0)){
    
                    rowSpan[j]++;
                    tableRows[i][j].rowSpan = 0;
                }
                else{
                    tableRows[i][j].rowSpan = rowSpan[j] * rowsMetricFactor;
                    rowSpan[j] = 1;
                }  
            }
        }
    }
}
