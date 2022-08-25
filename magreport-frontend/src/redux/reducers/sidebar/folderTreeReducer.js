import { FOLDERSTREE_TOGGLE, FOLDER_CONTENT_LOADED , FOLDERSTREELOADED, FOLDERSTREELOADFAIL, FOLDERSTREELOADSTART,
    FOLDER_CONTENT_FOLDER_ADDED, FOLDER_CONTENT_FOLDER_EDITED, FOLDER_CONTENT_FOLDER_DELETED, FOLDERSTREESETINIT,
    FOLDERS_TREE_CHANGE_PARENT_STARTED, FOLDERS_TREE_PARENT_CHANGED, FOLDER_CONTENT_PARENT_FOLDER_CHANGED, FOLDER_CONTENT_FOLDER_COPIED, FOLDER_CONTENT_SORT_CLICK
} from '../../reduxTypes'

import SidebarItems from '../../../main/Main/Sidebar/SidebarItems';
import {folderItemTypeSidebarItem} from 'main/FolderContent/folderItemTypeSidebarItem';

const initialState = {}

for(let i of Object.values(SidebarItems)){
    if(i.folderTree){
        initialState[i.key] = {
            expanded: false,
            status: 'init',
            folders: null,
        }
    }
    if(i.subItems){
        for(let si of Object.values(i.subItems)){
            if(si.folderTree){
                initialState[si.key] = {
                    expanded: false,
                    status: 'init',
                    folders: null,
                }
            }
        }
    }
}

