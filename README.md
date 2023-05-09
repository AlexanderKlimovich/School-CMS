# School Console Application
This is a School Console Application developed using Spring Boot and various technologies like JDBC, Hibernate, and Spring Data JPA. The application allows you to manage groups, students, and courses in a school database. You can perform operations such as adding/deleting students, finding groups with specific criteria, assigning students to courses, and more.

# Technologies Used
Java
Spring Boot
JDBC
Hibernate
Spring Data JPA
PostgreSQL
Testcontainers
Flyway

# Getting Started
To get started with the School Console Application, follow these steps:
Clone the repository: git clone <repository-url>
Open the project in your favorite IDE.
Configure the database connection in the application.properties file.
Build the project using Maven 
Run the application.
  
# Database Setup
Before running the application, make sure to set up the database and tables. The application uses PostgreSQL as the database. Follow these steps:
Create a new PostgreSQL database or Docker container with a database.
Create a new user with all privileges on the database.
Set the database credentials in the application.properties file.
  
# Database Migration
The application uses Flyway for database migration. The necessary SQL scripts for creating tables and populating test data are included in the src/main/resources/db/migration directory. When the application starts, Flyway will automatically execute these scripts and migrate the database to the latest version.
  
# Usage
Once the application is up and running, you can use the console menu to interact with the School Console Application. The menu provides the following options:
Find all groups with less or equal student count
Find all students related to a course by name
Add a new student
Delete a student by student ID
Add a student to a course from a list
Remove a student from one of their courses
Select the desired option by entering the corresponding number.
  
# Testing
The project includes comprehensive unit tests and integration tests for the DAO and Service layers. The tests cover all the functionalities mentioned in the requirements and ensure the correct behavior of the application. The tests use Testcontainers to set up a test database and verify the functionality of the application.
To run the tests, use the following command:
mvn test

