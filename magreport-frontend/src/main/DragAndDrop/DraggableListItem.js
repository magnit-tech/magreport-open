import {Draggable} from "react-beautiful-dnd";
import React from "react";

/**
 * Компонент-обертка над элементом списка DraggableItemsList с возможностью перетаскивания
 * @param {Object} props - свойства компонента
 * @param {number} props.index - индекс элемента в списке
 * @param {Array} props.children - дочерние элементы
 * @param {string} props.draggableId - идентификатор для компонента Draggable (перемещаемого)
 * @return {JSX.Element}
 * @constructor
 */
export default function DraggableListItem(props) {
    return (
        <Draggable
            index={props.index}
            key={props.draggableId}
            draggableId={props.draggableId}
        >
            {provided => (
                <div
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    ref={provided.innerRef}
                >
                    {props.children}
                </div>
            )}
        </Draggable>
    );
}
