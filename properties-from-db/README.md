# Configuration Properties from the Database

A sample Spring-Boot application loading its `@ConfigurationProperties` from a database.

## Build
```
mvn clean install
```

## Run
```
mvn spring-boot:run -f app\pom.xml
```

## Try it
1. Default greeting
    ```
    > curl http://localhost:8080/hello
    Hello!
    ```
2. Update the greeting
    ```
    > curl http://localhost:8080/hello?greeting=Ahoy!
    ```
3. Check the updated greeting
    ```
    > curl http://localhost:8080/hello
    Ahoy!
    ```
4. Restart the application
    ```
    ^C
    mvn spring-boot:run -f app\pom.xml
    ```
5. Check the greeting loaded from the database
    ```
    > curl http://localhost:8080/hello
    Ahoy!
    ```