SET /P REACT_APP_VERSION=< version
call mvn package
copy /y .\magreport-backend\target\magreport-backend-2.0.jar .\package\magreport.jar
copy /y .\magreport-backend\profiles\application.properties.template .\package\application.properties.template
copy /y .\magreport-backend\profiles\logback.xml.template .\package\logback.xml.template
copy /y .\docs\user-manual\src\user-manual.pdf .\package\user-manual.pdf
