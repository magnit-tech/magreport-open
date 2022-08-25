import {makeStyles} from '@material-ui/core/styles';

export const MainCSS = makeStyles(theme => ({
    main: {
        display: 'flex'
    },
    workspace: {
        flex: 1,
        overflow: 'hidden',
        display: 'flex',
        flexDirection: 'column'
    }
  }));