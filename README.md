# beer-crudl
A CRUDL for beers

## Build instructions
You can run this project manually (with a jar file) or using Docker.

### Build a docker image

- Prerequisites: java 11, maven and docker

Run the following command in the root folder:

```
./mvnw spring-boot:build-image -DskipTests
```

The simplest way to run the project on docker is with the following command:

```
docker run -p 8080:8080 fachini/beer-crudl:latest
```

Or if you want to point to a different database:

```
docker run -p 8080:8080 -e DATASOURCE_URL=jdbc:postgresql://myserver:5432/mydb -e DATASOURCE_USER=my-user -e DATASOURCE_PASS=my-pass -e DB_SCHEMA=my-schema fachini/beer-crudl:latest
```

### Build a simple jar file

- Prerequisites: java 11, maven

If necessary, change database configurations on application.properties file.  

Run the following command in the root folder. It will build and run the application on port 8080.

```
./mvnw spring-boot:run
```

After run the application, go to http://localhost:8080 to access the front-end interface.

## API documentation

The services and functionalities are described in the API itself. After run de application, go to: http://localhost:8080/swagger-ui.html