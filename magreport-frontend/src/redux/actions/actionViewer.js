import {VIEWER_EDIT_ITEM, VIEWER_VIEW_ITEM} from "../reduxTypes";

export function actionViewerViewItem(itemType, itemId, itemName) {
    return {
        type: VIEWER_VIEW_ITEM,
        itemType,
        itemsType: itemType,
        itemId,
        itemName
    };
}

export function actionViewerEditItem(itemType, itemId, itemName) {
    return {
        type: VIEWER_EDIT_ITEM,
        itemType,
        itemsType: itemType,
        itemId,
        itemName
    };
}
