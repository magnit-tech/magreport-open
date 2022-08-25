import React from 'react'

import {DesignerCSS} from './DesignerCSS';
import Button from '@material-ui/core/Button';
import AppBar from '@material-ui/core/AppBar';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Toolbar from '@material-ui/core/Toolbar';
import clsx from 'clsx'

export default function DesignerPage(props){

    const classes = DesignerCSS();

    return (
        <div className= {classes.designerPage} style={{overflow: 'hidden'}}>
           {props.name ?
                <AppBar position="static" className={classes.pageTitle} >
                    <Toolbar variant="dense" >
                        <Typography variant="h6">
                            {props.name}
                        </Typography> 
                    </Toolbar>
                </AppBar>
            :
                ""
            }
            <div className={classes.designerPageChildren}>
                <div className={clsx(classes.designerPageAbsolute, {[classes.designerPageWOPadding]: props.disabledPadding})}>
                    <div className={classes.designerPage}>
                    {props.children}
                    </div>
                </div>
            </div>
            { (props.onSaveClick ||  props.onCancelClick) &&
                <Paper elevation={3} className={classes.pageBtnPanel}>
                    {props.onSaveClick ?
                        <Button
                            className={classes.pageBtn}
                            type="submit"
                            variant="contained"
                            size="small"
                            color = "primary"
                            onClick={props.onSaveClick}
                            disabled={props.disableSave}
                        >
                            <Typography noWrap variant = 'button'>
                                {props.saveName ? props.saveName : 'Сохранить'}
                            </Typography>
                        </Button> 
                        : null
                    }
                    {props.onCancelClick ? 
                        <Button
                            className={classes.pageBtn} 
                            type="submit" 
                            variant="contained" 
                            size="small" 
                            color = "primary" 
                            onClick={props.onCancelClick}
                        >
                            <Typography noWrap variant = 'button'>
                                {props.cancelName ? props.cancelName : 'Отменить'}
                            </Typography>
                        </Button> 
                        : null
                    }
                
                </Paper>
            }
        </div>
    )
}
