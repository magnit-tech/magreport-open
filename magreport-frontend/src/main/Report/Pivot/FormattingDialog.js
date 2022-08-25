import React, { useState } from 'react';
import { PivotCSS } from './PivotCSS';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogTitle from '@material-ui/core/DialogTitle';
import Paper from '@material-ui/core/Paper';
import Draggable from 'react-draggable';
import {TextField} from "@material-ui/core";
import Grid from '@material-ui/core/Grid';

import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Box from '@material-ui/core/Box';

//Перетаскивание модального окна
function PaperComponent(props) {
    return (
		<Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
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
				<Box style={{padding: props.rightcontent ? '0' : '24px 0'}}>
					{children}
				</Box>
			)}
		</div>
	);
}

export default function FormattingDialog(props){
	const classes = PivotCSS();

	let format = {...props.format}

	const [mainTabsValue, setMainTabsValue] = useState(0);
	const [valueForNumberTabs, setValueForNumberTabs] = useState(0);
	const [inputNumber, setInputNumber] = useState(format.style?.rounding || 0);
	const [exampleRoundingNumber, setExampleRoundingNumber] = useState(5);
	
	// Переключение главных табов
	const handleChangeMainTabs = (event, newValue) => {
		setMainTabsValue(newValue);
	};

	// Переключение табов на вкладке "Цифры"
	const handleChangeNumberTabs = (event, newValue) => {
		setValueForNumberTabs(newValue);
	};

	// Изменение образца на вкладке "Цифры"
	const handleChangeExpamleNumber = (e) => {
		let number = Number(e.target.value)
		if (number >= 0) {
			format.style.rounding = number;
			setInputNumber(number)
		}
	}

  	return (
        <Dialog
            open={props.open}
            PaperComponent={PaperComponent}
            aria-labelledby="draggable-dialog-title"
        >
            <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
                Форматирование
            </DialogTitle>

			<div className={classes.FD_root}>

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
						<Tab label="Цифры" />
						<Tab label="Выравнивание" />
						<Tab label="Шрифт" />
					</Tabs>
				</AppBar>
				
				<TabPanel value={mainTabsValue} index={0}>
					<Grid container>
						<Grid item xs={4}>
							{/* <p>Числовые форматы:</p> */}
							<Tabs
									orientation="vertical"
									variant="scrollable"
									value={valueForNumberTabs}
									onChange={handleChangeNumberTabs}
									aria-label="Vertical tabs example"
									className={classes.FD_tabs}
								>
									<Tab label="Числовой" />
									<Tab label="Процентный" />
							</Tabs>
						</Grid>

						<Grid item xs={8}>
							<TabPanel 
								value={valueForNumberTabs} 
								index={0} 
								rightcontent="true"
								className={classes.FD_rightContent}
							>
								<TextField
									id="outlined-read-only-input"
									label="Образец:"
									value={exampleRoundingNumber.toFixed(inputNumber)}
									InputProps={{
										readOnly: true,
									}}
									variant="outlined"
								/>
								<Box display="flex" className={classes.FD_wrapperForInputNumber}>
									<Box whiteSpace="nowrap">Число десятичных знаков:</Box>
									<input 
										type='number' 
										className={classes.FD_inputForNumber}
										value={inputNumber} 
										onChange={(e) => handleChangeExpamleNumber(e)}
									/>
								</Box>
							</TabPanel>

							<TabPanel 
								value={valueForNumberTabs} 
								index={1} 
								rightcontent="true"
								className={classes.FD_rightContent}
							>
								Item Two
							</TabPanel>

						</Grid>
					</Grid>
				</TabPanel>

				<TabPanel value={mainTabsValue} index={1}>
					Item Two
				</TabPanel>

				<TabPanel value={mainTabsValue} index={2}>
					Item Three
				</TabPanel>

			</div>

			<DialogActions>
				<Button color="primary" onClick={() =>props.updateData(format)}>
					Сохранить
				</Button>
				<Button autoFocus color="primary" onClick={() =>props.onCancel(false)}>
					Отменить
				</Button>
			</DialogActions>

      </Dialog>
  );
}