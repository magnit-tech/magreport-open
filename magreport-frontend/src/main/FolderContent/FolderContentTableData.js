
// local
import {FolderItemTypes} from  './FolderItemTypes';

export default function FolderContentTableData(itemsType, data) {

    switch  (itemsType) {
        case FolderItemTypes.scheduleTasks:
           return {
                header: [
                    { id: 'id', numeric: true, disablePadding: true, label: 'Id' },
                    { id: 'name', numeric: false, disablePadding: false, label: 'Название' },
                    { id: 'description', numeric: false, disablePadding: false, label: 'Описание' },
                    { id: 'reportName', numeric: false, disablePadding: false, label: 'Отчёт' },
                    { id: 'destinations', numeric: false, disablePadding: false, label: 'Получатели' },
                    { id: 'schedules', numeric: false, disablePadding: false, label: 'Расписания' },
                    { id: 'status', numeric: false, disablePadding: false, label: 'Статус' },
                    { id: 'expirationDate', numeric: false, disablePadding: false, label: 'Дата окончания' },
                ],
                body: data.map(item => ({
                    id: item.id, 
                    name: item.name,
                    description: item.description,
                    reportName: item.report.name,
                    destinations: item.destinations.map(item=>item.value).join('; '),
                    schedules: item.schedules.map(item=>item.name).join('; '),
                    status: item.status,
                    expirationDate: item.expirationDate,
                }))
            };
        case FolderItemTypes.schedules:
            return {
                header: [
                { id: 'id', numeric: true, disablePadding: true, label: 'Id' },
                { id: 'name', numeric: false, disablePadding: false, label: 'Название' },
                { id: 'description', numeric: false, disablePadding: false, label: 'Описание' },
                { id: 'type', numeric: false, disablePadding: false, label: 'Тип' },
                { id: 'planStartDate', numeric: false, disablePadding: false, label: 'Плановая дата старта' }
                ],
                body: []
            };
        default: return {header: [], body:[]}
    }
}