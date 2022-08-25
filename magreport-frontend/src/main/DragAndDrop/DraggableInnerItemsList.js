import React from "react";

/**
 * Контейнер для Draggable элементов с memoization
 * @param {Object} props - свойства компонента
 * @param {Array} props.items - массив элементов
 * @return {JSX.Element}
 * @constructor
 */
function DraggableInnerItemsList(props) {
    return <div>
        {props.items}
    </div>;
}

export default React.memo(DraggableInnerItemsList);
