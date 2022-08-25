import { ROOTMENUCHANGE } from '../../reduxTypes'

const initialState = {
    rootMenu: {}
}

export const rootMenuReducer = (state = initialState, action) => {
    switch (action.type){
        case ROOTMENUCHANGE:
            let curProps = (state.rootMenu[action.menuItemType]) ? state.rootMenu[action.menuItemType] : {};
            curProps[action.key] = action.val;
            return { 
                rootMenu: {...state.rootMenu, [action.menuItemType]:curProps }
            }
        default:
            return state
    }
}
