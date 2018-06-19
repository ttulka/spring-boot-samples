## Run Consul
```
consul agent -dev -config-dir consul.d
```

## Run the Client Application 
```
mvn clean package
java -Dserver.port=8080 -jar target\spring-boot-consul-0.0.0.jar
```
```
http://localhost:8080/discoveryClient
```