import React, {useState} from 'react';
import {useSnackbar} from "notistack";

import { SketchPicker } from 'react-color';
import { connect } from 'react-redux';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import * as colors from '@material-ui/core/colors';
import Card from '@material-ui/core/Card';
import TreeView from '@material-ui/lab/TreeView';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import TreeItem from '@material-ui/lab/TreeItem';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import {CircularProgress} from "@material-ui/core";
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
// or
import { cloneDeep } from 'lodash';

// dataHub
import dataHub from "ajax/DataHub";

//local
import { actionThemeDesignSetValue } from  '../../../../redux/actions/admin/actionThemeDesign';
import DataLoader from "main/DataLoader/DataLoader";
import PageTabs from "main/PageTabs/PageTabs";
import DesignerTextField from "main/Main/Development/Designer/DesignerTextField";

// styles
import {ThemeDesignerCSS, StickyTableCell} from './ThemeDesignerCSS';
import DesignerPage from "main/Main/Development/Designer/DesignerPage";


function ThemeDesigner(props){
    const classes = ThemeDesignerCSS();
    const shades = [0, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900];
    const aShades = [100, 200, 400, 700];
    const hues =  Object.keys(colors).slice(1);
    const [paletteProperty, setPaletteProperty] = useState({});

    console.log('props/theme');
    console.log(JSON.stringify(props.theme, null, '\t'));
    
    function changeThemeValue(fullTheme, key, value) {
        if (key) {
            let rgbaOrHex = value.r ? "rgba(" + value.r + "," + value.g + "," + value.b + "," +value.a +")" : value;
            let newTheme = cloneDeep(fullTheme);
            let selectedColor = newTheme;

            for (let i = 0; i < key.length - 1; i++) {
                selectedColor = selectedColor[key[i]];
            }

            selectedColor[key[key.length-1]] = rgbaOrHex;
            props.actionThemeDesignSetValue(newTheme)
            setPaletteProperty({path: paletteProperty.path, value: rgbaOrHex});
            setThemeData({...themeData, data: newTheme});

            console.log('newTheme');
            console.log(newTheme);
            
        }
    }

    const renderTree = (nodes) => (
        <TreeItem 
            key={nodes.id} 
            nodeId={nodes.id} 
            label={ (nodes.value &&
                <div style={{display: 'flex', alignItems: 'center'}}> 
                    <Typography style={{margin: '0px 8px'}}> {nodes.name}</Typography>
                        <Button 
                            variant="outlined"
                            className={classes.colorTreeBtn} 
                            style={{backgroundColor: `${nodes.value ? nodes.value: 'inherit'}`}} 
                            onClick={()=>setPaletteProperty({path: nodes.path, value: nodes.value ? nodes.value: 'inherit'})}
                        />
                </div>) || nodes.name 
            }
        >
            {Array.isArray(nodes.children) ? nodes.children.map((node) => renderTree(node)) : null}
        </TreeItem>
      );

    function getPaletteTree (obj, li, path) {
        let list = li;
        let detailPath=path;
        let i = 0;
        if (typeof obj === 'object') {
            list.children = [];
            for (let key in obj) {
                    list.children.push({id: detailPath+'/'+key, name: key});  
                    getPaletteTree(obj[key], list.children[i], detailPath+'/'+key);
                    i+=1;                
                }
        }
        else if(typeof obj === 'string'){
            list.children = [];
            list.children.push({id: obj, name: obj, value: obj, path: path.split('/')})
        }
        else {
            
        };

        if (list.id ==='palette') {
            return list
        }
          
    };   
    
    //////////////////////////////
    const {enqueueSnackbar} = useSnackbar();
    const pageName = props.mode === "create" ? "Создание цветовой схемы" : "Редактирование цветовой схемы";

    const [uploading, setUploading] = useState(false);
    const [id, setId] = useState(props.themeId);
    const [typeId, setTypeId] = useState('0');
    const [errorField, setErrorField] = useState({});


    const [themeData, setThemeData] = useState({
        data: props.theme,
        description: null,
        favorite: false,
        id: null,
        name: null,
        typeId: 0,
        userId: 5 //null
    });

    let data = getPaletteTree(props.mode === 'create' ? props.palette : themeData.data.palette, {id: 'palette', name: 'palette'}, 'palette');

    console.log('props.palette');
    console.log(props.palette);

    console.log('themeData.data');
    console.log(themeData.data);

    const FieldNames = new Map([      
		["name"             , "Наименование"    ],
		["description"      , "Описание"        ],
        ["typeId"           , "Тип схемы"       ],
        ["favorite"         , "Избранное"       ],
        ["data"             , "Палитра"         ],
        ["userId"           , "Пользователь"    ]
    ]); 

    let loadFunc;
    let loadParams = [];

    console.log(props.mode)

    if (props.mode === "edit") {
        loadFunc = dataHub.themeController.get;
        loadParams = [id];
    }

    function hasErrors() {

        let errors = {};
        let errorExists = false;
        console.log('hasErrors');
        console.log(themeData);

        Object.entries(themeData)
            .filter( ([fieldName, fieldValue]) => 
                (   
                    (   fieldName === "name" || 
                        fieldName === "description" ||
                        fieldName === "typeId" ||
                        fieldName === "data" ||
                        fieldName === "favorite"
                    ) &&
                    (   fieldValue === null || 
                        (typeof fieldValue === "string" && fieldValue.trim() === "") ||
                        (typeof fieldValue === "object" && fieldValue.length === 0)
                    ) 
                ))
            .reverse()
            .forEach( ([fieldName, fieldValue]) => 
                {
                    errors[fieldName] = true;
                    enqueueSnackbar("Недопустимо пустое значение в поле " + FieldNames.get(fieldName), {variant : "error"});
                    errorExists = true;
                } );

            if(errorExists){
                setErrorField(errors);
            }
            return errorExists
    }

    function handleDataLoaded(loadedData) {
        setId(loadedData.id);
        setTypeId(loadedData.type === 'WHITE' ? '0': '1');
        setThemeData({
            data: JSON.parse(loadedData.data),
            description: loadedData.description, 
            favorite: loadedData.favorite, 
            id: loadedData.id, 
            name: loadedData.name, 
            typeId: loadedData.type === 'WHITE' ? 0: 1 , //loadedData.typeId, 
            userId: loadedData.user.id
        })
        console.log('handleDataLoaded');
        console.log(loadedData)
    }

    function handleSave() {
        console.log('themeData');
        console.log(themeData);
       // debugger;
        if(hasErrors()) {
            //enqueueSnackbar(`Форма содержит ошибки`, {variant: "error"});
        } else if (props.mode === "create") {
            console.log('handleSave: themeData.data');
            console.log(themeData.data);

            dataHub.themeController.add(
                JSON.stringify(themeData.data), 
                themeData.description, 
                themeData.favorite, 
                themeData.id,
                themeData.name, 
                themeData.typeId, 
                themeData.userId,  
                magResponse => handleAddedEdited(magResponse)
            );
            setUploading(true);
        } else {
            dataHub.themeController.edit(
                JSON.stringify(themeData.data), 
                themeData.description, 
                themeData.favorite, 
                themeData.id, 
                themeData.name, 
                themeData.typeId, 
                themeData.userId,  
                magRepResponse => handleAddedEdited(magRepResponse)
            );
            setUploading(true);
        }
    }

    function handleAddedEdited(magRepResponse) {
        
        if (magRepResponse.ok) {
            props.onExit();
            enqueueSnackbar("Тема успешно сохранена", {variant : "success"});
        } else {
            setUploading(false);
            const actionWord = props.mode === "create" ? "создании" : "обновлении";
            enqueueSnackbar(`При ${actionWord} возникла ошибка: ${magRepResponse.data}`,
                {variant: "error"});
        }
    }

    function handleChangeThemeMain(name, value) {
        setThemeData({...themeData, [name]: value});
        setErrorField({...errorField, [name]: false});
        console.log('handleChangeThemeMain: themeData');
        console.log(themeData);
    }

    function handleChangeTypeId(value) {
        console.log('handleChangeTypeId');
        console.log(value);
        setTypeId(value);
        setThemeData({...themeData,   typeId: parseInt(value)});
        setErrorField({...errorField, typeId: false});
    }

    // building component
    const tabs = [];

    // general
    tabs.push({
        tablabel: "Общие",
        tabcontent: uploading ? <CircularProgress/> : 
        <DesignerPage
            onSaveClick={handleSave}
            onCancelClick={()=> props.onExit()}
        >
            <DesignerTextField
                label="Название"
                value={themeData.name}
                onChange={(value) => handleChangeThemeMain('name', value)}
                displayBlock
                fullWidth
                error={errorField.name}
            />
            <DesignerTextField
                label="Описание"
                value={themeData.description}
                onChange={(value) => handleChangeThemeMain('description', value)}
                displayBlock
                fullWidth
                error={errorField.description}
            />
            <FormControl component="fieldset" style={{margin: '8px 0px'}}>
                    <FormLabel component="legend">Тип:</FormLabel>
                        <RadioGroup row aria-label="themeType" name="themeTypeId" 
                            value={typeId} 
                            defaultValue = {typeId}
                            onChange={(event) => handleChangeTypeId( event.target.value)}
                        >
                            <FormControlLabel value= '0' control={<Radio />} label="Светлая" />
                            <FormControlLabel value= '1' control={<Radio />} label="Тёмная" />
                        </RadioGroup>
                </FormControl>
        </DesignerPage>
    })

    tabs.push({
        tablabel: "Палитра",
        tabcontent: uploading ? <CircularProgress/> : 
        <DesignerPage
            onSaveClick={handleSave}
            onCancelClick={()=> props.onExit()}
        > 
        <Grid 
            className={classes.rootGrid}
            container 
            justifyContent="center"
            wrap="nowrap"
        > 
            <Grid item key={1} xs={3} className={classes.flexTreeGridItem}> 
                <Card elevation = {5} className={classes.treeRelative}> 
                    <AppBar className={classes.appBar}> 
                        <Toolbar style={{minHeight: '40px'}}>
                            <Typography> Цветовая схема </Typography>
                        </Toolbar>
                    </AppBar>
                    <TreeView id='container' className={classes.posAbs}
                        defaultCollapseIcon={<ExpandMoreIcon />}
                        defaultExpanded={['palette']}
                        defaultExpandIcon={<ChevronRightIcon />}
                    >
                        {renderTree(data)}

                    </TreeView>
                </Card>
            </Grid>
            <Grid item key={2} xs={3}  className={classes.flexScetchGridItem}> 
                <div className={classes.sketchRelative}> 
                    <div className={classes.posAbsColor}> 
                        <SketchPicker 
                            color={paletteProperty.value}
                            onChangeComplete={(color)=> changeThemeValue(themeData.data, paletteProperty.path, color.rgb)}
                        >
                            
                        </SketchPicker>
                    </div>
                </div>
            </Grid>
            <Grid item key={3} xs={6}  className={classes.flexColorsGridItem}> 
                <Card elevation = {5} className={classes.coloTableRelative}>   
                    <TableContainer className={classes.posAbs0}>
                        <Table stickyHeader size="small">
                            <TableHead >
                                <TableRow style={{height: '40px'}}>
                                    {shades.map((shade)=>(
                                        (shade === 0) ?
                                            <StickyTableCell  key={0} className={classes.head}>
                                                Цвет
                                            </StickyTableCell>
                                        :
                                            <TableCell key={shade} className={classes.colorCellHead}>
                                                {shade}
                                            </TableCell>
                                    ))}
                                {aShades.map((shade)=>(
                                    <TableCell key={"A" + shade} className={classes.colorCellHead}>
                                        {"A" + shade}
                                    </TableCell>
                                ))}
                                </TableRow>
                   
                            </TableHead>
                            <TableBody>
                                {hues.map((hue)=>(
                                    <TableRow key={hue}>
                                        {shades.map((shade)=>(
                                            (shade === 0) ?
                                                <StickyTableCell className={classes.head} key={hue}> 
                                                    {hue} 
                                                </StickyTableCell>
                                                :
                                                <TableCell  className={classes.colorCell}  key={colors[hue][shade]}>
                                                    <Button
                                                        className={classes.colorBtn}
                                                        style={{backgroundColor: colors[hue][shade]}}
                                                        onClick={()=> changeThemeValue(themeData.data, paletteProperty.path, colors[hue][shade])}
                                                    />
                                                </TableCell>
                                              
                                        ))}
                                        {aShades.map((shade)=>(
                                            <TableCell 
                                                className={classes.colorCell} 
                                                key = {colors[hue]["A" + shade]}
                                            >
                                                <Button 
                                                    className={classes.colorBtn}
                                                    style={{backgroundColor: colors[hue]["A" + shade]}}
                                                    onClick={()=> changeThemeValue(themeData.data, paletteProperty.path,colors[hue]["A" + shade])}//props.actionThemeDesignSetValue(1, colors[hue]["A" + shade])}
                                                >
                                
                                                </Button>
                                            </TableCell>
                                            
                                        ))}
                                    
                                    </TableRow>
                                
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Card>
            </Grid>
        </Grid>
        </DesignerPage>
    })

    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={loadParams}
            onDataLoaded={handleDataLoaded}
            onDataLoadFailed={(message) => enqueueSnackbar(`При получении данных возникла ошибка: ${message}`, {variant: "error"})}
        >
            <PageTabs
                tabsdata={tabs}
                pageName={pageName}
            />
        </DataLoader>
    )
}

const mapStateToProps = state => {
    return {
        theme: state.themesMenuView.theme,
        palette: state.themesMenuView.theme.palette
    }
}

const mapDispatchToProps = {
    actionThemeDesignSetValue
}

export default connect(mapStateToProps, mapDispatchToProps)(ThemeDesigner);