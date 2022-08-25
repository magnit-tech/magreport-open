import React, { useState } from 'react';

import { PivotCSS } from '../PivotCSS';

import { FormControlLabel, IconButton, List, ListItem, ListItemSecondaryAction, ListItemText, Switch, Tooltip, Typography } from '@material-ui/core';

//Icon-Component
import Icon from '@mdi/react'

//icons
import { mdiDeleteForever, mdiFileStar } from '@mdi/js';

export default function CustomList(props){
	const classes = PivotCSS();

	const [state, setState] = useState({
		checkedA: false,
		checkedB: true,
		checkedC: true,
	});

	const handleChange = (event) => {
		setState({ ...state, [event.target.name]: event.target.checked });
	};

	return (
		<List dense component="div" role="list">
			{props.items.map((item) => {
				const labelId = `item-${item.olapConfig.id}-label`;
				const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric' };

				return (
					<ListItem 
						button 
						role="listitem"
						key={item.olapConfig.id} 
						className={classes.CL_listItem}
					>
							<ListItemText 
								id={labelId}
								primary={item.olapConfig.name}
								secondary={
									<>
										<Typography
											component="span"
											variant="body2"
										>
											{new Date(item.modified).toLocaleString('ru', options)}
										</Typography>
										{" — "}
										<Typography
											component="span"
											variant="body2"
										>
											{item.user.name}
										</Typography>
									</>
								}
								onClick={(event) => props.chooseConfig(event, item)}   
							/>

						<ListItemSecondaryAction>
							<Tooltip title="Удалить"  placement='top'>
								<FormControlLabel 
									control = {
										<IconButton size="small" aria-label="delete" >
											<Icon path={mdiDeleteForever} size={1} />
										</IconButton>
									}
									onClick = {(event) => props.confirmDelete(event, item.olapConfig.id, item.olapConfig.name)}
								/>
							</Tooltip>
							{ item.report && 
								<Tooltip title={ item.isDefault ? 'По умолчанию' : 'Сделать по умолчанию' }  placement='top'>
									<FormControlLabel 
										control={
											<IconButton 
												size="small" 
												aria-label="delete" 
												color={item.isDefault ? 'secondary' : 'default'} 
												style={{ cursor: item.isDefault ? 'default' : 'pointer'}}
												onClick = {() => !item.isDefault ? props.chooseDefault(item) : false}
											>
												<Icon path={mdiFileStar} size={1} />
											</IconButton>
										}
									/>
								</Tooltip>
							}
							<FormControlLabel
								style={{margin: '0'}}
								value="top"
								label={
									<Typography style={{fontSize: '14px'}}>Общий доступ</Typography>	
								}
								labelPlacement="top"
								control = { <Switch disabled checked={state.checkedC} onChange={handleChange} name="checkedC" size="small"/> }
							/>
						</ListItemSecondaryAction>

					</ListItem>
				);
			})}
	  	</List>
	);
}