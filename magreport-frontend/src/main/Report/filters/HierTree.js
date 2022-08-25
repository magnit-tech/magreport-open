import React, {useState, useRef, useEffect} from 'react';
import { connect } from 'react-redux';

// components
import TreeView from '@material-ui/lab/TreeView';
import TreeItem from '@material-ui/lab/TreeItem';
import { CircularProgress, Checkbox, Box} from '@material-ui/core';
import ArrowDownwardIcon from '@material-ui/icons/ArrowDownward';
import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FilterNoneTwoToneIcon from '@material-ui/icons/FilterNoneTwoTone';

// dataHub
import dataHub from 'ajax/DataHub';

// local
import FilterStatus from './FilterStatus';
import SidebarItems from '../../Main/Sidebar/SidebarItems' ;

// styles
import {HierTreeCSS, AllFiltersCSS} from './FiltersCSS'

function getTreeNodeId(parentTreeNodeId, nodeId){
    return parentTreeNodeId + "/" + nodeId;
}

function Tree(classes, filterData, onUpdateData, onCheckedChange, strict, checkStatus, readOnly, getChildNodes){
    this.classes = classes;
    this.filterData = filterData;
    this.onUpdateData = onUpdateData;
    this.onCheckedChange = onCheckedChange;
    this.strict = strict;

    // Максимальный уровень в дереве
    this.maxLevel = 0;

    // Поля с типом ID, соответствующее уровню
    this.IDfields = [];
    // Поля с типом CODE, соответствующие уровню
    this.CODEfields = [];

    // Уровни для fieldId с типом CODE
    this.fieldLevels = new Map();
    this.idToCodeMapping = new Map();

    /*
        Анализ полей и уровней
    */
    const cls = AllFiltersCSS();

    for(let f of filterData.fields){
        if(f.level > this.maxLevel){
            this.maxLevel = f.level;
            this.IDfields.push(null);
            this.CODEfields.push(null);
        }
        /*if(curLev > this.maxLevel){
            this.maxLevel = curLev;
        }*/
        if(f.type === 'ID_FIELD'){
            this.IDfields[f.level-1] = f;
            if(this.CODEfields[f.level-1] !== null){
                this.idToCodeMapping.set(f.id /*filterInstanceFieldId*/, this.CODEfields[f.level-1].id /*filterInstanceFieldId*/);
            }
        }
        if(f.type === 'CODE_FIELD'){
            this.fieldLevels.set(f.id, f.level);
            this.CODEfields[f.level-1] = f;
            if(this.IDfields[f.level-1] !== null){
                this.idToCodeMapping.set(this.IDfields[f.level-1].id /*filterInstanceFieldId*/, f.id /*filterInstanceFieldId*/);
            }
        }
    }

    /*
        Поиск узла по treeNodeId
    */
    this.nodesMap = new Map();

    /*
        Поиск и добавление узла
    */

    this.getNode = (treeNodeId) => {
        return this.nodesMap.get(treeNodeId);
    }

    this.addNode = (node) => {
        this.nodesMap.set(node.treeNodeId, node);
    }

    /*
        Создаём корневой элемент
    */
    const name = this.filterData.mandatory ? <span>{this.filterData.name}<i className={cls.mandatory}>*</i></span> : <span>{this.filterData.name}</span>;
    this.root = new TreeNode(this.classes, "root", 0, name, this.CODEfields[0].id /*filterInstanceFieldId*/, null, 
                            (this.maxLevel === 0), 0, this.IDfields[0].expand, this.onCheckedChange, this.onUpdateData, this.loadNodeChildren, null, readOnly);
    this.addNode(this.root);

    this.root.setCheckStatus(checkStatus)
    /* 
        Построить TreeItem для всего дерева
    */
    this.buildTreeItem = () => {
        this.root.setCheckStatus(this.root.checkStatus)
        return this.root.buildTreeItem();
    }

    /*
        Загрузить с сервера данные по дочерним узлам заданного узла
    */
    this.loadNodeChildren = (treeNodeId, callback) => {
        let node = this.getNode(treeNodeId);

        if(node){
            let filterParams = {
                filterId: this.filterData.id, //filterInstanceId,
                pathNodes: [{
                    fieldId : (treeNodeId === "root") ? this.CODEfields[0].id /*filterInstanceFieldId*/ : this.CODEfields[node.level - 1].id /*filterInstanceFieldId*/,
                    value : (treeNodeId === "root") ? null : node.nodeId
                }]
            };
    
            if(!this.strict){
                let parent = node.parent;
                while(parent !== null && parent.treeNodeId !== "root"){
                    filterParams.pathNodes.push({
                        fieldId : this.CODEfields[parent.level - 1].id /*filterInstanceFieldId*/,
                        value : parent.nodeId
                    });
                    parent = parent.parent;
                }
            }

            getChildNodes(filterParams, 
                (magrepResponse) => {this.nodeChildrenLoadedCallback(treeNodeId, magrepResponse, callback);});
            
            if(node.loadStatus === "notLoaded" || node.loadStatus === "loadingFailed"){
                node.loadStatus = "loading";
            }
            
            this.onUpdateData();
        }
    }

    /*
        Метод - callback после загрузки дочерних узлов заданного узла
    */
    this.nodeChildrenLoadedCallback = (treeNodeId, magrepResponse) => {
        let node = this.getNode(treeNodeId);

        if(node){
            if(node.loadStatus === "loading"){
                if(magrepResponse.ok){
                    let data = magrepResponse.data;
                    let checkedChildrenCnt = 0;
                    let partCheckedChildrenCnt = 0;
                    let childrenCnt = 0;
                    for(let child of data.rootNode.childNodes){ 
                        /*
                            Выясняем, нужно ли отмечать данный узел
                        */        
                        
                        let checked = (node.checked === 1) ? 1 : 0;

                        /*
                            Ищем, был ли этот узел добавлен ранее при формировании узлов по умолчанию
                        */
                        let childTreeNodeId = getTreeNodeId(treeNodeId, child.nodeId);
                        let existingChild = this.getNode(childTreeNodeId);
                        if(existingChild){
                            existingChild.nodeName = child.nodeName;
                            checked = existingChild.checked;
                        }
                        else {
                            /*
                                Добавляем новый узел
                            */
                            this.addNode(new TreeNode(this.classes, child.nodeId, child.level, child.nodeName, this.CODEfields[child.level - 1].id, node, 
                                (this.maxLevel === child.level), checked, this.IDfields[child.level - 1].expand , this.onCheckedChange, this.onUpdateData, this.loadNodeChildren, null, readOnly /*, this.getParameters*/));
                            if (this.IDfields[child.level - 1].expand) {
                                this.loadNodeChildren(getTreeNodeId(treeNodeId , child.nodeId));
                            }
                        }

                        if(checked === 1){
                            checkedChildrenCnt++;
                        }
                        else if(checked === 2){
                            partCheckedChildrenCnt++;
                        }
                        childrenCnt++;
                    }
                    node.sortChildren();
                    node.loadStatus = "loaded";
                    node.checkedChildrenCnt  = checkedChildrenCnt;
                    node.partCheckedChildrenCnt = partCheckedChildrenCnt;
                    node.childrenCnt = childrenCnt;
                }
                else{
                    node.loadStatus = "loadFailed";
                }
                this.onUpdateData();
            }
        }
    }

    /*
        Загрузка ещё не загруженных раскрытых узлов
    */
    this.loadExpandedNodes = (treeNodeIdList) => {
        for(let treeNodeId of treeNodeIdList){
            let node = this.getNode(treeNodeId);
            if(node !== undefined && node.loadStatus === "notLoaded"){
                this.loadNodeChildren(treeNodeId);
            }
        }
    }

    /*
        Получение объекта выбора фильтра
    */

    // Для строгой иерархии - возвращает массив массивов
    // НЕ ИСПОЛЬЗУЕТСЯ - оставлено на всякий случай. Сейчас результат выбора и для строгой, и для нестрой иерархии
    // передаётся в едином формате - как для нестрого дерева

    this.getStrictParameters = () => {
        
        let sel = this.root.getStrictSelectionValues();
        let parameters = [];

        for(let i = 1; i < sel.length; i++){
            let values = [];
            for(let v of sel[i]){
                values.push({
                    fieldId: this.CODEfields[i-1].id,
                    value: v
                });
            }
            if(values.length > 0){
                parameters.push({values: values});
            }
        }

        return parameters;

    }

    // Для нестрогой иерархии - вовращает массив parameters для встраивания в объект передачи
    this.getNonstrictParameters = () => {
        let parameters = [];

        this.root.getNonstrictSelectionValues(parameters);

        return parameters;
    }

    this.getParameters = () => {
        return this.getNonstrictParameters();
    }

    this.setCheckStatus = status => {
        this.root.checkStatus = status
    }

    /*
        Задание значений по-умолчанию
    */

    this.setValues = fieldValues => {
        if (fieldValues.parameters.length > 0){
            let path = new Array(this.maxLevel + 1);
            for(let a of fieldValues.parameters){                
                for(let i = 0; i < path.length; i++){
                    path[i] = null;
                }
                // Строим пути по уровням иерархии
                path[0] = "root";
                let cnt = 1;
                for(let f of a.values){
                    let fieldId = f.fieldId;
                    let nodeId = f.value;
                    let level = this.fieldLevels.get(fieldId);
                    path[level] = nodeId;
                    cnt++;
                }
                // Проверяем корректность пути - чтобы не было пробелов
                let complete = true;
                for(let i = 0; i < cnt && complete; i++){
                    if(path[i] === null){
                        complete = false;
                    }
                }
                // Если всё хорошо - строим узлы 
                if(complete){
                    if(cnt === 1){
                        this.root.setChecked(1);
                    }
                    else{
                        let treeNodeId = path[0];
                        let parent = this.root;
                        for(let i = 1; i < cnt - 1; i++){
                            treeNodeId = getTreeNodeId(treeNodeId, path[i]);
                            // Выясняем - не был ли этот узел добавлен ранее
                            let childNode = this.getNode(treeNodeId);
                            if(childNode === undefined){
                                childNode = new TreeNode(this.classes, path[i], i, "?", this.CODEfields[i - 1].id, parent, 
                                                        (this.maxLevel === i), 2, this.IDfields[i - 1].expand, this.onCheckedChange, this.onUpdateData, this.loadNodeChildren, null, readOnly);
                                this.addNode(childNode);
                            }
                            parent = childNode;
                        }
                        treeNodeId = getTreeNodeId(treeNodeId, path[cnt - 1]);
                        // Если объект выбора значений фильтра формировался корректно при помощи данного объекта дерева,
                        // ситуация, что этот узел уже был добавлен ранее невозможна. Но на всякий случай проверяем.
                        let childNode = this.getNode(treeNodeId);
                        if(childNode === undefined){
                            this.addNode(new TreeNode(this.classes, path[cnt - 1], cnt - 1, "?", this.CODEfields[cnt - 2].id, parent, 
                                                        (this.maxLevel === cnt - 1), 1, this.IDfields[cnt - 2].expand, this.onCheckedChange, this.onUpdateData, this.loadNodeChildren, null, readOnly ));
                        }
                        else{
                            childNode.setChecked(1);
                        }
                    }
                }
            }
            
            // Если добавлены дочерние вершины и корень не отмечен - отмечаем корень, как частично отмеченный
            if(this.root.children.length > 0 && this.root.checked === 0){
                this.root.setChecked(2);
            }
        }
        else {
            if (this.root.checked !== 0){
                this.root.setChecked(0);
            }
        }
    }

}

