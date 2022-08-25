import {useDispatch, useSelector} from "react-redux";
import SidebarItems from '../Sidebar/SidebarItems.js'

export function useNavigateBack(sourceSidebarItemKey = null) {
    const dispatch = useDispatch()
    const items = useSelector(state => state.navbar.items);
    const currentSidebarItemKey = useSelector(state => state.currentSidebarItemKey);

    if (items.length < 2 || (sourceSidebarItemKey !== currentSidebarItemKey 
                          && sourceSidebarItemKey === SidebarItems.development.subItems.datasets.key
    )) {
        return () => {};
    }

    return () => dispatch(items[items.length - 2].callbackFunc);
}
