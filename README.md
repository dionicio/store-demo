This is rest webservice made with spring boot, maven and Java, some of the Spring libraries that were used like jpa, security with json web token and encrypting with RSA algorithm, and web, besides was implemented api doc, and tested with junit, mokito and postman. The database configured was postresql.

The project can be run as standalone, as docker service or kubernetes service.

The configuration for running it with docker is shared inside of the files there's and Dockerfile for creating the image, and a docker compose file for running the database and the app as service.

Step for running the app.

install Docker desktop
1. Ejecute the database with docker compose inside the project postgresql.yaml

    docker volume create data-postgres
    docker-compose -f postgresql.yaml up
2. Clean and install the app

   mvnw.cmd clean install 
   mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--server.port=8080"
   
Technologies Java 17 Spring Boot 3 (with Spring Web MVC, Spring Data JPA) PostgreSQL Maven 3.9