/*
    Узел дерева
    checked:
        0 - не отмечен сам и не отмечен ни один из потомков
        1 - отмечен сам
        2 - частично отмечен - то есть не отмечен сам, но отмечен один из потомков
*/
function TreeNode(classes, nodeId, level, nodeName, fieldId, parent, isLeaf, checked, expand, onCheckedChange, onSortOrderChanged, loadNodeChildren, loadStatus, readOnly, from){
    this.classes = classes;
    this.nodeId = nodeId;
    this.level = level;
    this.nodeName = nodeName;
    this.fieldId = fieldId;
    this.parent = parent;
    this.isLeaf = isLeaf;
    this.checked = checked;
    this.expand = expand;
    this.onCheckedChange = onCheckedChange;
    this.onSortOrderChanged = onSortOrderChanged;
    this.loadNodeChildren = loadNodeChildren;

    if(parent !== null){
        parent.children.push(this);
    }

    this.ascSortOrder = true;
    // nodeId уникален только в пределах данного уровня. Конструируем уникальный в пределах всего дерева treeNodeId
    if(parent != null){
        this.treeNodeId = getTreeNodeId(parent.treeNodeId, this.nodeId);
    }
    else{
        this.treeNodeId = nodeId;
    }
    

    /*
        loadStatus - статус загрузки дочерних узлов:
            notLoaded
            loading
            loaded
            loadFailed
    */
    if(loadStatus){
        this.loadStatus = loadStatus;
    }
    else{
        this.loadStatus = "notLoaded";
    }

    this.children = [];
    // Кол-во полностью отмеченных дочерних узлов
    this.checkedChildrenCnt = 0;
    // Кол-во частично отмеченных дочерних узлов
    this.partCheckedChildrenCnt = 0;
    //Кол-во дочерних узлов
    this.childrenCnt = 0;

    this.addChild = (node) => {
        this.children.push(node);
    }

    /*
        Изменение checked узла в связи с изменением checked дочернего узла
    */
    this.processChildCheckedChange = (oldChildChecked, newChildChecked) => {

        if(newChildChecked === 0){
            if(oldChildChecked === 1){
                this.checkedChildrenCnt--;
            }
            else if(oldChildChecked === 2){
                this.partCheckedChildrenCnt--;
            }
        }
        else if(newChildChecked === 1){
            if(oldChildChecked === 0){
                this.checkedChildrenCnt++;
            }
            else if(oldChildChecked === 2){
                this.checkedChildrenCnt++;
                this.partCheckedChildrenCnt--;
            }
        }
        else if(newChildChecked === 2){
            if(oldChildChecked === 0){
                this.partCheckedChildrenCnt++;
            }
            else if(oldChildChecked === 1){
                this.checkedChildrenCnt--;
                this.partCheckedChildrenCnt++;
            }
        }

        let newChecked = this.checked;

        if(this.checkedChildrenCnt === 0 && this.partCheckedChildrenCnt === 0){
            newChecked = 0;
        }
        else if(this.checkedChildrenCnt === this.children.length){
            newChecked = 1;
        }
        else{
            newChecked = 2;
        }

        if(newChecked !== this.checked){
            if(this.parent){
                parent.processChildCheckedChange(this.checked, newChecked);
            }
            this.checked = newChecked;
        }
    }

    /*
        Изменение checked узла в связи с изменением checked родительского узла
    */
    this.processParentCheckedChange = (newParentChecked) => {
        if(newParentChecked !== this.checked && newParentChecked !== 2){
            this.checked = newParentChecked;
            // fix #708
            if (newParentChecked === 0) {
                this.checkedChildrenCnt = 0;
                this.partCheckedChildrenCnt = 0;
            }
            if (newParentChecked === 1) {
                this.checkedChildrenCnt = this.children.length;
                this.partCheckedChildrenCnt = 0;
            }
            // end
            for(let child of this.children){
                child.processParentCheckedChange(this.checked);
            }
        }
    }

    this.setChecked = (checked) => {
        if(this.parent){
            parent.processChildCheckedChange(this.checked, checked);
        }
        for(let child of this.children){
            child.processParentCheckedChange(checked);
        }
        this.checked = checked;
        if(checked === 1){
            this.checkedChildrenCnt = this.children.length;
            this.partCheckedChildrenCnt = 0;
        }
        else if(checked === 0){
            this.checkedChildrenCnt = 0;
            this.partCheckedChildrenCnt = 0;
        }
        this.onCheckedChange();
    }

    this.changeChecked = () => {
        let newChecked = (this.checked === 1) ? 0 : 1;
        this.setChecked(newChecked);
    }

    this.handleCheckedChange = (event) => {
        event.stopPropagation();
        this.changeChecked();
    }

    this.sortChildren = () => {
        let ascSortOrder = this.ascSortOrder;

        this.children = this.children.sort(
            function (a, b) {
                if (a.nodeName > b.nodeName) {
                    return ascSortOrder ? 1 : -1;
                }
                if (a.nodeName < b.nodeName) {
                    return ascSortOrder ? -1 : 1;
                }
                return 0;
            }
        );  
    }

    this.changeSortOrder  = (event) => {

        event.stopPropagation();

        this.ascSortOrder = !this.ascSortOrder;

        this.sortChildren();

        this.onSortOrderChanged();
    }    

    this.setCheckStatus = status => {
        this.checkStatus = status
    }

    /*
        Вернуть TreeItem для данного узла
    */
    this.buildTreeItem = () => {

        var content;

        if(this.loadStatus === "notLoaded"){
            if(this.isLeaf){
                content = [];
            }
            else {
                content = <div></div>;
            }
        }
        else if(this.loadStatus === "loading"){
            content = <CircularProgress size={20} thickness={5}/>;
        }
        else if(this.loadStatus === "loaded"){
            content = [];

            for(let child of this.children){
                content.push(child.buildTreeItem());
            }
        }
        else if(this.loadStatus === "loadFailed"){
            content = <p>Ошибка загрузки данных</p>;
        }

        return (
            <TreeItem key = {this.treeNodeId} nodeId = {this.treeNodeId} 
                classes={{label: classes.treeItem}}
                label = {
                    <div style={{ display: "flex", justifyContent: "space-between"}} /*className={classes.labelTreeItem}*/>
                        <FormControl
                        //Здесь нельзя переделать на ClassName, т.к. при смене темы слетают классы.
                        //Только здесь и причина не понятна пока
                            style={{display: 'flex',
                                flexDirection: 'row',
                                alignItems: 'center',
                            }}
                        >
                            <FormControlLabel
                                control = {
                                <Checkbox 
                                    //style={{ color: 'green'}}
                                    color="primary"
                                    indeterminate = {this.checked === 2}
                                    indeterminateIcon ={<FilterNoneTwoToneIcon color='primary'/>}
                                    checked = {this.checked === 1}
                                    disabled={readOnly}
                                    onClick={this.handleCheckedChange}
                                /> }
                                
                                label={this.nodeName}
                                labelPlacement = 'end'
                            />
                            {
                                !this.isLeaf && (this.ascSortOrder ? 
                                    <ArrowDownwardIcon color='primary' size='small' onClick={this.changeSortOrder}/> :  
                                    <ArrowUpwardIcon color='primary' size='small' onClick={this.changeSortOrder}/>)
                            }
                        </FormControl>
                        {   
                            !readOnly && this.checkStatus &&
                            <Box 
                                display="flex"
                                alignItems="center"
                                justifyContent="center"
                            >
                                <FilterStatus status={this.checkStatus} />
                            </Box>
                        }
                    </div>
                }
                children = {content}
            />
        );
    }

    /*
        Возврат результата выбора значений фильтра для строгой иерархии, начиная с данного узла.
        Возвращает данные в виде списка списков:
        res[0] : list nodeId для данного узла (если он выбран)
        res[1] : list nodeId для непосредственных дочерних узлов
        и т.д.

        НЕ ИСПОЛЬЗУЕТСЯ - оставлено на всякий случай. Сейчас результат выбора и для строгой, и для нестрой иерархии
        передаётся в едином формате - как для нестрого дерева
    */
    this.getStrictSelectionValues = () => {
        let res = [];
        if(this.checked === 1){
            res.push([this.nodeId]);
        }
        else if(this.checked === 2){
            res.push([]);
            for(let child of this.children){
                let sel = child.getStrictSelectionValues();
                for(let i in sel){
                    if(parseInt(i) + 1 < res.length){
                        res[parseInt(i)+1] = res[parseInt(i)+1].concat(sel[i]);
                    }
                    else{
                        res.push(sel[i]);
                    }
                }
            }
        }

        return res;
    }

    /*
        Возврат результата выбора значений фильтра для нестрогой иерархии, начиная с данного узла.
    */

    this.getNonstrictSelectionValues = (parameters) => {
        if(this.checked === 1 && !this.expand && this.treeNodeId !== "root"){
            let values = [];
            let node = this;
            while(node !== null && node.treeNodeId !== "root"){
                values.push({
                    fieldId : node.fieldId,
                    value : node.nodeId
                });
                node = node.parent;
            }
            parameters.push({values:values});
        }
        else if(this.checked === 2 || (this.checked === 1 && this.expand && this.treeNodeId !== "root" )){
            for(let child of this.children){
                if(child.checked > 0){
                    child.getNonstrictSelectionValues(parameters);
                }
            }
        }
    }

}

