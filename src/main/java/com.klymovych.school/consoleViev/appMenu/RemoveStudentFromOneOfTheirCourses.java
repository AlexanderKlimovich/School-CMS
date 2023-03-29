package com.klymovych.school.consoleViev.appMenu;

import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class RemoveStudentFromOneOfTheirCourses implements Menu {
    private StudentServiceImpl studentService;
    private final String name = "Remove student from one of their courses";

    @Autowired
    public RemoveStudentFromOneOfTheirCourses(StudentServiceImpl studentServiceImpl) {
        this.studentService = studentServiceImpl;

    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        System.out.println("Enter student id");
        Scanner scanner = new Scanner(System.in);
        int studentId = scanner.nextInt();
        Student student = studentService.getById(studentId).get();
        student.getCourses().forEach(course -> System.out.println(course.toString()));
        System.out.println("Enter course id");
        int crId = scanner.nextInt();
        try {
            studentService.deleteCourse(crId, studentId);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        System.out.println(String.format("Student %d delete from the course %d", studentId, crId));
    }
}