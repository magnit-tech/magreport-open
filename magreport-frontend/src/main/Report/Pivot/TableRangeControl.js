import React, { useState, useEffect } from 'react';
import clsx from 'clsx';

import Slider from '@material-ui/core/Slider';
import ArrowLeft from '@material-ui/icons/ArrowLeft';
import ArrowRight from '@material-ui/icons/ArrowRight';
import ArrowDropUp from '@material-ui/icons/ArrowDropUp';
import ArrowDropDown from '@material-ui/icons/ArrowDropDown';
import IconButton from '@material-ui/core/IconButton';
import Box from '@material-ui/core/Box';
import {PivotCSS} from './PivotCSS';

/**
 * 
 * @param {*} props.orientation - vertical or horizontal
 * @param {*} props.total - total rows or columns
 * @param {*} props.position - current from row or column
 * @param {*} props.count - current window width (count of rows or columns shown)
 * @param {*} props.onPositionChange - position change handler
 */

export default function TableRangeControl(props) {

    let maxValue = Math.max(1, props.total - props.count);

    function positionToValue(position){
        return props.orientation === "horizontal" ?
            position :
            maxValue - position;
    }

    function valueToPosition(value) {
        return props.orientation === "horizontal" ?
            value :
            maxValue - value;
    }

    const styles = PivotCSS()

    const [innerValue, setInnerValue] = useState(positionToValue(props.position));

    const disabled = props.total <= props.count;

    useEffect(()=>{
        setInnerValue(positionToValue(props.position));
    }, [props.position, props.total]); // eslint-disable-line

    function handleArrowClick(inc){
        //setInnerValue(innerValue + inc);
        props.onPositionChange(props.position + inc);
    }
  
    return (
        <div 
            className={clsx({
                [styles.rangeMainHorizontal]: props.orientation === "horizontal",
                [styles.rangeMainVertical]: props.orientation === "vertical"
            })}
        >  
            <IconButton 
                className={styles.rangeBtn}
                color = 'primary' 
                size = 'small'
                onClick = {() => {handleArrowClick(-1)}} 
                disabled = {disabled || props.position === 0} 
            >
                {props.orientation === "horizontal" ? <ArrowLeft/> : <ArrowDropUp/>}
            </IconButton>

            <Box 
                style={{textAlign: 'center', marginRight: props.orientation === "horizontal" ? '20px': 0}}
                fontSize={10} 
                fontWeight={"fontWeightMedium"} 
            >
                {props.position}        
            </Box>
               
            <Slider
                classes={{
                    root: styles.sliderRoot,
                    thumb: styles.thumb,
                    rail: styles.rail
                }}
                track={false}
                aria-label="pretto slider"
                orientation = {props.orientation}
                value={innerValue}
                onChange = {(event, newValue) => {setInnerValue(newValue)}}
                onChangeCommitted={(event, newValue) => {props.onPositionChange(valueToPosition(newValue))}} 
                disabled = {disabled}
                scale = {valueToPosition}
                max = {maxValue}
            />
            <Box 
                style={{textAlign: 'center', marginLeft: props.orientation === "horizontal"?  '20px': 0}}
                fontSize={10} 
                fontWeight={"fontWeightMedium"} 
            >
                <p className={styles.rangeP}>{(Math.min(props.position + props.count, props.total))} </p>
                <p className={styles.rangeP}>  из {props.total} </p>    
            </Box>
                
            <IconButton 
                className={styles.rangeBtn}
                color = 'primary' 
                size = 'small'
                onClick = {() => {handleArrowClick(1)}} 
                disabled = {disabled || props.position===props.total}
            >
                {props.orientation === "horizontal" ? <ArrowRight /> : <ArrowDropDown />}
            </IconButton>
        </div>
    )

}