/**
 * Компонент настройки иерархического фильтра у отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * @param {boolean} props.toggleClearFilter - при изменении значения данного свойства требуется очистить выбор в фильтре
 * @param {boolean} props.readOnly - если true, то отображает компонент для изменения
 * @param {String} props.currentSidebarItemKey - текущий пункт меню
 * @param {onChangeFilterValue} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра
 *                                                  filterValue - объект для передачи в сервис в массиве parameters
 * @param {boolean} props.strict - true - если дерево со строгой иерархией, false - иначе (отличается форматом запрос дочерних узлов)
 */
function HierTree(props){

    const classes = HierTreeCSS();
    const getChildNodes = props.currentSidebarItemKey === SidebarItems.admin.subItems.securityFilters.key 
        ? dataHub.filterInstanceController.getChildNodes 
        : dataHub.filterReportController.getChildNodes;

    function handleTreeUpdated(){
        setTreeItems(tree.current.buildTreeItem());
    }

    function handleCheckedChange(){
        let parameters = tree.current.getParameters();
        let checked = tree.current.root.checked
        const status = (mandatory && parameters.length) || (!mandatory && checked !== 1) ? "success" : "error"
        
        props.onChangeFilterValue({
            filterId : props.filterData.id,
            operationType: "IS_IN_LIST",
            validation: status,
            parameters : parameters
        });

        tree.current.setCheckStatus(status)
        setCheckStatus(status)
        setTreeItems(tree.current.buildTreeItem());
    }

    const [expandedNodeIdList, setExpandedNodeIdList] = useState([]);
    const mandatory = props.filterData.mandatory;
    const [checkStatus, setCheckStatus] = useState(mandatory ? "error" : "success");
    const tree = useRef(new Tree(classes, props.filterData, handleTreeUpdated, handleCheckedChange, props.strict, checkStatus, props.readOnly, getChildNodes));
    const treeDefaultsSet = useRef(false);// Признак того, что значения по умолчанию установлены
    const [treeItems, setTreeItems] = useState(tree.current.buildTreeItem());
    const [toggleClearFilter, setToggleClearFilter] = useState(false);

    useEffect(() => {
        if(props.lastFilterValue && !treeDefaultsSet.current){
            treeDefaultsSet.current = true;
            tree.current.setValues(props.lastFilterValue);
            handleCheckedChange();
        }
    }, []) // eslint-disable-line

    useEffect(() => {
        if (props.toggleClearFilter !== undefined && props.toggleClearFilter !== toggleClearFilter){        
            treeDefaultsSet.current = true;
            tree.current.setValues({
                filterId : props.filterData.id,
                operationType: "IS_IN_LIST",
                validation: mandatory ? "success" : "error",
                parameters:[]
            });
            
            handleCheckedChange();
            setToggleClearFilter(props.toggleClearFilter);
        }
    }, [props.toggleClearFilter]) // eslint-disable-line

    function handleNodeExpand(event, nodes){
        tree.current.loadExpandedNodes(nodes);
        setExpandedNodeIdList(nodes);
    }

    return(
        <div /*className={classes.treeDiv}*/>
            <TreeView
                className={classes.root}
                defaultCollapseIcon={<ArrowDropDownIcon size="large"/>} //{<ExpandMoreIcon />}
                defaultExpandIcon={<ArrowRightIcon/>} //{<ChevronRightIcon />}
                expanded={expandedNodeIdList}
                onNodeToggle={handleNodeExpand}
            >
                {treeItems}
            </TreeView>
        </div>
    );
}

const mapStateToProps = state => {
    return {
        currentSidebarItemKey: state.currentSidebarItemKey
    }
}

export default connect(mapStateToProps, null)(HierTree);