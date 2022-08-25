# Работа с заглушкой LDAP-сервера ldap-server-mock

## Страница проекта

https://www.npmjs.com/package/ldap-server-mock

## Установка

npm install ldap-server-mock

## Файлы конфигурации

Файлы также размещены в текущем каталоге в репозитории. При запуске необходимо файлы разместить в каталоге запуска LDAP-сервера (можно просто выполнить запуск из данного каталога репозитория).

### ldap-server-mock-conf.json

    {
      "port": 3004,
      "searchBase": "dc=test"
    }

### users.json

    [
      {
        "dn": "cn=user,dc=test",
        "objectClass": "person",
        "cn": "user",
        "attribute1": "value1",
        "attribute2": "value2"
      } 
    ]
    
## Запуск LDAP-сервера

    npx ldap-server-mock --conf=./ldap-server-mock-conf.json --database=./users.json
    
## Использование в Магрепорт

Секцию *AD properties* application.properties необходимо записать следующим образом:

    spring.ldap.url=ldap://localhost:3004
    spring.ldap.base=dc=test
    ldap-settings.group-path=dc=test
    ldap-settings.user-path=dc=test
    ldap-settings.type=LDAP
    ldap-settings.user-template-dn=cn={0},dc=test
    
Для создания новых пользователей в Магрепорт выполняем следующую последовательность действий:
- Делаем попытку логина в Магрепорт под создаваемым логином. Магрепорт создаст пользователя, так как LDAP-сервер ответит положительно, но Магрепорт не пустит, так как у пользователя нет прав.
- Заходим в Магрепорт под суперпользователем (указан в параметре superuser-param-name файла application.properties) и выдаём созданному пользователю нужные права (ADMIN, USER или DEVELOPER)
- Выходим из под суперпользователя и логинимся под созданным пользователем.