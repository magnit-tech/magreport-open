import React from 'react';
import {FolderItemTypes} from './FolderItemTypes';
import FolderDescriptionWindow from './ModalWindows/FolderDescriptionWindow'
import RoleModalWindow from './ModalWindows/RoleModalWindow'
// import RoleUserListWindow from './ModalWindows/RoleUserListWindow'
import GrantsModalWindow from './ModalWindows/GrantsModalWindow';
import SqlViewer from '../FolderContent/ModalWindows/SqlViewer';

export default function ItemWindow(props){
    
    if (props.type === 'folder'){
        return (
            <FolderDescriptionWindow 
                isOpen={props.isOpen}
                data={props.data}
                onClose={props.onClose}
                onSave={(...args) => {props.onSave(props.type, ...args)}}
            />
        )
    }
    if (props.type === FolderItemTypes.roles){
        return (
            <RoleModalWindow 
                isOpen={props.isOpen}
                data={props.data}
                onSave={(...args) => {props.onSave(props.type, ...args)}}
                onClose={props.onClose}
            />
        )
    }

    // if (props.type === 'roleUsers'){
    //     return (
    //         <RoleUserListWindow 
    //             isOpen={props.isOpen}
    //             id={props.data.id}
    //             itemsType={props.data.itemsType}
    //             onClose={props.onClose}
    //         />
    //     )
    // }

    if (props.type === 'grants'){
        return (
            <GrantsModalWindow 
                isOpen={props.isOpen}
                folderId={props.data.folderId}
                itemsType={props.data.itemsType}
                onClose={props.onClose}
            />
        )
    }

    return (<SqlViewer/>)
    
}