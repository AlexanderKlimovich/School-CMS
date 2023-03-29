package validators;

import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
@SpringBootTest(classes = {CourseValidator.class})
class CourseValidatorTest {
    @Autowired
    private CourseValidator courseValidator;

    @Nested
    class ValidateOneObject {

        @Test
        void validate_ShouldThrowException_WhenObjectIsNull() {
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate((Course) null);
                    });
            Assertions.assertEquals("Course can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenCourseWithoutName() {
            Course course = new Course();
            course.setDescription("description");
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate(course);
                    });
            Assertions.assertEquals("Course name can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenCourseWithoutDescription() {
            Course course = new Course();
            course.setName("Music");
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate(course);
                    });
            Assertions.assertEquals("Course description can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenCourseNameLongerThan50Chars() {
            Course course = new Course("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                    "rrrrrrrr");
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate(course);
                    });
            Assertions.assertEquals("Course name can't be more then 50", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenCourseDescriptionLongerThan255Chars() {
            Course course = new Course("Music",
                    "rrrrrrrrfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                            "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                            "gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                            );
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate(course);
                    });
            Assertions.assertEquals("Course description can't be more then 255", exception.getMessage());
        }
    }

    @Nested
    class ValidateList {

        @Test
        void validate_ShouldThrowException_WhenListOfCoursesIsNull() {
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate((List<Course>) null);
                    });
            Assertions.assertEquals("Input list of courses can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenListOfCoursesExistNull() {
            List<Course> courses = new ArrayList<>();
            courses.add(null);
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate(courses);
                    });
            Assertions.assertEquals("Course can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenListOfCoursesHaveCourseWithoutName() {
            List<Course> courses = new ArrayList<>();
            Course course = new Course();
            course.setDescription("Description");
            courses.add(course);
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate(courses);
                    });
            Assertions.assertEquals("Course name can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenListOfCoursesHaveCourseWithoutDescription() {
            List<Course> courses = new ArrayList<>();
            Course course = new Course();
            course.setName("Music");
            courses.add(course);
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        courseValidator.validate(courses);
                    });
            Assertions.assertEquals("Course description can't be null", exception.getMessage());
        }
    }

}