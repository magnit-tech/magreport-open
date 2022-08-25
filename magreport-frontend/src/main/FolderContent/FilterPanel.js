import React, { useState } from 'react';

// material-ui
import FilterListIcon from '@material-ui/icons/FilterList';
//import Typography from '@material-ui/core/Typography';
import { MuiPickersUtilsProvider, KeyboardDateTimePicker } from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import ruLocale from "date-fns/locale/ru";
import format from "date-fns/format";
import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import Badge from '@material-ui/core/Badge';
//import DoneOutlineIcon from '@material-ui/icons/DoneOutline';
//import HighlightOffIcon from '@material-ui/icons/HighlightOff';
import CloseIcon from '@material-ui/icons/Close';
import DoneIcon from '@material-ui/icons/Done';
import AccessTimeIcon from '@material-ui/icons/AccessTime';
import Grid from '@material-ui/core/Grid';
import Tooltip from '@material-ui/core/Tooltip/Tooltip';
import Paper from '@material-ui/core/Paper';
import IconButton from '@material-ui/core/IconButton';
// local
import JobStatusSelect from './JobFilters/JobStatusSelect'
import JobUsernameSelect from './JobFilters/JobUsernameSelect'
import {FolderItemTypes} from './FolderItemTypes';
import Slide from '@material-ui/core/Slide';
// styles
import { TimeSlider, FolderContentCSS } from './FolderContentCSS';

class RuLocalizedUtils extends DateFnsUtils {
    getCalendarHeaderText(date) {
        return format(date, "LLLL yyyy", { locale: ruLocale });
    }
    getDateTimePickerHeaderText(date) {
        return format(date, "dd.MM", { locale: ruLocale });
    }
    getYearText(date) {
        return format(date, "yyyy", { locale: ruLocale });
    }
    getMonthText(date) {
        return format(date, "LLLL", { locale: ruLocale });
    }
}

export default function FilterPanel(props){

    const marks = [
        {
            value: 1,
            label: '1ч'
        },
        {
            value: 2,
            label: '2ч'
        },
        {
            value: 4,
            label: '4ч'
        },
        {
            value: 6,
            label: '6ч'
        },
        {
            value: 8,
            label: '8ч'
        },
        {
            value: 10,
            label: '10ч'
        },
        {
            value: 12,
            label: '12ч'
        },
        {
            value: 14,
            label: '14ч'
        },
        {
            value: 16,
            label: '16ч'
        },
        {
            value: 18,
            label: '18ч'
        },
        {
            value: 20,
            label: '20ч'
        },
        {
            value: 22,
            label: '22ч'
        },
        {
            value: 24,
            label: '24ч'
        }
      ]; 
    const classes = FolderContentCSS();
    const [panelOpen, setPanelOpen] = useState(false)

    function handleClick(isCleared){
        setPanelOpen(false)
        props.onFilterClick(isCleared)
    }

    function TimeSliderComponent(props) {
        return (
          <span {...props}>
            <AccessTimeIcon/>
          </span>
        );
    }

    let countFilters = 0
    if (props.filters){
        countFilters = Object.entries(props.filters).reduce((acc, [key, value]) => {
            if (value && key !== 'selectedStatuses' && key !=='isCleared') {
                return acc+1
            }
            if (key === 'selectedStatuses' && value.length !== 6){
                return acc+1
            }
            return acc
        }, 0)
    };
    
    return (
        <div >
        { 
            !panelOpen 
            ?
            <div className={classes.filterButton}>
                <Badge color="secondary" overlap="circular" badgeContent={countFilters}>
                    <Tooltip title = "Фильтры" placement="top"> 
                        <Paper elevation={3} className={classes.openSearchBtn}>
                            <IconButton
                                size="small"
                                aria-label="searchBtn"
                                onClick= {()=> setPanelOpen(!panelOpen)}
                            >
                                <FilterListIcon/>
                            </IconButton>
                        </Paper>
                    </Tooltip>
                </Badge>
            </div>
            :
            <Slide direction="down" in={panelOpen} mountOnEnter unmountOnExit>
				<Paper elevation={3} className={classes.drawerStyles}>    
					<Grid container className = {classes.gridFilter}>
						<Grid item className = {classes.divTime}>
							<div className={classes.datesFilter}>
								<MuiPickersUtilsProvider utils={RuLocalizedUtils} locale={ruLocale}>
									<KeyboardDateTimePicker
										size = 'small'
										className={classes.dtmStyle}
										id="datePickerStart"
										view={["date" | "year" | "month" | "hours" | "minutes"]}
										openTo="hours"
										ampm={false}
										disableFuture
										format="dd.MM.yyyy HH:mm"
										margin="normal"
										inputVariant="filled"
										value={props.filters.periodStart}
										onChange={date => props.onFilterChange('periodStart', date)}
                                        label="Начало периода"
                                        cancelLabel="ОТМЕНИТЬ"
                                        okLabel="СОХРАНИТЬ"
									/>   
									<KeyboardDateTimePicker
										size = 'small'
										className={classes.dtmStyle}
										id="datePickerEnd"
										view={["date" | "year" | "month" | "hours" | "minutes"]}
										openTo="hours"
										ampm={false}
										disableFuture
										format="dd.MM.yyyy HH:mm"
										margin="normal"
										inputVariant="filled"
										value={props.filters.periodEnd}
										onChange={date => props.onFilterChange('periodEnd', date)}
                                        label="Конец периода"
                                        cancelLabel="ОТМЕНИТЬ"
                                        okLabel="СОХРАНИТЬ"
									/>
                                    </MuiPickersUtilsProvider>
							</div>
							<TimeSlider
								ThumbComponent = {TimeSliderComponent}
								marks = {marks}
								min = {0}
								max = {25}
								step={1}
                                onChange={(event, value) => props.onFilterChange('updatePeriod', value)}
							>
							</TimeSlider>
						</Grid>
						{ props.itemsType === FolderItemTypes.userJobs &&
							<Grid item>
								<JobUsernameSelect 
									user={props.filters.user}
									onChange={user => props.onFilterChange('user', user)}
								/>
							</Grid>
						}
						<Grid item className = {classes.itemStatusFilter}>
							<JobStatusSelect 
								selectedStatuses={props.filters.selectedStatuses}
								onChange={statuses => props.onFilterChange('selectedStatuses', statuses)}
							/>
						</Grid>
					</Grid>
					<ButtonGroup variant="outlined" orientation="vertical" style = {{marginTop: "4px"}}>
						<Tooltip title="Очистить фильтр"> 
							<Button   
								onClick={() => {handleClick(true)}}
							>
								<CloseIcon  variant="contained" 
									color="primary"
								/>
							</Button>
						</Tooltip>
						<Tooltip title="Применить фильтр"> 
							<Button
								onClick={() => {handleClick(false)}}
							>
								<DoneIcon variant="contained" 
									color="secondary"/>
							</Button>
						</Tooltip>
					</ButtonGroup>
				</Paper>
            </Slide>
        }
        </div>
    );
}