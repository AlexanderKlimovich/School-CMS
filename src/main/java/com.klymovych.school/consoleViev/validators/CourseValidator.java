package com.klymovych.school.consoleViev.validators;

import com.klymovych.school.consoleViev.model.Course;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
@Component
public class CourseValidator {

    public void validate(List<Course> courses) {
        validateNonNull(courses, "Input list of courses");
        courses.forEach(this::validate);
    }

    public void validate(Course course) {
        validateNonNull(course, "Course");
        validateNonNull(course.getName(), "Course name");
        validateNonNull(course.getDescription(), "Course description");
        validateFieldLength(course.getName(), "Course name", 50);
        validateFieldLength(course.getDescription(), "Course description", 255);
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
