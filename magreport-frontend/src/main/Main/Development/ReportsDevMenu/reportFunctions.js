// utils
import {randomWordCode} from 'utils/randomWordCode';

export function setRandomCodeForEmptyCode(filterGroup){
    if(filterGroup.code === null || filterGroup.code === undefined || filterGroup.code.trim() === ""){
        filterGroup.code = randomWordCode(6);
    }

    if(filterGroup.childGroups){
        for(let g of filterGroup.childGroups){
            setRandomCodeForEmptyCode(g);
        }
    }

    if(filterGroup.filters){
        for(let f of filterGroup.filters){
            if(f.code === null || f.code === undefined || f.code.trim() === ""){
                f.code = randomWordCode(6);
            }
        }
    }
}

export function getFilters(groupFilters, reportFiltersGroup){
    let haveErrors = false
    let groups = []
    if (groupFilters.haveErrors){
        haveErrors = true
    }
    else {
        for(let cgId of groupFilters.childGroups){
            let cg = reportFiltersGroup.get(cgId)
            
            let {invalid, groupsObj} = getFilters({...cg}, reportFiltersGroup)
            if (invalid){
                haveErrors = true
                break
            }
            else{
                groups.push({...groupsObj})
            }
        }
        for(let f of groupFilters.filters){
            if (f.errors){
                haveErrors = true
                break
            }
        }
        if (!haveErrors){
            groupFilters.childGroups = groups
        }
    }

    return {
        invalid: haveErrors, 
        groupsObj: groupFilters
    }
}

export function checkChanges(field, value) {
    let error = ""
    if (field === "name" && !isNaN(value)) {
        if (value === `${parseInt(value, 10)}`) error = "Недопустимое название поля, добавьте буквы или символ '_'"
    }
    if ((field === "name" ||  field === "description") && value.length > 255) error = "Длина текста более 255 символов"
    return error
}

export function sortByOrdinal(arr) {
    return arr.sort((a, b) => {
        if (a.ordinal > b.ordinal){
            return 1
        }
        if (a.ordinal < b.ordinal){
            return -1
        }
        return 0
    })
}