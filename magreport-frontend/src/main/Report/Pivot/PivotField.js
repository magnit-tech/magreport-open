import React from 'react';
import { Draggable} from 'react-beautiful-dnd';
import Icon from '@mdi/react';
import { mdiFilter}  from '@mdi/js';
import { mdiPencil } from '@mdi/js';
import clsx from 'clsx';
import Popover from '@material-ui/core/Popover';
import ListItem from '@material-ui/core/ListItem';
import Box from '@material-ui/core/Box';
//import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import IconButton from '@material-ui/core/IconButton';
//local
import {AggFunc} from '../../FolderContent/JobFilters/JobStatuses';
import {PivotCSS} from './PivotCSS';

/**
 * @param {*} props.listName - список, в котором размещено поле
 * @param {*} props.fieldId - id поля
 * @param {*} props.index - индекс поля в списке поля
 * @param {*} props.fieldName - имя поля
 * @param {*} props.aggFuncName - имя агрегирующей функции (для метрик)
 * @param {*} props.filter - фильтр на поле
 * @param {*} props.filtered - признак наличия фильтрации по полю
 * @param {*} props.onButtonClick - функция вызывается при начатии на кнопку
 * @returns 
 */

export default function PivotField(props){
    const styles = PivotCSS();

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handlePopoverOpen = (event) => {
        setAnchorEl(event.currentTarget);
      };
    
      const handlePopoverClose = () => {
        setAnchorEl(null);
      };
    
      const open = Boolean(anchorEl);

      const handleClick=(e) => {
      //  e.stopPropagation();
        props.onButtonClick(e, props.index);
      }

    return(
        <Draggable draggableId={props.listName + "-" + props.fieldId.toString() + "-" + props.index} index={props.index}>
            {(provided, snapshot)=>(
                <ListItem
                    className={clsx({
                        [styles.field] : true,
                        [styles.draggingField] : snapshot.isDragging
                    })}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    innerRef = {provided.innerRef}
                >
                    <Popover
                        id="mouse-over-popover"
                        className={styles.popover}
                        classes={{
                            paper: styles.paper,
                        }} 
                        open={open}
                        anchorEl={anchorEl}
                        anchorOrigin={{
                            vertical: 'center',
                            horizontal: 'center',
                        }}
                        transformOrigin={{
                            vertical: 'center',
                            horizontal: 'center',
                        }}
                        onClose={handlePopoverClose}
                        disableRestoreFocus
                    >
                        <div className={styles.popoverDiv}>
                            <Box fontSize={10} fontWeight={"fontWeightMedium"} className={styles.fieldTextHover}>
                                {props.filtered &&
                                    <Icon path={mdiFilter} size={0.5}/> 
                                }
                                {(props.aggFuncName ? AggFunc.get(props.aggFuncName) + ' ' : '') + props.fieldName} 
                            </Box>
                        </div>
                    </Popover>
                    {(props.listName === 'filterFields' || props.listName === 'metricFields') &&
                        <div className={styles.divFilterButton}>
                            <IconButton
                                size='small'
                                className={styles.filterButton}
                                onClick={handleClick}
                            >   
                                {props.listName === 'filterFields' ?  <Icon path={mdiFilter} size={0.8}/> :
                                props.listName === 'metricFields'  ? <Icon path={mdiPencil} size={0.8}/> : null
                                }
                            </IconButton>
                        </div>
                    }

                    <ListItemText className={styles.listItemText}
                        aria-owns={open ? 'mouse-over-popover' : undefined}
                        aria-haspopup="true"
                        onMouseEnter={handlePopoverOpen}
                        onMouseLeave={handlePopoverClose}
                    >                      
                        <Box fontSize={9} fontWeight={"fontWeightMedium"} className={styles.fieldText}>
                            {props.filtered && props.listName !== 'filterFields' &&
                                <Icon path={mdiFilter} size={0.8}/>
                            }
                            {(props.aggFuncName ? AggFunc.get(props.aggFuncName) + ' ' : '') + props.fieldName}
                        </Box>
                            
                    </ListItemText>
                    
                </ListItem>
          
            )}
        </Draggable>
    )
}