export const folderTreeReducer = (state = initialState, action) => {
    let entity;
    let folders;
    let folder;
    let folderId;
    let parentNew;
    let parentOld;

    if (action.entity){
        entity = action.entity;
    }

    switch (action.type){
        case FOLDERSTREE_TOGGLE:

            folders = state[entity].folders
            folder = folders.get(action.folderId)
            folders.set(action.folderId, {...folder, expanded: !folder.expanded})
                        
            return {
                ...state, [entity]:{...state[entity], folders: folders}
            };  

        case FOLDERSTREELOADSTART:
            return {
                ...state, [entity]:{...state[entity], status: "startLoading", folderId: action.folderId}
            };

        case FOLDERSTREELOADED:
            let sortParams = JSON.parse(localStorage.getItem("sortParams"));

            if(!sortParams) {
                const defaultSortParams = { key: 'name', direction: 'ascending' };
                sortParams = defaultSortParams;
            }

            folders = state[entity].folders
        
            if (!folders){
                folders = new Map();
            }

            let childFoldersId = [],
                actionFolders = action.folders;

            folderId = action.folderId

            if (!action.folderId){
                folderId = 'root'

                folders = new Map()

                if (actionFolders && actionFolders.length > 0 && sortParams) {
                    actionFolders.sort((a, b) => {
                        if (a[sortParams.key] < b[sortParams.key]) {
                            return sortParams.direction === 'ascending' ? -1 : 1;
                        }
                        if (a[sortParams.key] > b[sortParams.key]) {
                            return sortParams.direction === 'ascending' ? 1 : -1;
                        }
                            return 0;
                    })
                }

                for (let folder of actionFolders){
                    folders.set(folder.id, {...folder, parentId: folderId, expanded: undefined, childFolders: []})
                    childFoldersId.push(folder.id)
                }

                folders.set(folderId , {...folders.get(folderId), expanded:true, childFolders: childFoldersId})

            } else {

                for (let folder of actionFolders){
                    folders.set(folder.id, {...folder, parentId: folderId, expanded: undefined, childFolders: []})
                    childFoldersId.push(folder.id)
                }

                folders.set(folderId , {...folders.get(folderId), expanded:true, childFolders: childFoldersId})
            }

            return {
                ...state, [entity]:{...state[entity], status: "loaded", folders}
            };

        case FOLDERSTREELOADFAIL:
            return {
                ...state, [entity]:{...state[entity], status: "loadFail"}
            };
        
        case FOLDERSTREESETINIT:
            return {
                ...state, [entity]:{...state[entity], status: "init"}
            };
        
        case FOLDERS_TREE_CHANGE_PARENT_STARTED:
            entity = folderItemTypeSidebarItem(action.itemsType).key
            folders = state[entity].folders

            if (!folders){
                folders = new Map()
            }

            folder = folders.get(action.folderId)
            parentOld = folders.get(folder.parentId)
            parentOld.childMove = true

            folders.set(folder.parentId, {...parentOld})
            folders.set(action.folderId, {...folder})

            return {...state, folders}
        
        case FOLDERS_TREE_PARENT_CHANGED:
            entity = folderItemTypeSidebarItem(action.itemsType).key
            folders = state[entity].folders

            if (!folders){
                folders = new Map()
            }
            
            // добавляем каталог к новому родителю
            const parentNewId = action.parentFolderId ? action.parentFolderId : 'root'
            parentNew = folders.get(parentNewId)
            parentNew.childFolders.push(action.folderId)
            
            // удаляем каталог у старого родителя
            folder = folders.get(action.folderId)
            const parentOdlId = folder.parentId
            parentOld = folders.get(parentOdlId)
            const folderIndex = parentOld.childFolders.indexOf(action.folderId)
            parentOld.childFolders.splice(folderIndex, 1)
            delete parentOld.childMove

            // обновляем ID родителя у переносимого каталога
            folder.parentId=parentNewId

            folders.set(action.folderId, {...folder})
            folders.set(parentNewId, {...parentNew})
            folders.set(parentOdlId, {...parentOld})
            
            return {...state, folders}

        case FOLDER_CONTENT_SORT_CLICK: 
            entity = folderItemTypeSidebarItem(action.itemsType).key;
            if (state[entity]) {
                folders = state[entity].folders;
            }
            
            if (folders) {

                let sortParams = JSON.parse(localStorage.getItem("sortParams")),
                    folderItems = [...folders].map(e =>{ return e[1];}).slice(),
                    newFolderItems = new Map(),
                    childFoldersIds = [];

                folderId = action.folderId;

                if (!action.folderId){
                    folderId = 'root'
                }

                if (folderItems && folderItems.length > 0 && sortParams) {
                    folderItems.sort((a, b) => {
                        if (a[sortParams.key] < b[sortParams.key]) {
                            return sortParams.direction === 'ascending' ? -1 : 1;
                        }
                        if (a[sortParams.key] > b[sortParams.key]) {
                            return sortParams.direction === 'ascending' ? 1 : -1;
                        }
                            return 0;
                    })
                }
                for (let folder of folderItems){
                    if(folder.name) {
                        newFolderItems.set(folder.id, {...folder})
                        if(folder.parentId === "root") {
                            childFoldersIds.push(folder.id)
                        }
                    }
                }
                
                newFolderItems.set(folderId , {...folders.get(folderId), expanded:true, childFolders: childFoldersIds})
    
                return {...state, [entity]:{...state[entity], folders: newFolderItems}}
            }

            return {...state, folders}

        case FOLDER_CONTENT_LOADED:
        case FOLDER_CONTENT_FOLDER_ADDED:
        case FOLDER_CONTENT_FOLDER_EDITED:
        case FOLDER_CONTENT_FOLDER_DELETED:
        case FOLDER_CONTENT_PARENT_FOLDER_CHANGED:
            const hasFolderTree = folderItemTypeSidebarItem(action.itemsType).folderTree
            if (hasFolderTree){
                entity = folderItemTypeSidebarItem(action.itemsType).key;
                folders = state[entity].folders;

                if (!folders){
                    folders = new Map();
                }
                
                if (action.type === FOLDER_CONTENT_LOADED) {
                    folderId = action.folderData.parentId ?  action.folderData.parentId : 'root';
                    folder = folders.get(action.folderData.id);
                }
                else {
                    folderId = action.parentFolderId ? action.parentFolderId : 'root';
                    folder = folders.get(folderId);
                }

                if (folder){
                    if (action.type === FOLDER_CONTENT_FOLDER_ADDED || action.type === FOLDER_CONTENT_PARENT_FOLDER_CHANGED || action.type === FOLDER_CONTENT_FOLDER_COPIED){
                        folders.set(folderId, {...folder, childFolders: [...folder.childFolders, action.data.id]})
                        const newFolder = {
                            expanded: undefined,
                            childFolders: [],
                            id: action.data.id,
                            name: action.data.name,
                            parentId: action.data.parentId ? action.data.parentId : 'root'
                        }
                        folders.set(action.data.id, newFolder)
                    }
                    else if (action.type === FOLDER_CONTENT_FOLDER_EDITED){
                        let fldr = folders.get(action.data.id)
                        let childFoldersId = [];
                        for (let childfld of action.data.childFolders) {
                            childFoldersId.push(childfld.id)
                        }
                        const editedFolder = {
                            expanded: fldr.expanded,
                            childFolders: childFoldersId,
                            id: action.data.id,
                            name: action.data.name,
                            parentId: action.data.parentFolderId ? action.data.parentFolderId : 'root'
                        }
                        folders.set(action.data.id, editedFolder)
                    }
                    else if (action.type === FOLDER_CONTENT_FOLDER_DELETED){
                        const newChildFolders = folder.childFolders.filter(item => item !== action.folderData.id)
                        folders.set(folderId, {...folder, childFolders: newChildFolders})
                        folders.delete(action.folderData.id)
                    }
                    else if (action.type === FOLDER_CONTENT_LOADED){
                        let childFldrs = []
                        if (action.folderData.childFolders) {
                            for (let fld of action.folderData.childFolders) {
                                childFldrs.push(fld.id)

                                folders.set(fld.id, {expanded: undefined, childFolders: [], id: fld.id, name: fld.name, parentId: fld.parentId ? fld.parentId : 'root'})
                            }
                        } 
                        folders.set(action.folderData.id, {...folder, name: action.folderData.name, childFolders: childFldrs})
                    }
                    else {}

                    return {
                        ...state, [entity]:{...state[entity], status: "loaded", folders: folders}
                    };
                }
                else{
                    return {...state}
                }
            }
            else {
                return {...state}
            }

        default:
            return state;
    }
}