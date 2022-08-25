import React from 'react'
import {useState, useRef} from 'react';
import { CircularProgress, Typography } from '@material-ui/core';
import clsx from 'clsx';

import {DataLoaderCSS} from './DataLoaderCSS';

/**
 * Загрузчик данных для дочерних компонентов
 * @param {*} props.loadFunc - function(magrepResponse) return requestId - функция загрузки данных. Может отсутствовать, в этом случае данные считаются
 *                              уже загруженными
 * @param {Array} props.loadParams - массив параметров функции loadFunc
 * @param {boolean} props.reload - объект {needReload} - выполнять ли загрузку при ререндере при совпадающем с прошлой загрузкой значении параметров
 *                                  если пристствует и needReload === true - выполняется загрузка при совпадающем с прошлой загрузкой значении параметра
 *                                  при этом свойство needReload объекта меняется на false
 * @param {*} props.onDataLoaded - function(data) - функция обработки успешной загрузки данных
 * @param {*} props.onDataLoadFailed - function(message) - функция обработки неуспешной загрузки данных
 * @param {*} props.loadFailedElement - элемент, выводящийся при неуспешной загрузке данных
 * @param {*} props.showSpinner - показывать ли спинер
 * @param {*} props.disabledScroll - убрать overflow: auto
 */
 
export default function DataLoader(props){
    const classes = DataLoaderCSS();

    // loadState : notLoaded, loading, loaded, loadFailed
    const [loadState, setLoadState] = useState('notLoaded');
    const lastLoadParams = useRef(null);
    const errLoadMessage = useRef("Ошибка загрузки данных");
    const currentLoadRequestId = useRef(null);

    function arrayEquals(A, B){
        return A.length === B.length && A.every((val, ind) => val === B[ind]);
    }

    function handleDataLoaded(magrepResponse){
        if(magrepResponse.requestId === currentLoadRequestId.current){
            if(magrepResponse.ok){
                if(props.onDataLoaded){
                    props.onDataLoaded(magrepResponse.data);
                    setLoadState('loaded');
                }
            }
            else{
                if(props.onDataLoadFailed){
                    props.onDataLoadFailed(magrepResponse.data);
                }
                errLoadMessage.current = magrepResponse.data;
                setLoadState('loadFailed');                
            }
        }
    }

    function startLoad(){
        lastLoadParams.current = props.loadParams.slice();
        if(props.reload){
            props.reload.needReload = false;
        }
        if(props.loadFunc){
            currentLoadRequestId.current = props.loadFunc(...props.loadParams, handleDataLoaded);
            if(loadState !== 'loading'){
                setLoadState('loading');
            }
        }
        else{
            setLoadState('loaded');
        }
    }

    let needLoad = props.loadParams && 
                    ( 
                      (lastLoadParams.current === null || ! arrayEquals(lastLoadParams.current, props.loadParams) ) 
                      ||
                      (props.reload && props.reload.needReload)
                    );

    if(needLoad){
        startLoad();
    }

    return (
        <div  className={clsx(classes.dataLoaderRoot, {[classes.dataLoaderRootWOScroll]: props.disabledScroll})}>
        {
            loadState === 'notLoaded' ?
                <div></div>  
            : loadState === 'loading' && (props.showSpinner === undefined || props.showSpinner === true)? 
                <div className={classes.dataLoaderProgressDiv}><CircularProgress className={classes.dataLoaderProgress}/></div>
            : loadState === 'loadFailed' ?
                (props.loadFailedElement ? props.loadFailedElement : <Typography>{errLoadMessage.current}</Typography>)
            : props.children 
        }
        </div>
    );

}