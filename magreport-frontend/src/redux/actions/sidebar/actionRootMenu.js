import { ROOTMENUCHANGE } from '../../reduxTypes'

export const rootMenuChange = (menuItemType, key, val) =>{
    return {
        type: ROOTMENUCHANGE,
        menuItemType,
        key,
        val,
    }
}