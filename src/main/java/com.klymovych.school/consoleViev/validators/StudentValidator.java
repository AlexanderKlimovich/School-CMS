package com.klymovych.school.consoleViev.validators;

import com.klymovych.school.consoleViev.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class StudentValidator {

    public void validate(List<Student> students) {
        validateNonNull(students, "Input list of students");
        students.forEach(this::validate);
    }

    public void validate(Student student) {
        validateNonNull(student, "Student");
        validateNonNull(student.getFirstName(), "First name");
        validateNonNull(student.getLastName(), "Last name");
        validateFieldLength(student.getFirstName(), "First name", 20);
        validateFieldLength(student.getLastName(), "Last name", 50);
    }

    private void validateNonNull(Object object, String name){
        Optional.ofNullable(object).orElseThrow(() -> new IllegalArgumentException(
                name +" can't be null"));
    }

    private void validateFieldLength(String object, String name, int length) {
        if(object.length() > length){
            throw new IllegalArgumentException(name +" can't be more then " + length);
        }
    }
}
