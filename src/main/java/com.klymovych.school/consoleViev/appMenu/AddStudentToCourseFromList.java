package com.klymovych.school.consoleViev.appMenu;

import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.StudentService;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AddStudentToCourseFromList implements Menu {
    private StudentService studentService;
    private final String name = "Add student to course from List";

    @Autowired
    public AddStudentToCourseFromList(StudentServiceImpl studentServiceImpl) {
        this.studentService = studentServiceImpl;
    }
    @Override
    public String getName() {
        return name;
    }


    @Override
    public void run() {
        System.out.println("Enter course id");
        Scanner scanner = new Scanner(System.in);
        try {
            studentService.addStudentToCourseFromList(scanner.nextInt());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Students added to the course");
    }
}
