import {
    ASM_ADD_ITEM_CLICK,
    ASM_ADDED,
    ASM_DATA_LOAD_FAILED,
    ASM_DATA_LOADED,
    ASM_DELETED,
    ASM_EDIT_ITEM_CLICK,
    ASM_EDITED,
    ASM_LIST_LOAD_FAILED,
    ASM_LIST_LOADED,
    ASM_LIST_SHOW,
    ASM_REFRESH_FINISH,
    ASM_REFRESH_START,
    ASM_VIEW_ITEM_CLICK
} from "redux/reduxTypes";
import {FolderItemTypes} from "../../../main/FolderContent/FolderItemTypes";

export const actionAsmListShow = () => {
    return {type: ASM_LIST_SHOW};
}

export const actionAsmListLoaded = (data) => {
    return {
        type: ASM_LIST_LOADED,
        data
    };
}

export const actionAsmListLoadFailed = (error) => {
    return {
        type: ASM_LIST_LOAD_FAILED,
        error
    };
}

export const actionAsmAddItemClick = () => {
    return {
        type: ASM_ADD_ITEM_CLICK,
        itemType: FolderItemTypes.asm,
    };
}

export const actionAsmViewItemClick = (itemId) => {
    return {
        type: ASM_VIEW_ITEM_CLICK,
        itemId,
        itemType: FolderItemTypes.asm,
    };
}

export const actionAsmEditItemClick = (itemId) => {
    return {
        type: ASM_EDIT_ITEM_CLICK,
        itemId,
        itemType: FolderItemTypes.asm,
    };
}

export const actionAsmAdded = (data) => {
    return {
        type: ASM_ADDED,
        data
    };
}

export const actionAsmEdited = (data) => {
    return {
        type: ASM_EDITED,
        data
    };
}

export const actionAsmDeleted = (data) => {
    return {
        type: ASM_DELETED,
        data
    };
}

export const actionAmsRefresh = status => {
    return {
        type: status === 'START' ? ASM_REFRESH_START : ASM_REFRESH_FINISH,
        refresh: status === 'START',
    }
}

export function actionAsmDataLoaded(data) {
    return {
        type: ASM_DATA_LOADED,
        data
    };
}

export function actionAsmDataLoadFailed(error) {
    return {
        type: ASM_DATA_LOAD_FAILED,
        error
    }
}
