import { makeStyles, withStyles } from '@material-ui/core/styles';
//import { useTheme } from '@material-ui/core/styles';
import TableCell from '@material-ui/core/TableCell';
/*
function getPalette (theme) {
    let t = {};

    function getPaletteRecursive (obj, name) {
        if (typeof obj === 'object') {
            for (let k in obj) {
                getPaletteRecursive(obj[k], name+k[0].toUpperCase() + k.slice(1))
            }
        }
        else if (typeof obj === 'string'){
            t[name[0].toLowerCase() + name.slice(1)] = {backgroundColor: obj};
        }
    
    }

    getPaletteRecursive(theme.palette, '');
    return t
}
*/
export const ThemeDesignerCSS = makeStyles((theme) => ({
    appBar: {
        backgroundColor: theme.palette.primary.light,
        position: 'sticky',
        height: '40px'
    },
    rootGrid: {
        padding: '16px', 
        display: 'flex', 
        flex: 1,
        overflow: 'auto'
    },
    flexTreeGridItem: {
        display: 'flex',
        minWidth: '240px'
    },
    flexScetchGridItem: {
        display: 'flex',
        minWidth: '240px'
    },
    flexColorsGridItem: {
        display: 'flex',
        minWidth: '174px'
    },
    posAbs: {
        position: 'absolute', 
        top: '40px', 
        margin: '10px'
      //  bottom: 0
    },
    posAbsColor: {
        position: 'absolute', 
        margin: '4px'
    },
    posAbs0: {
        position: 'absolute', 
        top: 0, 
        bottom: 0
    },
    treeRelative: {
        display: 'flex', 
        flexDirection: 'column', 
        position: 'relative', 
        flex: 1, 
        overflow: 'auto', 
        height: '100%', 
        //padding: '0px 10px 10px',
        minWidth: '240px'
    },
    sketchRelative: {
        display: 'flex', 
        flexDirection: 'column', 
        position: 'relative',
        flex: 1, 
        overflow: 'auto', 
        alignItems: 'center',
        minWidth: '240px'
    },
    coloTableRelative: {
        display: 'flex', 
        flexDirection: 'column', 
        position: 'relative', 
        flex: 1,
        minWidth: '174px'
    },
    colorCell: {
        padding: '2px',
        maxHeight: '40px',
        maxWidth: '40px'
    /*    cursor:'pointer',
         */
    },
    colorCellHead: {
        padding: '2px',
        maxHeight: '40px',
        maxWidth: '40px',
        backgroundColor: theme.palette.primary.light,
        color: theme.palette.primary.contrastText,
    },
    colorBtn: {
        minWidth: '38px',
        maxWidth: '38px',
        minHeight: '38px',
      //  margin: '4px 8px',
        '&:hover': {
            borderRadius: '20px'
        }
    },
    colorTreeBtn: {
        minWidth: '32px',
        maxWidth: '32px',
        minHeight: '32px',
        margin: '4px 8px',
        '&:hover': {
            borderRadius: '20px' 
        }
    },
    toolbar: {
        backgroundColor: theme.palette.drawerColor  ,
        color: theme.palette.text.primary
    }
}));
//export const themePalette =  getPalette(theme)

export const StickyTableCell = withStyles((theme) => ({
    head: {
      backgroundColor: theme.palette.primary.light, //common.black,
      color: theme.palette.primary.contrastText, //common.white,
      left: 0,
      position: "sticky",
      zIndex: theme.zIndex.appBar + 2
    },
    body: {
      backgroundColor: theme.palette.background.paper, //"#ddd",
      minWidth: "50px",
      left: 0,
      position: "sticky",
      zIndex: theme.zIndex.appBar + 1
    }
  }))(TableCell);

//export const PaletteCSS = makeStyles(getPalette);