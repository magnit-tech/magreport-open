import React from 'react';
import clsx from "clsx";
import {DragDropContext, Droppable} from "react-beautiful-dnd";

// local
import DraggableListItem from "./DraggableListItem";
import DraggableInnerItemsList from "./DraggableInnerItemsList";

// styles
import {DragAndDropCSS} from "./DragAndDropCSS";


/**
 * @callback onMoveElement
 * @param {string|number} sourceGroupId - идентификатор контейнера (группы, списка), из которого перетаскивается элемент
 * @param {string|number} sourceIndex - индекс перемещаемого элемента
 * @param {string|number} targetGroupId - идентификатор контейнера (группы, списка), на который перетаскивается элемент
 * @param {string|number} targetIndex - индекс элемента, вместо которого будет помещен элемент
 *
 */
/**
 * Компонент с перемещаемым списком элементов
 * @param {Object} props - свойства компонента
 * @param {Array} props.items - массив элементов списка
 * @param {string} props.groupId - id группы для элементов списка для использования в качестве droppableId у контейнера Droppable
 * @param {string} props.listClass - class элемента списка
 * @param {string} props.listItemClass - class контейнера элемента
 * @param {onMoveElement} props.onMove - callback для события перемещения элемента внутри списка:

 */
export default function DraggableItemsList(props) {
    const classes = DragAndDropCSS();

    let itemIdPrefix = 'draggableItem_' + props.groupId + "_";

    function onDragEnd(result) {
        const {destination, source} = result;

        if (!destination) {
            return;
        }

        if (destination.droppableId === source.droppableId &&
            destination.index === source.index) {
            return;
        }

        if (source.droppableId !== props.groupId) {
            return;
        }

        const sourceGroupId = source.droppableId;
        const sourceIndex = source.index;
        const targetGroupId = destination.droppableId;
        const targetIndex = destination.index
        props.onMove(sourceGroupId, sourceIndex, targetGroupId, targetIndex);
    }


    const listItems = (props.items || {})
        .map((item, index) => (
            <DraggableListItem
                draggableId={itemIdPrefix + item.key}
                key={item.key}
                index={index}
                className={clsx(props.listItemClass, classes.listItem)}
            >
                {item}
            </DraggableListItem>
        ));

    return (
        <DragDropContext
            onDragEnd={onDragEnd}
        >
            <Droppable
                droppableId={props.groupId}
            >
                {provided => (
                    <div
                        ref={provided.innerRef}
                        {...provided.droppableProps}
                        className={props.listClass}
                    >
                        <DraggableInnerItemsList items={listItems} />
                        {provided.placeholder}
                    </div>
                )}
            </Droppable>
        </DragDropContext>
    );
}
