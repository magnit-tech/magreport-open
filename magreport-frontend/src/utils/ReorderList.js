/**
 * Переупорядочивание списка при перемещении элемента.
 * Возвращает новый переупорядоченный список.
 * @param {Array} srcList - Исходный список
 * @param {number} movingInd - Индекс перемещаемого элемента (от 0 до length-1)
 * @param {number} tgtInd - Индекс в массиве, в котором должен быть перемещаемый элемент (от 0 до length-1)
 */
export default function ReorderList(srcList, movingInd, tgtInd){
    const list = Array.from(srcList);
    const movingElement = list[movingInd];
    list.splice(movingInd, 1);
    list.splice(tgtInd, 0, movingElement);

    list.forEach((item, i) => {
        item['ordinal'] = i
    })

    return list;
}
