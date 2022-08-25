* alert - _данные для Alert_
    * data
        * open - _признак открытия окна_
        * title - _заголовок окна_
        * text - _текст сообщения_
        * buttons - _массив кнопок. Например, [{'text':'OK','onClick':handleDialogClose}]_
        * callback - _ссылка на функцию, которая отработает при закрытии окна_
* alertDialog - _данные для AlertDialog_
    * data
        * open - _признак открытия окна_
        * title - _заголовок окна_
        * entity - _сущность_        
        * entityType - _тип сущности_
        * callback(answer, entity, entityType)
* currentSidebarItemKey - _ключ текущего пункта бокового меню_
* reportsMenuView - _данные предствления пункта меню "Отчёты"_
    * flowState - _состояние жизненнего цикла_
    * currentFolderId - _id текущей папки. null для корневой папки_
    * currentFolderData - _объект data текущей папки (соответствует объекту, получаемому от get-folder)_
    * folderContentLoadErrorMessage - _сообщение об ошибке загрузки содержимого папки_