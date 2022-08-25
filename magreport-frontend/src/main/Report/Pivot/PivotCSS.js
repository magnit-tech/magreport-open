import { withStyles, makeStyles } from '@material-ui/core/styles';

const gridWidth = '100%';
const gridHeight = '100%';

export const FieldForDrag =  withStyles({

})

export const PivotCSS = makeStyles(theme => ({
    verticalList: {
        margin : '2px',
        border : '1px solid',
        borderColor: theme.palette.divider,
        borderRadius : '2px',
        padding: '2px'
    },
    verticalListTableCell: {
        verticalAlign : 'top',
        maxWidth: '100%', 
        display: 'grid', 
        //gridTemplateColumns: '1fr 1fr 48px'
    },
    horRel: {
        position: 'relative',
         minHeight: '10px', 
         maxHeight: '68px',
         height: '100px'
    },
    horizontalList: {
        margin : '2px',
        border : '1px solid',
        borderColor: theme.palette.divider,
        borderRadius : '2px',
        padding: '2px',
        overflow: 'auto',
        maxHeight: '72px'
    },
    listDraggingOver:{
        backgroundColor : theme.palette.action.hover
    },
    allFieldsBtn:{
        position: 'absolute', 
        top: '2px', 
        right: '16px', 
        zIndex: 1, 
        width: '8px', 
        height: '4px', 
        padding: '4px', 
        backgroundColor: theme.palette.background.paper
    },
    popover: {
        pointerEvents: 'none'
    },
    fieldTextHover: {
        height: 'fit-content',
        minHeight: '56px',
        textAlign: 'center',
        display: 'table-cell',
        verticalAlign: 'middle'
    },
    paper: {
        minWidth: '48px',
        padding : '2px',
        border : '1px solid',
        borderColor: theme.palette.divider,
        borderRadius : '8px',
        backgroundColor : theme.palette.drawerColor,
        maxWidth: '200px',
    },
    field: {
        margin : '2px',
        padding : '2px',
        border : '1px solid',
        borderColor: theme.palette.divider,
        borderRadius : '8px',
        backgroundColor : theme.palette.drawerColor,
        justifyContent : 'center',
        alignItems: 'center',
        width : 'fit-content',
        maxWidth: '200px',
       // overflow: 'hidden',
      /*  '&:hover': {
            transform: 'scale(1.1)',
            zIndex: 100,
            height: 'fit-content',
            minHeight: '56px'
        } */
    },
    fieldText: {
        maxHeight: '40px',
        overflow: 'hidden',
        display: 'block',
        position: 'relative',
     /*   '&:after': {
            content:'"..."',
            background: 'inherit',
            position: 'absolute',
            bottom:0,
            right:0,
           // boxShadow: '0 0 5px red'
        }*/
    },
    popoverDiv: {
        textAlign: 'center', 
        minHeight: '56px', 
        display: 'table'
    },
    listItemText: {
        minWidth: '40px', 
        textAlign: 'center'
    },
    divFilterButton:{
        position: 'absolute', 
        top: '-2px', 
        right: '-12px', 
        borderRadius: '50%', 
        backgroundColor: 'cadetblue'
    },
    filterButton: {
        borderRadius: '50%', 
        height: '18px', 
        width: '20px'
    },
    draggingField: {
        borderWidth : '2px',
    },
    gragDropDiv: {
        display: 'flex', 
        flex: 1, 
        position: 'relative'
    },
    dragDropGridContainer:{
        display: 'grid', 
        gridTemplateColumns: 'auto 1fr', 
        gridTemplateRows: 'auto 1fr',
        position: 'absolute', 
        inset: 0
    },
    gridMeasure: {
        maxHeight: '100%', 
        maxWidth: '100%', 
        minHeight: '20px', 
        minWidth: '20px'
    },
    gridForFilters: {
        display: 'flex' , 
        maxWidth: '100%'
    },
    gridForTools: {
        maxWidth: '100%', 
        display: 'grid', 
        gridTemplateRows: '32px'
    },
    cell: {
        border: '1px solid black',
        width: '100px'
      //  borderStyle : 'solid',
      //  borderColor : "black"
    },
    leftTopCornerCell : {
    },
    tr: {
        "&:nth-child(odd)" :{
            backgroundColor: theme.palette.background.paper
        },
        "&:nth-child(even)":{
                backgroundColor: theme.palette.action.disabled,
            }
    },
    blanc : {
    },
    dimensionValueCell: {
        align : 'center',
        cursor : 'pointer',
        width: '100px'
    },
    dimensionNameCell: {
        backgroundColor : theme.palette.drawerColor,
        textAlign: 'center',
        width: '100px'
    },
    metricNameCell: {
        backgroundColor : theme.palette.drawerColor,
        textAlign: 'center',
        cursor : 'pointer',
        width: '100px'
    },
    metricValueCell: {
        width: '100px'
    },
    pivotTable: {
        width : gridWidth,
        height : gridHeight,
        overflow: 'auto'
    },
    modal: {
        position: 'absolute',
        backgroundColor: theme.palette.background.paper,
        border: '2px solid #000',
        boxShadow: theme.shadows[5],
        borderRadius: '4px',
        padding: '0px 8px',
      // minWidth: '500px'
    },
    dialog: {
        backgroundColor: theme.palette.background.default
    },
    panelNameVertical: {
        width: '10px', 
        wordWrap: 'break-word',
        margin: '8px auto',
        color: theme.palette.action.disabled
    },
    panelNameHorizontal: {
        color: theme.palette.action.disabled,
        margin: '0px auto',
        letterSpacing: '8px'
    },
    rangeMainHorizontal: {
        display: 'flex', 
        alignItems: 'center'
    },
    rangeMainVertical: {
        display: 'grid', 
        gridTemplateRows: '32px 20px 1fr 30px 32px', 
        justifyItems: 'center'
    },
    rangeP: {
        margin: 0
    },
    rangeBtn: {
        width: 'fit-content'
    },
    sliderRoot: {
        justifySelf: 'center'
    },
    thumb: {
        marginTop: '-4px',
        '&:focus, &:hover, &$active': {
            boxShadow: 'inherit',
        },
    },
    track: {
        height: 4,
        borderRadius: 4,
    }, 
    rail: {
        height: 4,
        borderRadius: 4,
    },
    valueLabel: {
      //  height: '24px',
        width: 'fit-content',
       // left: 'calc(-50% + 12px)',
        top: 0,
        backgroundColor: theme.palette.background.default,
        border: '2px solid',
        borderColor: theme.palette.primary.main,
        borderRadius: '25%', //'50%',
        padding: '0px 8px',
        '& *': {
          background: 'transparent',
          color: '#000',
        },
      },
    formControl: {
        margin: '0px 8px',
        width: '180px'
    },
    equalText: {
        width: '100%',
        margin: '8px'
    },
    opTypeAria: {
        margin: '8px'
    },
    btnsArea: {
        display: 'flex',
        justifyContent: 'end',
        marginTop: '16px'
    },
    cancelBtn: {
        marginLeft: '16px'
    },
    //TransferList
    rootTrList: {
        margin: 'auto',
    },
    cardHeaderTrList: {
        padding: theme.spacing(1, 2),
    },
    listTrList: {
        width: '100%',
        height: 230,
        backgroundColor: theme.palette.background.paper,
        overflow: 'auto',
    },
    listWOPaginationTrList: {
        width: '100%',
        height: 260,
        backgroundColor: theme.palette.background.paper,
        overflow: 'auto',
    },
    buttonTrList: {
        margin: theme.spacing(0.5, 0),
    },
    dialogTitle: {
        cursor: 'move'
    },
    // FormattingDialog
    FD_root: {
        flexGrow: 1,
        width: '100%',
        backgroundColor: theme.palette.background.paper,
    },
    FD_rightContent: {
        padding: '0 30px'
    },
    FD_wrapperForInputNumber: {
        display : 'flex',
        marginTop: '30px'
    },
    FD_inputForNumber: {
        width: '70px',
        marginLeft: '10px'
    },
    // ConfigSaveDialog
    CSD_wrapperList: {
        backgroundColor: '#e1e5f2',
        padding: '15px',
        borderRadius: '8px',
    },
    CSD_wrapperSaveFor:{
        marginTop: '16px',
        display: 'flex',
        alignItems: 'center'
    },
    CSD_formControl: {
        width: '100%',
        margin: 0
    },
    CSD_nameField: {
        width: '100%',
        margin: 0
    },
    CSD_nameFieldExisting: {
        width: '100%',
        margin: '40px 0 0'
    },
    CSD_descriptionField: {
        width: '100%',
        margin: '24px 0 0'
    },
    CSD_saveFor: {
        marginRight: '12px'
    },
    //ConfigDialog
    CD_root: {
        flexGrow: 1,
        width: '100%',
        backgroundColor: theme.palette.background.paper,
        padding: '8px 24px'
    },
    CD_item: {
        marginBottom: '8px',
        padding: '15px',
        cursor: 'pointer'
    },
    CD_subtitle: {
        padding: "12px",
        backgroundColor: "#e1e5f2",
        borderRadius: "8px",
        marginBottom: "4px"
    },
    //CustomList
    CL_listItem: {
        marginBottom: '12px', 
        borderBottom: '1px solid #dad9d9'
    }
}))
