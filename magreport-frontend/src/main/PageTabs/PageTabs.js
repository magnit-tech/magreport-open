import React, { useState } from 'react';
import { useSnackbar } from 'notistack';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import DesignerTabPanel from 'main/Main/Development/Designer/DesignerTabPanel';
import Typography from '@material-ui/core/Typography';
import Toolbar from '@material-ui/core/Toolbar';

//styles
import {DesignerCSS} from 'main/Main/Development/Designer/DesignerCSS';

/**
 * @callback onTabChange
 * @param {Number} newTab
 */

/**
 * @callback isTabChangeAllowed
 * @param {Number} newTab
 */

/**
 * Компонент-менеджер вкладок
 * @param {Object} props - component properties
 * @param {Number} props.tabNum - Номер вкладки для установки из другого компонента или если нужно открыть сначала не первую вкладку.
 * @param {Array} props.tabsdata - Массив вкладок. Свойства: tablabel - заголовок вкладки, tabdisabled - если true, то вкладка неактивна, tabcontent - JSX элементы с содержимым вкладки
 *                              onFocus - ф-я, которая выполнится при переходе на эту вкладку
 * @param {onTabChange} props.onTabChange - callback на смену вкладки
 * @param {isTabChangeAllowed} props.isTabChangeAllowed - callback для проверки возможности перехода на вкладку
 * @param {String} props.pageName - заголовок страницы
 */
export default function PageTabs(props) {

    const classes = DesignerCSS();

    const { enqueueSnackbar } = useSnackbar();

    let panels = []
    let tabelems = []
    let isTabChangeAllowed = props.isTabChangeAllowed || (() => true);

    /*
        Переключение между вкладками
    */
    function handleTabChange(event, newValue){
        if (!isTabChangeAllowed(newValue)) {
            enqueueSnackbar("Невозможно переключить вкладку. Проверьте правильность данных на текущей вкладке.", {variant: "error"});
            return;
        }
        if (props.tabsdata[newValue].onFocus)
            props.tabsdata[newValue].onFocus();
        setTabValue(newValue);
        if (props.onTabChange) {props.onTabChange(newValue)}
    }

    // вкладка с id=0 может быть скрыта, поэтому ищем первую нескрытую вкладку и она станет вкладкой по умолчанию
    let defaultTabId = false
    props.tabsdata.forEach( function (elem,index) {
        if (defaultTabId===false)
            if (!elem.tabdisabled) {
                defaultTabId = index
                // if (elem.onFocus) elem.onFocus()
            }
    })

    const [tabValue, setTabValue] = useState(defaultTabId || 0);

    props.tabsdata.forEach( function (elem,index) {
        panels.push(
            <DesignerTabPanel key={panels.length} value={tabValue} index={index}>
                {elem.tabcontent}
            </DesignerTabPanel>
        );

        tabelems.push(
            <Tab label={elem.tablabel} key={index} disabled={!!elem.tabdisabled} />
        )
    })

    return (
        <div className={classes.pageTabs}>
            {props.pageName && <AppBar position="static" className={classes.tabTitle} >
                <Toolbar variant="dense" >
                    <Typography variant="h6">
                        {props.pageName}
                    </Typography> 
                </Toolbar>
            </AppBar>}
            <AppBar position="static" color="transparent">       
                <Toolbar variant="dense">    
                    <Tabs 
                        variant="scrollable"
                        scrollButtons="auto"
                        aria-label="scrollable auto tabs"
                        textColor="primary"
                        value={tabValue} 
                        onChange={handleTabChange} 
                    >
                        {tabelems}
                    </Tabs>
                </Toolbar>
            </AppBar>
               <div style={{display: 'flex', flex: 1, flexDirection: 'column'}}> <div style={{display: 'flex', flex: 1, flexDirection: 'column'}}>{panels}</div> </div>
        </div>
    );
}