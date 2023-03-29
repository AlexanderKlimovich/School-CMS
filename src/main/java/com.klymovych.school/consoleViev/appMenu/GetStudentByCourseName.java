package com.klymovych.school.consoleViev.appMenu;

import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.StudentService;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Scanner;
@Component
public class GetStudentByCourseName implements Menu {
    private StudentService studentService;
    private final String name = "Get student by course name";

    @Autowired
    public GetStudentByCourseName(StudentServiceImpl studentServiceImpl) {
        this.studentService = studentServiceImpl;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        System.out.println("Enter course name");
        Scanner scanner = new Scanner(System.in);
        try {
            studentService.getStudentByCourseName(scanner.nextLine()).forEach
            (student -> System.out.println(student.toString()));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}