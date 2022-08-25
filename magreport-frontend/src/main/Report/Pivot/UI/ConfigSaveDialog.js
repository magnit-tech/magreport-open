import React, { useRef, useState } from 'react';
import { PivotCSS } from '../PivotCSS';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogTitle from '@material-ui/core/DialogTitle';
import Paper from '@material-ui/core/Paper';
import Draggable from 'react-draggable';

import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Box from '@material-ui/core/Box';

// Select
import { FormControl, InputLabel, MenuItem, Select, TextField, Typography } from '@material-ui/core';

//Перетаскивание модального окна
function PaperComponent(props) {
    return (
		<Draggable handle="#drag-title" cancel={'[class*="MuiDialogContent-root"]'}>
			<Paper {...props} />
		</Draggable>
    );
}

//Обертка для children у табов
function TabPanel(props) {
	const { children, value, index, ...other } = props;
  
	return (
		<div
			role="tabpanel"
			hidden={value !== index}
			id={`scrollable-auto-tabpanel-${index}`}
			aria-labelledby={`scrollable-auto-tab-${index}`}
			{...other}
		>
			{value === index && (
				<Box style={{padding: '24px'}}>
					{children}
				</Box>
			)}
		</div>
	);
}

export default function ConfigSaveDialog(props){

	const classes = PivotCSS();

	const availableConfigs = props.configs

	// Переключение главных табов
	const [mainTabsValue, setMainTabsValue] = useState(0);
	const handleChangeMainTabs = (event, newValue) => {
		setMainTabsValue(newValue)
		
		if (newValue === 1 && existingConfig.show) {
			return objectToSend.current = {
				type: 'saveExistingConfig',
				...existingConfig,
			}
		} 

		return objectToSend.current = {
			type: 'saveNewConfig',
			...newConfig,
		}

	};


	// Объект конфигурации для отправки в родителя
	const objectToSend = useRef({
		type: 'saveNewConfig',
		name: 'Новая конфигурация',
		description: '',
		jobId: null,
		olapConfigId: '',
		report: null,
		reportOlapConfigId: null,
		saveFor: "job",
	})

	// Новая конфигурация
	const [newConfig, setNewConfig] = useState({
		name: 'Новая конфигурация',
		description: '',
		saveFor: "job"
	});

	// Существующая конфигурация
	const [existingConfig, setExistingConfig] = useState({
		show: false,
		name: '',
		description: '',
		jobId: null,
		olapConfigId: '',
		report: null,
		saveFor: "job"
	});

	// Переключение select
	const handleChange = (event, action) => {
		const e = event.target.value;

		switch(action) {
			case 'nameNewConfig':
				objectToSend.current = {
					...objectToSend.current,
					name: e
				}
				setNewConfig({...newConfig, name: e})
				break;
			case 'descriptionNewConfig':
				objectToSend.current = {
					...objectToSend.current,
					description: e
				}
				setNewConfig({...newConfig, description: e})
				break;	
			case 'saveForNewConfig':
				objectToSend.current = {
					...objectToSend.current,
					saveFor: e
				}
				setNewConfig({...newConfig, saveFor: e})
				break;


			case 'listExistingConfig': {
				const config = availableConfigs.find(obj => obj.olapConfig.id === e),
					{ name, description, id } = config.olapConfig,
					{ jobId, report, reportOlapConfigId } = config

				setExistingConfig(
					{
						...existingConfig, 
						show: true, 
						name, 
						description, 
						jobId, 
						olapConfigId: id, 
						report,
						saveFor: jobId !== null ? 'job' : 'report'
					}
				)

				objectToSend.current = {
					type: 'saveExistingConfig',
					name,
					description,
					jobId,
					olapConfigId: id,
					report,
					reportOlapConfigId,
					saveFor: jobId !== null ? 'job' : 'report',
				}
				break;
			}

			case 'nameExistingConfig':
				objectToSend.current = {
					...objectToSend.current,
					name: e
				}
				setExistingConfig({...existingConfig, name: e})
				break;
			case 'descriptionExistingConfig':
				objectToSend.current = {
					...objectToSend.current,
					description: e
				}
				setExistingConfig({...existingConfig, description: e})
				break;
			case 'saveForExistingConfig':
				objectToSend.current = {
					...objectToSend.current,
					saveFor: e
				}
				setExistingConfig({...existingConfig, saveFor: e})
				break;
			default:
				return false
		}	
	};

  	return (
        <Dialog
            open={props.open}
            PaperComponent={PaperComponent}
            aria-labelledby="drag-title"
        >

            <DialogTitle className={classes.dialogTitle} id="drag-title"> Сохранение конфигурации </DialogTitle>

			<div className={classes.FD_root}>

				{/* Main Tabs */}
				<AppBar position="static" color="default">
					<Tabs
						value={mainTabsValue}
						onChange={handleChangeMainTabs}
						indicatorColor="primary"
						textColor="primary"
						variant="scrollable"
						scrollButtons="auto"
						aria-label="scrollable auto tabs example"
					>
						<Tab label="Новая конфигурация" />
						<Tab label="Существующая конфигурация" />
					</Tabs>
				</AppBar>
				
				{/* Новая конфигурация */}
				<TabPanel value={mainTabsValue} index={0}>
					<TextField
						required
						error={ newConfig.name.replace(/\s/g,"") === "" ? true : false }
						id="outlined-required"
						label="Название"
						placeholder="Введите название конфигурации"
						className={classes.CSD_nameField}
						InputLabelProps={{
							shrink: true,
						}}
						variant="outlined"
						value={newConfig.name}
						onChange={(event) => handleChange(event, 'nameNewConfig')}
					/>
					<TextField
						id="outlined-full-width"
						label="Описание"
						placeholder="Введите описание к конфигурации"
						multiline
						rows={5}
						className={classes.CSD_descriptionField}
						InputLabelProps={{
							shrink: true,
						}}
						variant="outlined"
						value={newConfig.description}
						onChange={(event) => handleChange(event, 'descriptionNewConfig')}
					/>
					<Box className={classes.CSD_wrapperSaveFor}>
						<Typography className={classes.CSD_saveFor}>Сохранить для:</Typography>
						<Select
								labelId="select-label"
								id="select"
								value={newConfig.saveFor}
								onChange={(event) => handleChange(event, 'saveForNewConfig')}
							>
								<MenuItem value="report"> отчёт </MenuItem>
								<MenuItem value="job"> задание </MenuItem>
								<MenuItem value="forEveryone" disabled> для всех </MenuItem>
						</Select>
					</Box>
				</TabPanel>

				{/* Существующая конфигурация */}
				<TabPanel value={mainTabsValue} index={1}>
					{
						availableConfigs.length > 0 ?
						<>
							<Box className={classes.CSD_wrapperList}>
								<FormControl variant="outlined" className={classes.CSD_formControl}>
									<InputLabel id="selectExistingConfigLabel">Список существующих конфигураций</InputLabel>
									<Select
										labelId="selectExistingConfigLabel"
										id="selectExistingConfig"
										label="Список существующих конфигураций"
										value={existingConfig.olapConfigId}
										onChange={(event) => handleChange(event, 'listExistingConfig')}
									>
										{ availableConfigs.map( item => {
											return (
												<MenuItem 
													key={item.olapConfig.id}
													value={item.olapConfig.id}
												>
													{item.olapConfig.name}
												</MenuItem>
											)
										})}
									</Select>
								</FormControl>
							</Box>

							{ existingConfig.show &&  
								<>
									<TextField
										required
										error={ existingConfig.name.replace(/\s/g,"") === "" ? true : false }
										id="outlined-required"
										label="Название"
										placeholder="Введите название конфигурации"
										className={classes.CSD_nameFieldExisting}
										InputLabelProps={{
											shrink: true,
										}}
										variant="outlined"
										value={ existingConfig.name}
										onChange={(event) => handleChange(event, 'nameExistingConfig')}
									/>
									<TextField
										id="outlined-full-width"
										label="Описание"
										placeholder="Введите описание к конфигурации"
										multiline
										rows={5}
										className={classes.CSD_descriptionField}
										InputLabelProps={{
											shrink: true,
										}}
										variant="outlined"
										value={existingConfig.description}
										onChange={(event) => handleChange(event, 'descriptionExistingConfig')}
									/>
									<Box className={classes.CSD_wrapperSaveFor}>
										<Typography className={classes.CSD_saveFor}>Сохранить для:</Typography>
										<Select
												labelId="select-label"
												id="select"
												value={existingConfig.saveFor}
												onChange={(event) => handleChange(event, 'saveForExistingConfig')}
											>
												<MenuItem value="report">отчёт</MenuItem>
												<MenuItem value="job">задание</MenuItem>
												<MenuItem value="forEveryone" disabled>для всех</MenuItem>
										</Select>
									</Box>
								</>
							}
						</>
						:
						<Box className={classes.CSD_wrapperList}>
							<Typography style={{ textAlign: 'center' }}>Нет существующих конфигураций</Typography>
						</Box>
					}
				</TabPanel>

			</div>

			<DialogActions>
				<Button 
					color="primary" 
					disabled = { (mainTabsValue === 1 && !existingConfig.show) || objectToSend.current.name.replace(/\s/g,"") === "" ? true : false }
					onClick={() =>props.onSave(objectToSend.current)}
				>
					Сохранить
				</Button>
				<Button 
					color="primary" 
					onClick={() =>props.onCancel('closeConfigSaveDialog')}
				>
					Отменить
				</Button>
			</DialogActions>

      </Dialog>
  );
}