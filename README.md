
# document-processing-service

# Requirements:
- Jdk 8
- Gradle: https://gradle.org/

# Getting Started
- Inside "src/main/resources/application.properties" please update server, port, postgres server, user+password credentials
- Then write the following commands to compile it on your pc.
- gradle clean
- gradle build -x test (It will create war/jar file under build/libs folder)

- 
## Spring Boot CSV Batch Processor
- This is a Spring Boot web application that reads chunks of CSV data and writes user information (email and phone) to a PostgreSQL database using Spring Batch.

### Table of Contents
- Prerequisites
- Getting Started
- Clone the Repository
- Configuration
- Running the Application
- Project Structure
- Database Setup
- Profiles

### Prerequisites
Before you can run this application, you need to have the following installed on your machine:

- Java 8
- PostgreSQL
- Gradle

### Getting Started
### Clone the Repository
- git clone https://github.com/MasumCse2k12/document-processing-service.git
- cd your-repo

### Configuration
- Modify the application configuration as needed. You can find the configuration files in the src/main/resources directory.

### Running the Application

- The application will start, and you can access it at http://localhost:10310/document-processor-service-1.0/swagger-ui.html

### Project Structure

![image](https://github.com/MasumCse2k12/document-processing-service/assets/12800530/7db7370e-34b4-4b30-87dd-5701a997cbbc)


### Database Setup
- You'll find SQL scripts in the db-scripts directory to set up the PostgreSQL database. Refer to the README in that folder for database setup instructions.

### Profiles
- This project supports different deployment profiles (e.g., dev, prod). You can configure profile-specific settings in the profiles folder.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://docs.gradle.org/current/samples/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.4/gradle-plugin/api/)
* [CSV File Batch Processing] (https://ademola-kazeem.medium.com/how-to-process-large-csv-file-with-spring-batch-cd3ed8f21d09)
* [Spring Batch] (https://medium.com/@devalerek/spring-batch-retrieve-data-from-the-csv-file-and-save-it-to-database-h2-75a689b7370)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
