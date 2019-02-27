# Twitter emulation in Java

The application emulates the main features of Twitter

* log in;
* account management:
  * registration;
  * editing;
  * deleting;
  * search by substring;
* main page:
  * account properties;
  * timeline tweets;
  * account tweets;
  * following accounts;
  * follower accounts;
* tweet creating;
* follow/unfollow.

in several ways:

1. Spring MVC, JSP
1. Spring Boot, JSP
1. Spring Boot, Angular

## Requirements

* [JDK 8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Apache Maven 3.5.0+](https://maven.apache.org/download.cgi)

## Running

### Spring MVC, JSP

1. Change directory:

    `cd twitter-emulation-spring-mvc-jsp`

1. From the command line with *Maven*:

    `mvn jetty:run` (*H2*)  
    `mvn jetty:run -P development` (*H2*)  
    `mvn jetty:run -P production` (*Oracle Database*)  
    (*Oracle Database* connection properties: `etc/jetty.xml`)

1. Access the deployed web application at:

    http://localhost:8080

### Spring Boot, JSP

1. Change directory:

    `cd twitter-emulation-spring-boot-jsp`

1. From the command line with *Maven*:

    `mvn spring-boot:run` (*H2*)  
    `mvn spring-boot:run -P development` (*H2*)  
    `mvn spring-boot:run -P production` (*Oracle Database*)  
    (*Oracle Database* connection properties: `src/main/resources/application.properties`)

1. Access the deployed web application at:

    http://localhost:8080

### Spring Boot, Angular

1. Change directory:

    `cd twitter-emulation-spring-boot-angular-server`

1. From the command line with *Maven*:

    `mvn spring-boot:run` (*H2*)  
    `mvn spring-boot:run -P development` (*H2*)  
    `mvn spring-boot:run -P production` (*Oracle Database*)  
    (*Oracle Database* connection properties: `src/main/resources/application.properties`)

1. Change directory:

    `cd twitter-emulation-spring-boot-angular-web`

1. From the command line with *npm*:

    `npm start`

1. Access the deployed web application at:

    http://localhost:4200

## Frameworks and libraries

* [Spring](https://spring.io):
  * [Spring MVC](https://spring.io/projects/spring-framework)
  * [Spring Boot](https://spring.io/projects/spring-boot)
  * [Spring Security](https://spring.io/projects/spring-security)
  * [Spring Test](https://spring.io/projects/spring-framework)
* [H2](http://www.h2database.com) or [Oracle Database](https://www.oracle.com/database/index.html) (data storage)
* [TestContainers](https://www.testcontainers.org) (testing with [Docker](https://www.docker.com) containers)

## Tools

* [ToDoList 7.1.5+](http://www.abstractspoon.com) (planning)
* [Pencil 3.0.4+](https://pencil.evolus.vn) (GUI prototyping)
* [Violet 0.21.1](http://violet.sourceforge.net) (UML diagrams)
