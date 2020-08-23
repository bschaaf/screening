# Screening #

Good day,

This implementation is done with Spring Boot and Postgres. When de Spring boot application starts the songs.json file is read from the TeamRockstar site and the records are stored in the Postgres database. The database has three tables: users, songs and artists. From each song there is a reference (foreign key) to the corresponding artist. Genre is stored in the artists table allthough it could be argued that it should be stored in the songs table, e.g. in case artists perform multiple genres.

There is a Docker Compose file, screening.yml, in the root which runs three containers: the Postgres database, PgAdmin for database management and the Spring Boot app which exposes the REST api and performs the main business logic. To start the containers type: docker-compose -f screening.yml up. On localhost:8090 there is PgAdmin and on localhost:8080/swagger-ui.html the REST api can be accessed with Swagger (Open API). These ports can of course be modified in screening.yml. PgAdmin requires you to login (postgres@gmail.com/password) and the first time the database can be configured using the link pg-server.



## Configuration ##

In application.properties the following properties can be configured:

- spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
- #spring.datasource.url=jdbc:postgresql://pg-server:5432/postgres
- spring.datasource.username=postgres
- spring.datasource.password=password
 
rockstars.baseUrl=https://www.teamrockstars.nl/sites/default/files
rockstars.songFile=songs.json



## Execution ##

Besides with Docker compose he application can be run with `mvn spring-boot:run` and also as standalone jar with `java -jar app.jar`.
 

For any clarification: benoit.schaaf@yahoo.com, +31625051061.

Best regards,
Benoît

