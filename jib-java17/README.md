# Spring Boot with Jib for Java 17

Build image:
```
./gradlew jibDockerBuild --info -Djib.to.image=spring-boot-jib-java17
```

Run container:
```
docker run --rm --name spring -p 8080:8080 spring-boot-jib-java17
```

Inspect Java environment:
```
docker exec spring java -version
```