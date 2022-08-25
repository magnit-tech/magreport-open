import { SIDEBAR_ITEM_CHANGED } from '../../reduxTypes'

export default function actionSetSidebarItem(newSidebarItem){
    return {type: SIDEBAR_ITEM_CHANGED,
            newSidebarItem : newSidebarItem}
}