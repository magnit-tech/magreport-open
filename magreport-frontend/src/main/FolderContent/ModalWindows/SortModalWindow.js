import React, { useEffect, useRef, useState } from 'react'
import Button from '@material-ui/core/Button'
import ClickAwayListener from '@material-ui/core/ClickAwayListener'
import Grow from '@material-ui/core/Grow'
import Paper from '@material-ui/core/Paper'
import Popper from '@material-ui/core/Popper'
import MenuItem from '@material-ui/core/MenuItem'
import MenuList from '@material-ui/core/MenuList'
import { makeStyles } from '@material-ui/core/styles'
import { Divider, ListItemText, Menu } from '@material-ui/core'
import { Check } from '@material-ui/icons'

const useStyles = makeStyles(theme => ({
	root: {
		display: 'flex',
		
	},
	popper: {
		marginRight: '10px'
	},
}))

export default function SortModalWindow(props) {
	const classes = useStyles()
	const [open, setOpen] = useState(false)
	const anchorRef = useRef(null)
	const prevOpen = useRef(open)
	const [sortConfig, setSortConfig] = useState(Object.keys(props.sortParams).length !== 0 ? props.sortParams  : { key: 'name', direction: 'ascending' })

	useEffect(() => {
		if (prevOpen.current === true && open === false) {
			anchorRef.current.focus()
		}
		prevOpen.current = open
	}, [open])

	const handleToggle = () => {
		setOpen(prevOpen => !prevOpen)
	}

	const handleClose = event => {
		if (anchorRef.current && anchorRef.current.contains(event.target)) {
			return
		}

		setOpen(false)

		props.onClose()
	}

	const handleClickSortKey = (keyName) => {
		props.onSortClick({
            key: keyName,
            direction: sortConfig.direction
        });
		setSortConfig({...sortConfig, key: keyName});
		setOpen(false);
	}

	const handleClickSortDirection = (directionName) => {
		props.onSortClick({
            key: sortConfig.key,
            direction: directionName
        });
		setSortConfig({...sortConfig, direction: directionName});
		setOpen(false);
	}
	

	return (
		<div className={classes.root} >
			<Menu
				keepMounted
				open={props.open}
				onClose={handleClose}
				anchorReference="anchorPosition"
				anchorPosition={props.anchorPosition}
				PaperProps={{
                    style: {
                        overflowX: 'visible',
                        overflowY: 'visible'
                    },
                }}
			>
				<Button
					ref={anchorRef}
					aria-controls={open ? 'menu-list-grow' : undefined}
					aria-haspopup='true'
					onClick={handleToggle}
				>
					Сортировка
				</Button>
				<Popper
					open={open}
					anchorEl={anchorRef.current}
					role={undefined}
					transition
					disablePortal
					placement='left'
				>
					{({ TransitionProps, placement }) => (
						<Grow
							{...TransitionProps}
							style={{
								transformOrigin: placement === 'left' ? 'left' : 'right',
							}}
						>
							<Paper sx={{ width: 320 }} >
								<ClickAwayListener onClickAway={handleClose}>
									<MenuList dense>

										<MenuItem onClick={() => handleClickSortKey('name')}>
											{
												sortConfig.key === 'name' &&
												<Check style={{position: 'absolute'}}/>
											}
											<ListItemText inset>Имя</ListItemText>
										</MenuItem>

										<MenuItem onClick={() => handleClickSortKey('created')}>
											{
												sortConfig.key === 'created' &&
												<Check style={{position: 'absolute'}}/>
											}
											<ListItemText inset>Дата создания</ListItemText>
										</MenuItem>

										<MenuItem onClick={() => handleClickSortKey('modified')}>
											{
												sortConfig.key === 'modified' &&
												<Check style={{position: 'absolute'}}/>
											}
											<ListItemText inset>Дата изменения</ListItemText>
										</MenuItem>
									
										<Divider />

										<MenuItem onClick={() => handleClickSortDirection('ascending')}>
											{
												sortConfig.direction === 'ascending' &&
												<Check style={{position: 'absolute'}}/>
											}
											<ListItemText inset>По возрастанию</ListItemText>
										</MenuItem>

										<MenuItem onClick={() => handleClickSortDirection('descending')}>
											{
												sortConfig.direction === 'descending' &&
												<Check style={{position: 'absolute'}}/>
											}
											<ListItemText inset>По убыванию</ListItemText>
										</MenuItem>

									</MenuList>
								</ClickAwayListener>
							</Paper>
						</Grow>
					)}
				</Popper>
			</Menu>
		</div>
	)
}
