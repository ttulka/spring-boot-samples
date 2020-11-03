# Redis Message Broker with Spring Boot Demo

## Start Redis Server
```
docker run --rm --name redis-broker -p 6379:6379 -d redis redis-server
```

## Run Application
```
gradle bootRun
```