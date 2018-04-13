# Patient Catalogue Server

## Build
```
mvn clean package
```

## Run the Server
```
java -jar target\patient-catalogue-server-1.0-SNAPSHOT.jar
```

## Usage

### Create a new patient
```
echo {"name":"John Smith", "email":"john.smith@example.com"} > patient.dat
curl localhost:1234 -X POST -H "Content-Type: application/json" -d @patient.dat 
```

### Get a patient
```
curl localhost:1234/<patient-id> -X GET
```

### Update a patient
```
echo {"name":"John Smith Junior", "email":"john.smith.junior@example.com"} > patient.dat
curl localhost:1234/<patient-id> -X PUT -H "Content-Type: application/json" -d @patient.dat
```

### Delete a patient
```
curl localhost:1234/<patient-id> -X DELETE
```

### List of all patients
```
curl localhost:1234 -X GET
```