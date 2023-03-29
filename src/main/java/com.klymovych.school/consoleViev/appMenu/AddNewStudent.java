package com.klymovych.school.consoleViev.appMenu;

import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.StudentService;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Scanner;
@Component
public class AddNewStudent implements Menu {

    private final StudentService studentService;
    private final String name = "Add New Student";

    @Autowired
    public AddNewStudent(StudentServiceImpl studentServiceImpl) {
        this.studentService = studentServiceImpl;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        System.out.println("Enter first name");
        Scanner scanner = new Scanner(System.in);
        String firstName = scanner.nextLine();
        System.out.println("Enter last name");
        String lastName = scanner.nextLine();
        try {
            studentService.save(new Student(firstName, lastName));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        System.out.println(String.format("Student %s %s added", firstName, lastName));
    }
}