# Spring Boot Application For Add-Ons

Add-Ons can be put into the application just be moving JARs into the defined location.

Add-Ons are dynamically loaded into the Spring Boot application.

## Build 

```
mvn clean install

mkdir libs

cp addon1/target/*.jar libs/ \
    & cp addon1-spring-boot-starter/target/*.jar libs/
cp addon2/target/*.jar libs/ \
    & cp addon2-spring-boot-starter/target/*.jar libs/
```

## Start Application

```
LOADER_PATH=./libs java -jar application/target/addons-application-1.0-SNAPSHOT.jar

## as `./libs` is default value (see loader.properties), the same could be achieved with: 
java -jar application/target/addons-application-1.0-SNAPSHOT.jar

## to make this work, uncomment the settings for spring-boot-maven-plugin
# LOADER_PATH=../libs mvn -f application/pom.xml spring-boot:run

curl localhost:8080/myapp/addon1
curl localhost:8080/myapp/addon2
```

## Test Add-Ons

Modules `addon1-application` and `addon2-application` are meant to be used only for testing of Add-Ons in isolation. 

They don't contribute in the final product composition in any way.

```
mvn spring-boot:run -f addon1-application/pom.xml
curl localhost:8080/test

mvn spring-boot:run -f addon2-application/pom.xml
curl localhost:8080/test
```
