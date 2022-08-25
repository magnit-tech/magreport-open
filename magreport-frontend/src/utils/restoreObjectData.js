/**
 * Восстанавливает один объект по данным из другого объекта:
 * - восстанавливаются значения всех перечислимых полей объекта и цепочки протипов, кроме функций
 * - восстанавливаются поля примитивных типов, объектных типов и массивов, функции не восстанавливаются
 * - если тип поля источника и приёмника отличаются - поле не восстанавливается (в частности не восстанавливаются поля со значением undefined)
 * - массивы восстанавливаются следующим образом: 
 * если у объекта есть свойство _arrayElementConstructor и в этом объекте есть поле с названием как у поля массива, являющееся функцией
 * то каждый элемент перед копированием создаётся в целевом массиве при помощи этого конструктора и потом рекурсивно восстанавливается из объекта-источника
 * иначе массив восстанавливается поэлементным копированием объектов
 * - поля типа объект восстанавливаются рекурсивно, если восстанавливаемое поле не равно null, иначе поле копируется полностью (неглубокое копирование)
 * @param {object} destObj - восстанавливаемый объект
 * @param {object} srcObj - объект, из которого происходит восстановление
 */
export default function restoreObjectData(destObj, srcObj){
    if( (typeof destObj !== "object") || (typeof srcObj !== "object")){
        return;
    }

    let taskStack = [];
    taskStack.push({destObj, srcObj}); // стек для реализации рекурсии
    while(taskStack.length > 0){
        let nextTask = taskStack.pop();
        for(let p in nextTask.destObj){
            if( (p in nextTask.srcObj) &&
                (p !== "_arrayElementConstructor") &&
                (typeof nextTask.destObj[p] === typeof nextTask.srcObj[p]) && 
                (typeof nextTask.destObj[p] !== "function") ){

                if(typeof nextTask.destObj[p] === "object"){
                    if(nextTask.destObj[p] === null){
                        nextTask.destObj[p] = {...nextTask.srcObj[p]};
                    }
                    else if(Array.isArray(nextTask.destObj[p]) && Array.isArray(nextTask.srcObj[p])){
                        if(nextTask.destObj.hasOwnProperty("_arrayElementConstructor") &&
                          (typeof nextTask.destObj["_arrayElementConstructor"] === "object") &&
                          (typeof nextTask.destObj["_arrayElementConstructor"][p] === "function")){

                            nextTask.destObj[p] = new Array();
                            for(let elm of nextTask.srcObj[p]){
                                let newElement = new nextTask.destObj["_arrayElementConstructor"][p]();
                                nextTask.destObj[p].push(newElement);
                                taskStack.push({destObj: newElement, srcObj: elm});
                            }
                        }
                        else{
                            nextTask.destObj[p] = Array.from(nextTask.srcObj[p]);
                        }
                    }
                    else{
                        taskStack.push({destObj: nextTask.destObj[p], srcObj: nextTask.srcObj[p]});
                    }
                }
                else{
                    nextTask.destObj[p] = nextTask.srcObj[p];
                }       
            }
        }
    }
}