export REACT_APP_VERSION=`cat version`
mvn package
cp -f ./magreport-backend/target/magreport-backend-2.0.jar ./package/magreport.jar
cp -f ./magreport-backend/profiles/application.properties.template ./package/application.properties.template
cp -f ./magreport-backend/profiles/logback.xml.template ./package/logback.xml.template
cp -f ./docs/user-manual/src/user-manual.pdf ./package/user-manual.pdf
