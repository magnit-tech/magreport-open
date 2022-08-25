import React, {useState, useEffect} from 'react';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { CircularProgress } from '@material-ui/core';
// dataHub
import dataHub from 'ajax/DataHub';

function RolesAutocomplete(props){

    const [entityToAddList, setEntityToAddList] = useState([]);
    const [openAsyncEntity, setOpenAsyncEntity] = React.useState(false);
    const [optionsAsyncEntity, setOptionsAsyncEntity] = React.useState([]);

    const loadingAsync = openAsyncEntity && optionsAsyncEntity.length === 0;

    function handleOnChange(e, value){
        let entity=null;
        for (let i of entityToAddList){
            if (i.name === value){
                entity = i.id;
            };
        };
        props.onChange(entity);
    }

    useEffect(() => {
            let active = true;
            if (!loadingAsync) {
                return undefined;
            }

            (async () => {
                let entity = [];
                if (entityToAddList.length === 0){
                    dataHub.roleController.getType(null, handleRoleTypes)

                    function handleRoleTypes(magrepResponse){
                        if (magrepResponse.ok && active){
                            let entityTmp = magrepResponse.data.childTypes.sort((a, b) => (a.name < b.name) ? -1 : (a.name > b.name) ? 1 : 0);
                            entityTmp.forEach(i => entity.push(i.name))
                            setEntityToAddList(entityTmp);
                        }
                    }
                }
                else {
                    entityToAddList.forEach(i => entity.push(i.name))
                };
                setOptionsAsyncEntity(entity);
            })();
            
            return () => {
                active = false;
            };
        }, [loadingAsync] // eslint-disable-line
    );

    useEffect(() => {
        if (!openAsyncEntity) {
            setOptionsAsyncEntity([]);
        }
        }, [openAsyncEntity]
    );


    return (
            <Autocomplete
                id="asynchronousRoleListToAdd"
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
                fullWidth
                renderInput={params => (
                    <TextField
                    {...params}
                    label="Тип роли"
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

export default RolesAutocomplete;   