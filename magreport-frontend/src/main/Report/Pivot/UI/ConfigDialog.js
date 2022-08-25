import React, { useRef } from 'react';
import { connect } from 'react-redux';

import { PivotCSS } from '../PivotCSS';

import Draggable from 'react-draggable';

import { Paper, Dialog, DialogTitle, DialogActions, Button, Box, Typography} from '@material-ui/core';

import CustomList from './CustomList';

//actions
import { showAlertDialog, hideAlertDialog } from 'redux/actions/actionsAlertDialog'

//Перетаскивание модального окна
function PaperComponent(props) {
    return (
		<Draggable handle="#drag-title" cancel={'[class*="MuiDialogContent-root"]'}>
			<Paper {...props}/>
		</Draggable>
    );
}

function ConfigDialog(props){
	const classes = PivotCSS();

	const availableConfigs = props.configs
	const isHaveAvailableConfigs = useRef(availableConfigs.myJobConfig.length !== 0 || availableConfigs.myReportConfigs.length !== 0);
	const isHaveAllAvailableConfigs = useRef(availableConfigs.myJobConfig.length !== 0 && availableConfigs.myReportConfigs.length !== 0);

	const deleteItem = useRef(null);
	const chosenConfig = useRef(null);

	// Подтверждение удаления конфигурации
	const handleClickDelete = (event, id, name) => {
        event.stopPropagation();
		
		deleteItem.current = {
			id,
			name,
			type: 'ConfigDialog'
		}

        props.showAlertDialog('Удалить конфигурацию "' + name + '" ?', null, null, handleConfirmDelete)
    }
	function handleConfirmDelete(answer){
        if (answer){
            props.onDelete(deleteItem.current)
        }
        props.hideAlertDialog()
    }

	// Подтверждение выбора конфигурации
	const handleClickChooseConfig = (event, item) => {
        event.stopPropagation();

		chosenConfig.current = {
			data: item.olapConfig.data,
			name: item.olapConfig.name,
			reportOlapConfigId: item.reportOlapConfigId
		}

        props.showAlertDialog('Загрузить конфигурацию "' + item.olapConfig.name + '" ?', null, null, handleConfirmChooseConfig)
    }
	function handleConfirmChooseConfig(answer){
        if (answer){
            props.onChooseConfig(chosenConfig.current)
        }
        props.hideAlertDialog()
    }

  	return (
        <Dialog
            open={props.open}
            PaperComponent={PaperComponent}
            aria-labelledby="drag-title"
        >
			<Box style={{ width: isHaveAvailableConfigs.current ? '570px' : '350px' , overflow: 'hidden'}}>
				<DialogTitle className={classes.dialogTitle} id="drag-title"> Доступные конфигурации </DialogTitle>

				<div className={classes.CD_root}>
					{ isHaveAvailableConfigs.current? 
						<>
							{
								availableConfigs.myJobConfig.length > 0 &&
								<Box>
									<Typography className={classes.CD_subtitle}>Конфигурации задания:</Typography>
									<Box style={{ height: availableConfigs.myJobConfig.length < 5 ? 'auto' : '300px', overflowY: 'auto'}}>
										<CustomList 
											items = {availableConfigs.myJobConfig}
											confirmDelete = {(event, id, name) => handleClickDelete(event, id, name)}
											chooseConfig = {(event, item) => handleClickChooseConfig(event, item)}
										/>
									</Box>
								</Box>
							}
							{
								availableConfigs.myReportConfigs.length > 0 &&
								<Box style={{ marginTop: isHaveAllAvailableConfigs ? '30px' : '0'}}>
									<Typography className={classes.CD_subtitle}>Конфигурации отчета:</Typography>
									<Box style={{ height: availableConfigs.myReportConfigs.length < 5 ? 'auto' : '300px', overflowY: 'auto'}}>
										<CustomList 
											items = {availableConfigs.myReportConfigs}
											confirmDelete = {(event, id, name) => handleClickDelete(event, id, name)}
											chooseConfig = {(event, item) => handleClickChooseConfig(event, item)}
											chooseDefault = {(item) => props.onMakeDefault(item)}
										/>
									</Box>
								</Box>
							}
						</>
						:
							<Box className={classes.CSD_wrapperList} textAlign="center">
								<Typography>Нет доступных конфигураций</Typography>
							</Box>
					}
				</div>

				<DialogActions>
					{/* <Button 
						color="primary" 
						disabled={ availableConfigs.length > 0 ? false : true}
					>
						Сохранить
					</Button> */}
					<Button 
						color="primary" 
						onClick={() =>props.onCancel('closeConfigDialog')}
					>
						Отменить
					</Button>
				</DialogActions>
			</Box>
      </Dialog>
  );
}

const mapDispatchToProps = {
    showAlertDialog, 
    hideAlertDialog,
}

export default connect(null, mapDispatchToProps)(ConfigDialog);