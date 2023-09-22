# document-processing-service

pring Boot CSV Batch Processor
Spring Boot
Java
Database
License

This is a Spring Boot web application that reads chunks of CSV data and writes user information (email and phone) to a PostgreSQL database using Spring Batch.

Table of Contents
Prerequisites
Getting Started
Clone the Repository
Configuration
Running the Application
Project Structure
Database Setup
Profiles
Deployment
License
Prerequisites
Before you can run this application, you need to have the following installed on your machine:

Java 8
PostgreSQL
Gradle
Getting Started
Clone the Repository
bash
Copy code
git clone https://github.com/MasumCse2k12/document-processing-service.git
cd your-repo
Configuration
Modify the application configuration as needed. You can find the configuration files in the src/main/resources directory.

Running the Application
To run the application, use Gradle:

bash
Copy code
gradle bootRun
The application will start, and you can access it at http://localhost:8080.

Project Structure
Here's an overview of the project structure:

Database Setup
You'll find SQL scripts in the db-scripts directory to set up the PostgreSQL database. Refer to the README in that folder for database setup instructions.

Profiles
This project supports different deployment profiles (e.g., dev, prod). You can configure profile-specific settings in the profiles folder.

Deployment
For deployment instructions and additional details, refer to the deployment folder.
