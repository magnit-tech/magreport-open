import React from 'react';
import UserDesigner from './UserDesigner'

function UsersMenuView(props){
    return(
        <UserDesigner
            items = {props.items}
        >
        </UserDesigner>
    )
}



export default UsersMenuView;