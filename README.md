# School-console-app
pet project
this project have 3  entity 
database postgresql
DAO layer at first realization on JDBC, than rewrite using hibernate, than add spring data jpa
service layer  is adapted to use spring data jpa via pattern adapter
console menu build using pattern Facade
tests write using junit5, mockito and testcontainers 
test data:

* 10 groups with randomly generated names. The name should contain 2 characters, hyphen, 2 numbers

* Create 10 courses (math, biology, etc)

* 200 students. Take 20 first names and 20 last names and randomly combine them to generate students.

* Randomly assign students to groups. Each group could contain from 10 to 30 students. It is possible that some groups will be without students or students without groups

* Create the MANY-TO-MANY relation  between STUDENTS and COURSES tables. Randomly assign from 1 to 3 courses for each student
