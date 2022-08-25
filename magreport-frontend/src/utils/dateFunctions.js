export function dateToStringFormat(dt){
    let dd = dt.getDate();
    if(dd < 10){
        dd = '0' + dd;
    }
    let mm = dt.getMonth() + 1;
    if(mm < 10){
        mm = '0' + mm;
    }
    let yy = dt.getFullYear();
    
    return yy + '-' + mm + '-' + dd;
};