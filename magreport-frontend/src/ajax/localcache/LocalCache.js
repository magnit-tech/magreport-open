import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

function LocalCache(){

    /*
        FolderInfo
    */

    this.folderDataMap = new Map();
    this.itemDataMap = new Map();

    this.setFolderData = (folderItemsType, folderData) => {
        /*
            folderData
        */
        if(!this.folderDataMap.get(folderItemsType)){
            this.folderDataMap.set(folderItemsType, new Map());
        }
        let itemsTypeMap = this.folderDataMap.get(folderItemsType);

        if (folderItemsType === FolderItemTypes.report) {
            for (let r of folderData.reports){
                if (r.path){
                    for (let p of r.path){
                        itemsTypeMap.set(p.id, p);
                    }
                }
            }
        }

        if(folderData.id !== null){
            itemsTypeMap.set(folderData.id, folderData);
        }
        for(let f of folderData.childFolders){
            itemsTypeMap.set(f.id, f);
        }

        /*
            itemData
        */
        if(!this.itemDataMap.get(folderItemsType)){
            this.itemDataMap.set(folderItemsType, new Map());
        }
        if (folderItemsType === FolderItemTypes.reportsDev){
            if(!this.itemDataMap.get(FolderItemTypes.report)){
                this.itemDataMap.set(FolderItemTypes.report, new Map());
            }
        }
        itemsTypeMap = this.itemDataMap.get(folderItemsType);
        let itemsReportMap = this.itemDataMap.get(FolderItemTypes.report);

        let items =  folderItemsType === FolderItemTypes.report ? folderData.reports
                    :folderItemsType === FolderItemTypes.reportsDev ? folderData.reports
                    :folderItemsType === FolderItemTypes.datasource ? folderData.dataSources
                    :folderItemsType === FolderItemTypes.dataset ? folderData.dataSets
                    :folderItemsType === FolderItemTypes.filterTemplate ? folderData.filterTemplates
                    :folderItemsType === FolderItemTypes.filterInstance ? folderData.filterInstances
                    :folderItemsType === FolderItemTypes.roles ? folderData.roles
                    :folderItemsType === FolderItemTypes.securityFilters ? folderData.securityFilters
                    :folderItemsType === FolderItemTypes.asm ? folderData.asm
                    :folderItemsType === FolderItemTypes.schedules ? folderData.schedules
                    :folderItemsType === FolderItemTypes.scheduleTasks ? folderData.scheduleTasks
                    :folderItemsType === FolderItemTypes.systemMailTemplates ? folderData.systemMailTemplates
                    :folderItemsType === FolderItemTypes.theme ? folderData.theme
                    :[];
        for(let i of items){
            itemsTypeMap.set(i.id, {...i, folderId: folderData.id});
            if (folderItemsType === FolderItemTypes.reportsDev){
                itemsReportMap.set(i.id, {...i, folderId: folderData.id})
            }
        }
    }

    this.setSearchFolderData = (folderItemsType, folderData) => {
        /*
            folderData
        */
        if(!this.folderDataMap.get(folderItemsType)){
            this.folderDataMap.set(folderItemsType, new Map());
        }
        const folderTypeMap = this.folderDataMap.get(folderItemsType);
        for(let f of folderData.folders){
            for (let p of f.path){
                folderTypeMap.set(p.id, p);
            }
        }

        if(!this.itemDataMap.get(folderItemsType)){
            this.itemDataMap.set(folderItemsType, new Map());
        }
        
        const itemsTypeMap = this.itemDataMap.get(folderItemsType);
        
        for(let f of folderData.objects){
            let lastFolderId
            for (let p of f.path){
                folderTypeMap.set(p.id, p);
                lastFolderId=p.id
            }
            itemsTypeMap.set(f['element'].id, {...f['element'], folderId: lastFolderId});
        }
    }

    this.setDependenciesFolderData = (folderData) => {
        /*
            folderData
        */
        const folderItemsTypesArr = [
            {keyInResponse: 'reports', itemsType: FolderItemTypes.reportsDev,},
            {keyInResponse: 'filterInstances', itemsType: FolderItemTypes.filterInstance,},
            {keyInResponse: 'securityFilters', itemsType: FolderItemTypes.securityFilters,},
            {keyInResponse: 'dataSets', itemsType: FolderItemTypes.dataset,},
        ]
        for (let f of folderItemsTypesArr){
            if (folderData[f.keyInResponse] && folderData[f.keyInResponse].length){
                if(!this.folderDataMap.get(f.itemsType)){
                    this.folderDataMap.set(f.itemsType, new Map());
                }
                const folderTypeMap = this.folderDataMap.get(f.itemsType);
                for (let i of folderData[f.keyInResponse]){
                    if (i.path && i.path.length){
                        for(let p of i.path){
                            folderTypeMap.set(p.id, p);
                        }
                    }
                }
            }
        }
    }

    this.setItemData = (itemType, itemData) => {
        let itemsTypeMap = this.itemDataMap.get(itemType);
        if(!itemsTypeMap){
            this.itemDataMap.set(itemType, new Map());
            itemsTypeMap = this.itemDataMap.get(itemType);
        }
        itemsTypeMap.set(itemData.id, itemData);
    }

    this.getFolderData = (folderItemsType, folderId) => {
        return this.folderDataMap.get(folderItemsType).get(folderId);
    }

    this.getItemData = (itemType, itemId) => {
        return this.itemDataMap.get(itemType).get(itemId);
    }

    /*
        ReportInfo
    */
    this.reportInfoMap = new Map();

    this.setReportInfo = (reportInfo) => {
        this.setItemData(FolderItemTypes.report, reportInfo);
    }

    this.getReportInfo = (reportId) => {
        return this.getItemData(FolderItemTypes.report, reportId);
    }

    /*
        JobInfo
    */

    this.setJobInfo = (jobInfo) => {
        this.setItemData(FolderItemTypes.job, jobInfo);
    }

    this.getJobInfo = (jobId) => {
        return this.getItemData(FolderItemTypes.job, jobId);
    }

    /*
        Security Filter
     */

    this.getSecurityFilterInfo = (securityFilterId) => {
        return this.getItemData(FolderItemTypes.securityFilters, securityFilterId);
    }

    /*
        Filter Instance
     */

    this.getFilterInstanceInfo = (filterInstanceId) => {
        return this.getItemData(FolderItemTypes.filterInstance, filterInstanceId);
    }

    /*
        Filter Template
     */

    this.getFilterTemplateInfo = (filterTemplateId) => {
        return this.getItemData(FolderItemTypes.filterTemplate, filterTemplateId);
    }

    /*
        Datasource
    */ 
    this.datasourceInfoMap = new Map();

    this.getDatasourceInfo = (datasourceId) => {
        return this.getItemData(FolderItemTypes.datasource, datasourceId);
    } 

    /*
        Dataset
    */

    this.datasetInfoMap = new Map();

    /*this.setDatasetInfo = (datasetInfo) => {
        this.datasetInfoMap.set(datasetInfo.datasetId, datasetInfo);
    }*/

    this.getDatasetInfo = (datasetId) => {
        return this.getItemData(FolderItemTypes.dataset, datasetId);
    }

    /*
        ASM
     */

    this.getASMInfo = (asmId) => {
        return this.getItemData(FolderItemTypes.asm, asmId);
    }

    /*
        Schedule
     */
    this.getScheduleInfo = (scheduleId) => {
        return this.getItemData(FolderItemTypes.schedules, scheduleId);
    }

    /*
        Scheduled reports
     */
    this.getScheduleTasksInfo = (scheduleTaskId) => {
        return this.getItemData(FolderItemTypes.scheduleTasks, scheduleTaskId);
    }
    /*
        Design (themes)
    */
   this.getThemeInfo = (themeId) => {
    return this.getItemData(FolderItemTypes.theme, themeId);
}

    /*
        Roles
     */

    this.getRoleInfo = (roleId) => {
        return this.getItemData(FolderItemTypes.roles, roleId);
    }

    /*
        System Mail Templates
     */

    this.getSystemMailTemplateIdInfo = (systemMailTemplateId) => {
        return this.getItemData(FolderItemTypes.systemMailTemplates, systemMailTemplateId);
    }

    /*
        UserRolesInfo
    */

    this.userInfo = null;

    this.setUserInfo = (userInfo) => {
        userInfo.isAdmin = false
        userInfo.isDeveloper = false
        for (let r of userInfo.roles) {
            if (r.name==='ADMIN') userInfo.isAdmin = true;
            if (r.name==='DEVELOPER') userInfo.isDeveloper = true;
        }
        this.userInfo = userInfo;
    }

    this.getUserInfo = () => {
        return this.userInfo;
    }

    this.userRolesInfo = null;

    this.setUserRolesInfo = (userRolesInfo) => {
        this.userRolesInfo = userRolesInfo;
    }

    this.getUserRolesInfo = () => {
        return this.userRolesInfo;
    }

    /*
    *******************************
    */

    this.jobFiltersObj = {jobStatuses: []};

    this.setJobFilters = jobFilters => {
        this.jobFiltersObj = jobFilters;
    }

    this.setJobFiltersRefresh = interval => {
        this.jobFiltersObj.interval = interval;
    }

    this.setJobFiltersCards = cardsOnPage => {
        this.jobFiltersObj.cardsOnPage = cardsOnPage;
    }

    this.getJobFilters = () => {
        return this.jobFiltersObj;
    }

}
export default LocalCache;