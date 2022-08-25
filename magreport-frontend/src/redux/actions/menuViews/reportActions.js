import {REPORT_START /*, REPORT_RUN */ } from 'redux/reduxTypes';

export function startReport(reportId, jobId, sidebarItemKey, itemType){
    return {
        type: REPORT_START,
        reportId: reportId,
        jobId: jobId,
        sidebarItemKey: sidebarItemKey,
        itemType: itemType
    }
}
