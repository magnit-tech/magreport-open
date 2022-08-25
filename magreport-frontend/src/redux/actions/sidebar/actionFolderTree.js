import { FOLDERSTREE_TOGGLE, FOLDERSTREELOADSTART, FOLDERSTREELOADED, FOLDERSTREELOADFAIL, FOLDERSTREESETINIT } from '../../reduxTypes'
import SidebarItems from '../../../main/Main/Sidebar/SidebarItems';

// dataHub
import dataHub from 'ajax/DataHub';

export const foldersLoading = (folderId, entity, folderPath) => {
    return async dispatch => {
        dispatch(foldersLoadStart(entity, folderId))

        try {
            if (entity === SidebarItems.reports.key){
                dataHub.folderController.getFolder(folderId, magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)})
            }
            else if (entity === SidebarItems.admin.subItems.roles.key){
                if (!folderId){
                    dataHub.roleController.getAllTypes(magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)})
                }
            }
            // else if (entity === SidebarItems.admin.subItems.users.key){

            // }
            else if (entity === SidebarItems.admin.subItems.securityFilters.key){
                dataHub.securityFilterController.getFolder(folderId, magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)})
            }
            // else if (entity === SidebarItems.admin.subItems.userJobs.key){

            // }
            // else if (entity === SidebarItems.admin.subItems.ASMAdministration.key){

            // }
            else if (entity === SidebarItems.development.subItems.datasources.key){
                dataHub.datasourceController.getFolder(folderId, magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)});
            }
            else if (entity === SidebarItems.development.subItems.datasets.key){
                dataHub.datasetController.getFolder(folderId, magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)});
            }
            else if (entity === SidebarItems.development.subItems.filterTemplates.key){
                dataHub.filterTemplateController.getFolder(folderId, magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)});
            }
            else if (entity === SidebarItems.development.subItems.filterInstances.key){
                dataHub.filterInstanceController.getFolder(folderId, magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)});
            }
            else if (entity === SidebarItems.development.subItems.reportsDev.key){
                dataHub.reportController.getFolder(folderId, magrepResponse => { handleFoldersResponse(folderId, entity, folderPath, magrepResponse)});
            }
            else if (entity === SidebarItems.admin.subItems.mailTexts.key){
                if (!folderId) {
                    dataHub.serverMailTemplateController.getAllMailTemplateType(magrepResponse => {
                        handleFoldersResponse(folderId, entity, folderPath, magrepResponse)
                    })
                }
            }
            else {
                dispatch(foldersLoadFail(entity))
            }

            function handleFoldersResponse(folderId, entity, folderPath, magrepResponse){
                if (magrepResponse.ok){
                    
                    if (entity === SidebarItems.admin.subItems.roles.key ||
                        entity === SidebarItems.admin.subItems.mailTexts.key){
                        magrepResponse.data.childFolders = magrepResponse.data
                    }
                    
                    let folderItems = [];
                    let sortParams = JSON.parse(localStorage.getItem("sortParams"));
                    
                    for (let folder of magrepResponse.data.childFolders){
                        folderItems.push({
                            id: folder.id,
                            name: folder.name,
                            childFolders:[],
                            expanded: undefined,
                            created: folder.created,
                            modified: folder.modified
                        })
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
                        });
                    }

                    dispatch({
                        type: FOLDERSTREELOADED,
                        folders: folderItems,
                        entity,
                        folderId,
                        folderPath
                    })
                }
                else{
                    dispatch(foldersLoadFail(entity))
                }
            }
        } 
        catch (e){
            console.error(e)
            dispatch(foldersLoadFail(entity))
        }
    }
}

export const foldersLoadFail = (entity) => {
    return {
        type: FOLDERSTREELOADFAIL,
        entity
    }
}

export const foldersSetInit = (entity) => {
    return {
        type: FOLDERSTREESETINIT,
        entity
    }
}

export const foldersLoadStart = (entity, folderId) => {
    return {
        type: FOLDERSTREELOADSTART,
        entity,
        folderId
    }
}

export const foldersTreeToggle = (folderId, entity, folderPath) =>{
    return {
        type: FOLDERSTREE_TOGGLE,
        folderId,
        entity, 
        folderPath,
    }
}