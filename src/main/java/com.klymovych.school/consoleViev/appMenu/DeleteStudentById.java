package com.klymovych.school.consoleViev.appMenu;

import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class DeleteStudentById implements Menu{
    private StudentServiceImpl studentService;
    private final String name = "Delete student by Id";

    @Autowired
    public DeleteStudentById(StudentServiceImpl studentServiceImpl) {
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
        try {
            studentService.deleteById(scanner.nextInt());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Student delete");
    }
}
