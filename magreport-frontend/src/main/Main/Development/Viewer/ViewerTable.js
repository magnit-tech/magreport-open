import React from "react";


import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import Table from "@material-ui/core/Table";

import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";

/**
 * callback для определения того, является ли строка валидной
 * @callback checkIsValidRow
 * @param {Object} row - строка
 */
/**
 * @typedef {Object} Column
 * @property {String} label - заголовок
 * @property {String} key - имя свойства Row, в котором лежат данные для данного столбца
 */
/**
 * Компонент просмотра данных в табличном виде
 * @param {Object} props - свойства компонента
 * @param {Array.<Column>} props.columns - массив объектов Column (описание столбцов и ключ для извлечения данных)
 * @param {Array.<Object>} props.rows - массив объектов-строк с данными
 * @param {String} [props.keyName="id"] - имя поля для уникальной идентификации строк
 * @param {checkIsValidRow} props.checkIsValidRow - callback для определения того, является ли строка валидной
 * @return {JSX.Element}
 * @constructor
 */
export default function ViewerTable(props) {

    const classes = ViewerCSS();

    const columns = props.columns || [];
    const rows = props.rows || [];
    const keyName = props.keyName || "id";
    const checkIsValidRow = props.checkIsValidRow || (() => true);

    return (
        <Table size="small" stickyHeader aria-label="simple table">
            <TableHead>
                <TableRow>
                    {columns.map(row =>
                        <TableCell align="center" className={classes.tableCellHead}>{row.label}</TableCell>
                    )}
                </TableRow>
            </TableHead>
            <TableBody>
                {rows.map(row => (
                    <TableRow key={row[keyName]}
                              className={!checkIsValidRow(row) ? classes.invalidRow : null}>
                        {columns.map(col => (
                            <TableCell align="center">{row[col.key]}</TableCell>
                            )
                        )}
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    );
}
