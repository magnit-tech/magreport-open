export REACT_APP_VERSION=`cat version`
mvn package
mv .\magreport-backend\target\magreport-backend-2.0.jar .\package\magreport.jar
mv .\magreport-backend\profiles\application.properties.template .\package\application.properties.template
mv .\magreport-backend\profiles\logback.xml.template .\package\logback.xml.template
mv .\docs\user-manual\src\user-manual.pdf .\package\user-manual.pdf
