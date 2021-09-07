# Mancala Game

- This project has been implemented as an assessment for **bol.com**.

- Technologies/libraries used: <br>
Java 8, Spring boot, Spring cloud, MapStruct, Swagger-UI, H2 DB, JUnit 5.



**Swagger URL:** http://localhost:8080/api/mancala/swagger-ui/index.html

**Project packages:**
- io.nassar.game.mancala: _Parent package for all below._<br>
  - .config: _Contains configuratino classes._<br>
  - .controller: _Contains API related classes (rest controllers, controller advisors, ...)._<br>
  - .domain: _Contains the domain/entity classes._ <br>
  - .dto: _Contains the request/response DTOs/POJOs and their mappers (domain <-> DTO)._
  - .exception: _Contains the custom exception classes._
  - .repository: _Contains the repositories interfaces._
  - .service: _Contains service/business classes._
  - MancalaAPIApplication: _Represents the driver/main class of this project._


**Database:** <br> 
  - URL: http://localhost:8080/api/mancala/h2-console
with below configuratinos:
    - Driver class: org.h2.Driver
    - JDBC URL: jdbc:h2:mem:mancala-db
    - User Name: sa
    - Password: password
  - ER Diagram: <br>

  ![erDiagram.png](erDiagram.png)