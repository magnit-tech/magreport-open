import React, {useState} from 'react';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { CircularProgress } from '@material-ui/core';
import dataHub from '../../ajax/DataHub';

function AsyncAutocomplete(props){

    const [entityToAddList, setEntityToAddList] = useState([]);
    const [openAsyncEntity, setOpenAsyncEntity] = React.useState(false);
    const [optionsAsyncEntity, setOptionsAsyncEntity] = React.useState([]);

    const loadingAsync = openAsyncEntity && optionsAsyncEntity.length === 0;

    function handleOnInputChange(e, value){
        if ((value.length >= 3) && (props.typeOfEntity === "domainGroup")) { dataHub.adController.getDomainGroups (0, value, handleDomainGroups); }
    }

    function handleDomainGroups(magrepRespone){
        if(magrepRespone.ok){         
            setOptionsAsyncEntity(magrepRespone.data);
            let entityArr = [];
            magrepRespone.data.forEach( (elem) => { entityArr.push({'name':elem})} )

            setEntityToAddList(entityArr);
        }
    }

    function handleOnChange(e, value){
        let entity;
        for (let i of entityToAddList){
            if (i.name === value){
                entity = i;
            };
        };
        props.onChange(entity);
    }

    React.useEffect(() => {
            let active = true;
            if (!loadingAsync) {
                return undefined;
            }

            (async () => {
                let entity = [];
                if (entityToAddList.length === 0){
                    if (props.typeOfEntity === "user"){
                        dataHub.userController.users(handleUsers);
                    }
                    else if(props.typeOfEntity === "role"){
                        dataHub.roleController.getAll(handleRoles);
                    }
                    else if(props.typeOfEntity === "domainGroup"){
                    }

                    function handleRoles(magrepResponse){
                        sortEntity(magrepResponse)
                    }

                    function handleUsers(magrepResponse){
                        sortEntity(magrepResponse)
                    }

                    function sortEntity(magrepResponse){
                        if (magrepResponse.ok && active){
                            let entityTmp = magrepResponse.data.filter(props.filterOfEntity ? props.filterOfEntity : ()=>{return true}).sort(
                                function (a, b) {
                                    if (a.name < b.name) {
                                        return -1;
                                    }
                                    if (a.name > b.name) {
                                        return 1;
                                    }
                                    return 0;
                                }
                            );

                            console.log('entityTmp');
                            console.log(entityTmp);

                            for (let i of entityTmp){
                                entity.push(i.name);
                            };

                            

                            setEntityToAddList(entityTmp);
                        }
                    };

                }
                else {
                    for (let i of entityToAddList){
                        entity.push(i.name);
                    };
                };
                setOptionsAsyncEntity(entity);
            })();
            
            return () => {
                active = false;
            };
        }, [loadingAsync] // eslint-disable-line
    );

    React.useEffect(() => {
        if (!openAsyncEntity) {
            setOptionsAsyncEntity([]);
        }
        }, [openAsyncEntity]
    );


    return (
            <Autocomplete
                id="asynchronousRoleListToAdd"
                key={props.resetAutocomplete}
                disabled = {props.disabled}
                open={openAsyncEntity}
                onOpen={() => {
                    setOpenAsyncEntity(true);
                }}
                onClose={() => {
                    setOpenAsyncEntity(false);
                }}
                getOptionSelected={(option, value) => option.name === value.name}
                options={optionsAsyncEntity}
                loading={loadingAsync}
                onChange={handleOnChange}
                onInputChange={handleOnInputChange}
                renderInput={params => (
                    <TextField
                    {...params}
                    label={props.typeOfEntity === "user" ? "Пользователи": (props.typeOfEntity === "domainGroup" ? "Доменные группы" : "")}
                    fullWidth
                    variant="outlined"
                    InputProps={{
                        ...params.InputProps,
                        endAdornment: (
                        <React.Fragment>
                            {loadingAsync ? <CircularProgress color="inherit" size={20} /> : null}
                            {params.InputProps.endAdornment}
                        </React.Fragment>
                        ),
                    }}
                    />
                )}
            />
        );
    }

export default AsyncAutocomplete;   