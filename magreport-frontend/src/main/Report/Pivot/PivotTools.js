import React from 'react';
import Icon from '@mdi/react'
import { mdiTable, mdiTableMergeCells, mdiTableSplitCell, mdiTableColumn, 
        mdiTableRow, mdiTableHeadersEyeOff, mdiTableHeadersEye, mdiTrafficLight, mdiTune, mdiCog, mdiContentSaveCogOutline } from '@mdi/js';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Tooltip from '@material-ui/core/Tooltip/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import FullscreenIcon from '@material-ui/icons/Fullscreen';
import FullscreenExitIcon from '@material-ui/icons/FullscreenExit';
/**
 * 
 * @param {*} props.columnsMetricPlacement - true/false
 * @param {*} props.onMetricPlacementChange - function(placeColumn)
 * @param {*} props.mergeMode - true/false
 * @param {*} props.onMergeModeChange - function(mergeMode)
 * @param {*} props.onViewTypeChange - function() - callback смена вида с сводной на простую таблицу
 * @param {*} props.onFullScreen - function() - callback полноэкранный режим
 * @param {*} props.fieldsVisibility - видимость панелей с полями
 * @param {*} props.onFieldsVisibility - function() - callback изменить видимость панелей с полями
 * @param {*} props.onThresholdDialog - открытие диалогового окна для Thresholds
 * @returns 
 */
export default function PivotTools(props){

    function handleViewTypeChange(){
        props.onViewTypeChange('PlainTable');
    }

    return(
        <FormGroup row style={{justifyContent: 'space-between', margin: '2px 8px 2px 16px'}}>
            <div>
                <Tooltip title={(props.fieldsVisibility ? 'Скрыть ' : 'Показать ') + 'панели с полями'}  placement='top'>
                    <FormControlLabel
                        control={
                            <IconButton
                                size="small"
                                aria-label="fields-visibility-off"
                                onClick={() =>props.onFieldsVisibility(!props.fieldsVisibility)}
                            >
                            <Icon path={props.fieldsVisibility ? mdiTableHeadersEyeOff: mdiTableHeadersEye}
                                size={1}
                           />
                            </IconButton>
                        }
                    />
                </Tooltip>
                <Tooltip title="Метрики по столбцам"  placement='top'>
                    <FormControlLabel
                        control={
                            <IconButton
                                size="small"
                                aria-label="merge"
                                onClick={() => {props.onMetricPlacementChange(!props.columnsMetricPlacement)} }
                            >
                            <Icon path={props.columnsMetricPlacement ? mdiTableColumn: mdiTableRow}
                                size={1}
                            />
                            </IconButton>
                        }
                    />
                </Tooltip>
                <Tooltip title={(props.mergeMode ? 'Разделить ' : 'Слить ') +'одинаковые' } placement='top'>
                    <FormControlLabel
                        control={
                            <IconButton
                                size="small"
                                aria-label="merge"
                                onClick={() => {props.onMergeModeChange(!props.mergeMode)} }
                            >
                            <Icon path={props.mergeMode ? mdiTableSplitCell : mdiTableMergeCells}
                                size={1}
                            />
                            </IconButton>
                        }
                    />
                </Tooltip>
                <Tooltip title="Простая таблица"  placement='top'>
                    <FormControlLabel
                        control={
                            <IconButton
                                size="small"
                                aria-label="plain-table"
                                onClick={handleViewTypeChange}
                            >
                            <Icon path={mdiTable}
                                size={1}
                            />
                            </IconButton>
                        }
                    />
                </Tooltip>
                <Tooltip title="Свойства"  placement='top'>
                    <FormControlLabel
                        control={
                            <IconButton
                                size="small"
                                aria-label="plain-table"
                                onClick={() => props.onThresholdDialog(true)}
                            >
                            <Icon path={mdiTrafficLight}
                                size={1}
                            />
                            </IconButton>
                        }
                    />
                </Tooltip>
                <Tooltip title="Конфигурации"  placement='top'>
                    <FormControlLabel
                        control={
                            <IconButton
                                size="small"
                                aria-label="plain-table"
                                onClick={() => {props.onConfigDialog('openConfigDialog')} }
                            >
                            <Icon path={mdiCog}
                                size={1}
                            />
                            </IconButton>
                        }
                    />
                </Tooltip>
                <Tooltip title="Сохранить конфигурацию"  placement='top'>
                    <FormControlLabel
                        control={
                            <IconButton
                                size="small"
                                aria-label="plain-table"
                                onClick={() => {props.onConfigDialog('openConfigSaveDialog')} }
                            >
                            <Icon path={mdiContentSaveCogOutline}
                                size={1}
                            />
                            </IconButton>
                        }
                    />
                </Tooltip>
            </div>
            <div>
                { props.fullScreen ?
					<Tooltip title="Закрыть" placement='left'>
						<IconButton
							size="small"
							aria-label="full-screen-exit"
							onClick={() => props.onFullScreen(false)}
						>
							<FullscreenExitIcon/>
						</IconButton>
					</Tooltip>
					:
					<Tooltip title="Полноэкранный режим"  placement='left'>
						<IconButton
							size="small"
							aria-label="full-screen"
							onClick={() => props.onFullScreen(true)}
						>
							<FullscreenIcon/>
						</IconButton>	
					</Tooltip>
                }
            </div>
        </FormGroup>
    )
}