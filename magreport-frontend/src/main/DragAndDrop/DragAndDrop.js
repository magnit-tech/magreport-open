import React, {useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';

/**
 * @callback onDropInto
 * @param e
 * @param {number} id
 * @param {string} idIncoming
 * @param {number} groupIdIncoming
 */

/**
 * @callback onDropBefore
 * @param e
 * @param {number} id
 * @param {string} idIncoming
 * @param {number} groupIdIncoming
 */

/**
 * @callback isDropAllowedExt
 * @param e
 * @param {number} id
 * @param {string} idIncoming
 */

/**
 * Компонент, реализующий логику Drag and drop
 * @param {Object} props - свойства компонента
 * @param {Array} props.children - дочерние элементы компонента
 * @param {number|string} props.id - Обязательный параметр. Без него функция DnD будет отключена (нельзя переносить ни сам элемент, ни в/перед него).
 * @param {number|string} props.groupId - Виртуальная категория элементов. Нужна т.к. draggable элементы можно переносить не в каждый droppable элемент. Если не указать, то можно перетаскивать только туда, где droppableGroups == null.
 * @param {string[]} props.droppableGroups - Массив groupId, которые можно дропать в этот элемент. Если null, тогда разрешено перетаскивать все draggable элементы.
 * @param {boolean} props.draggable - Флаг: можно ли перетаскивать текущий элемент.
 * @param {boolean} props.dropVirtual - Флаг: пропустить физический перенос элемента, только выполнить ф-ю onDrop*. Для тех случаев, когда элемент является дочерним только логически, но не в HTML DOM.
 * @param {boolean} props.dropInto - Флаг: можно ли дропать элементы в текущий элемент и делать их дочерними.
 * @param {boolean} props.dropBefore - Флаг: можно ли дропать элементы в текущий элемент и ставить их по соседству перед текущим элементом. При наличии этого флага выше текущего элемента будет рисоваться div высотой 1px.
 * @param {onDropInto} props.onDropInto - function(e, id, idIncoming, groupIdIncoming)   - Действия, которые нужно выполнить, когда на текущий элемент перетащили другой элемент (в качестве потомка).
 * @param {onDropBefore} props.onDropBefore - function(e, id, idIncoming, groupIdIncoming)   - Действия, которые нужно выполнить, когда на текущий элемент перетащили другой элемент (в качестве sibling).
 * @param {isDropAllowedExt} props.isDropAllowedExt - function(e, id, idIncoming)   - Пользовательская процедура для проверки можно ли на текущий элемент перетаскивать другой элемент
 */
function DragAndDrop(props) {
    const id = props.id;
    const groupId = props.groupId;
    const droppableGroups = props.droppableGroups;
    const droppableGroupsNVL = (!props.droppableGroups) ? [] : props.droppableGroups;
    const draggable = (props.draggable) && (props.id);
    const dropVirtual = props.dropVirtual;
    const dropInto = props.dropInto && (props.id);
    const dropBefore = props.dropBefore && (props.id);
    const onDropInto = props.onDropInto ?? (() => {/*empty*/
    });
    const onDropBefore = props.onDropBefore ?? (() => {/*empty*/
    });
    const isDropAllowedExt = props.isDropAllowedExt ?? (() => {
        return true;
    });
    const [isDropAreaVisible, setIsDropAreaVisible] = useState(false);
    const [isDraggedOver, setIsDraggedOver] = useState(false);

    const useStyles = makeStyles(theme => ({
        dropAreaHidden: {
            height: "0px",
        },
        dropAreaPreview: {
            height: "10px",
        },
        dropAreaVisible: {
            height: "20px",
            backgroundColor: theme.palette.action.focus,
            filter: "blur(2px)",
            transition: "height .05s"
        }
    }));

    const classes = useStyles();

    /**
     * Проверка возможности сделать drop другого элемента на проверяемый
     * @param {number|string} id              - id текущего элемента (куда пытаются перетащить другой элемент)
     * @param {number|string} idIncoming      - id элемента, который пытаются перетащить на текущий элемент
     * @param {boolean} dropCondition   - доп. логическое условие (например, dropBefore/dropInto)
     */
    function isDropAllowed(id, idIncoming, dropCondition) {
        let elem = document.getElementById(id);
        let elemIncoming = document.getElementById(idIncoming);

        if ((elem == null) || (elemIncoming == null)) return false; // для защиты от случаев, когда выделили часть текста (не draggable) и попытались ее перетащить

        let groupIdIncoming = elemIncoming.getAttribute("groupId");
        let draggableIncoming = (elemIncoming.getAttribute("draggable") === 'true');

        // сравниваем вложенность родительских элементов (а не самих элементов) из-за того, что в некоторых случаях переносимый блок является прямым родителем только в ReactDOM, но в HTML DOM они идут по отдельным веткам.
        return dropCondition
            && draggableIncoming
            && (id !== idIncoming)
            && (!elemIncoming.parentNode.contains(elem.parentNode)
                || elemIncoming.parentNode === elem.parentNode)
            && ((!droppableGroups)
                || (droppableGroupsNVL.includes(groupIdIncoming)));
    }

    const dropAreaClass = isDropAreaVisible
        ? classes.dropAreaVisible
        : isDraggedOver
            ? classes.dropAreaPreview
            : classes.dropAreaHidden;

    return (
        <div
            onDragEnter={(e) => {
                e.stopPropagation();
                setIsDraggedOver(true);
            }}
            onDragLeave={(e) => {
                const container = e.currentTarget;
                const rect = container.getBoundingClientRect();

                const cursorX = e.pageX;
                const cursorY = e.pageY;

                // ignore if cursor is positioned inside this container div
                if(rect.x < cursorX && cursorX < (rect.x + rect.width)
                && rect.y < cursorY && cursorY < (rect.y + rect.height)) {
                    return;
                }
                setIsDraggedOver(false);
            }}
            onDrop={(e) => {
                setIsDraggedOver(false);
            }}
        >
            {(dropBefore)
                ? <div
                    className={dropAreaClass}
                    onDragOver={(e) => {
                        if (e.dataTransfer.types.includes("text/plain")) {
                            e.preventDefault();
                            !isDropAreaVisible && setIsDropAreaVisible(true);
                        }
                    }}
                    onDragLeave={(e) => {
                        setIsDropAreaVisible(false);
                    }}
                    onDrop={(e) => {
                        const idIncoming = e.dataTransfer.getData("text/plain");
                        setIsDropAreaVisible(false);
                        setIsDraggedOver(false);

                        if (isDropAllowed(id, idIncoming, dropBefore) && isDropAllowedExt(e, id, idIncoming)) {
                            e.preventDefault();
                            let groupIdIncoming = document.getElementById(idIncoming).getAttribute("groupId");

                            if (!dropVirtual) {
                                let pnode = document.getElementById(id).parentNode.parentNode;
                                pnode.insertBefore(document.getElementById(idIncoming).parentNode, document.getElementById(id).parentNode);
                            }
                            onDropBefore(e, id, idIncoming, groupIdIncoming);
                        }
                    }}
                >
                </div>
                : null}
            <div id={id}
                 groupid={groupId}
                 draggable={(draggable) ? 'true' : 'false'}
                 onDragStart={(e) => {
                     e.dataTransfer.setData("text/plain", e.target.id);
                     e.stopPropagation();
                 }}
                 onDragOver={(e) => {
                     if (e.dataTransfer.types.includes("text/plain")) {
                         e.preventDefault();
                     }
                 }}
                 onDrop={(e) => {
                     const idIncoming = e.dataTransfer.getData("text/plain");
                     setIsDropAreaVisible(false);
                     setIsDraggedOver(false);

                     if (isDropAllowed(id, idIncoming, dropInto) && isDropAllowedExt(e, id, idIncoming)) {
                         e.preventDefault();
                         let groupIdIncoming = document.getElementById(idIncoming).getAttribute("groupId");

                         if (!dropVirtual) {
                             e.target.appendChild(document.getElementById(idIncoming).parentNode);
                         }
                         onDropInto(e, id, idIncoming, groupIdIncoming);
                     }
                 }}
            >
                {props.children}
            </div>
        </div>
    );
}

export default DragAndDrop;
