SET /P REACT_APP_VERSION=< version
call mvn package
move .\magreport-backend\target\magreport-backend-2.0.jar .\package\magreport.jar
move .\magreport-backend\profiles\application.properties.template .\package\application.properties.template
move .\magreport-backend\profiles\logback.xml.template .\package\logback.xml.template
move .\docs\user-manual\src\user-manual.pdf .\package\user-manual.pdf
