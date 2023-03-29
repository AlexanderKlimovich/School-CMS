package com.klymovych.school.consoleViev;

import com.github.javafaker.Faker;

import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import com.klymovych.school.consoleViev.validators.StudentValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class MakerDataToDataBase {

    private final StudentValidator studentValidator;
    private final GroupValidator groupValidator;

    private final CourseValidator courseValidator;
    private static final int AMOUNT_OF_STUDENTS = 200;
    private static final int MAX_OF_COURSES = 4;
    private static final int AMOUNT_OF_COURSES = 10;
    private static final int MIN_STUDENTS_IN_GROUP = 10;
    private static final int MAX_STUDENTS_IN_GROUP = 20;

    public MakerDataToDataBase(StudentValidator studentValidator, GroupValidator groupValidator, CourseValidator courseValidator) {
        this.studentValidator = studentValidator;
        this.groupValidator = groupValidator;
        this.courseValidator = courseValidator;
    }

    public List<Student> makeStudents() throws ServiceException {
        Faker faker = new Faker();
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= AMOUNT_OF_STUDENTS; i++) {
            students.add(new Student(faker.name().firstName(), faker.name().lastName()));
        }
        return students;
    }

    public List<Student> addCourseToStudent(List<Student> students, List<Course> courses) throws ServiceException {
        studentValidator.validate(students);
        courseValidator.validate(courses);
        for (Student student : students) {
            int courseCount = 1 + new Random().nextInt(MAX_OF_COURSES);
            List<Course> coursesOfStudent= new ArrayList<>();
            for (int i = 0; i < courseCount; i++) {
                int randomIndex = new Random().nextInt(AMOUNT_OF_COURSES);
                coursesOfStudent.add(courses.get(randomIndex));
            }
            student.setCourse(coursesOfStudent);
        }
        return students;
    }

    public List<Student> addStudentsToGroup(List<Student> students, List<Group> groups) throws ServiceException {
        studentValidator.validate(students);
        groupValidator.validate(groups);
        int studentCounter = 1;
        for (Group group : groups) {
            int studentCount = MIN_STUDENTS_IN_GROUP + new Random().nextInt(MAX_STUDENTS_IN_GROUP);
            for (int i = 0; i <= studentCount; i++) {
                if ((AMOUNT_OF_STUDENTS - studentCounter) > studentCount) {
                    students.get(i).setGroup(group);
                    studentCounter++;
                }
            }
        }
        for(Student student:students){
                student.setGroup(new Group(new Random().nextInt(10)+1));
        }
        return students;
    }

    public List<Group> makeGroups() throws ServiceException {
        Random r = new Random();
        List<String> groupNames = new ArrayList<>();
        int boundForChars = 26;
        int maxBound = 90;
        int minBound = 10;
        for (int i = 0; i < minBound; i++) {
            groupNames.add(String.format("%s%s - %d", ((char)(r.nextInt(boundForChars) + 'a')),
                    ((char)(r.nextInt(boundForChars) + 'a')), (r.nextInt(maxBound)+minBound)));
        }
        return groupNames.stream().map(Group::new).collect(Collectors.toList());
    }

    public List<Course> makeCourses(String fileCoursesNames) throws ServiceException, IOException {
        Optional.ofNullable(fileCoursesNames).orElseThrow(() -> new IllegalArgumentException(
                "File CoursesNames can't be null"));
        Optional.ofNullable(getClass().getClassLoader().getResource(fileCoursesNames)).orElseThrow(() -> new NoSuchFileException(
                String.format("File %s not found", fileCoursesNames)));
        Reader in = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileCoursesNames));
        List<Course> courses = new ArrayList<>();
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader("courseName", "courseDescription").withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            courses.add(new Course(record.get("courseName"), record.get("courseDescription")));
        }
        return courses;
    }

}
