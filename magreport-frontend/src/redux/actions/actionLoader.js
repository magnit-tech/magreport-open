import { SHOWLOADER, HIDELOADER } from '../reduxTypes'

export const showLoader = () =>{
    return {type: SHOWLOADER}
}

export const hideLoader = () =>{
    return {type: HIDELOADER}
}