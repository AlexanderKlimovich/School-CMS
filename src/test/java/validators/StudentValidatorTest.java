package validators;

import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.validators.StudentValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {StudentValidator.class})
class StudentValidatorTest {

    @Autowired
    private StudentValidator studentValidator;

    @Nested
    class validateOneObject {

        @Test
        void validate_ShouldThrowException_WhenStudentWithoutFirstName() {
            Student student = new Student();
            student.setLastName("dsfg");
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {studentValidator.validate(student);});
            Assertions.assertEquals("First name can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenStudentWithoutLastName() {
            Student student = new Student();
            student.setFirstName("sdg");
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate(student);
                    });
            Assertions.assertEquals("Last name can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenStudentIsNull() {
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate((Student) null);
                    });
            Assertions.assertEquals("Student can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenStudentFirstNameLongerThan() {
            Student student = new Student();
            student.setFirstName("sdgeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            student.setLastName("erg");
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate(student);
                    });
            Assertions.assertEquals("First name can't be more then 20", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenStudentLastNameLongerThan() {
            Student student = new Student();
            student.setFirstName("sdgee");
            student.setLastName("erghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhttttttttttttttttttttttttttttttttttttttt");
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate(student);
                    });
            Assertions.assertEquals("Last name can't be more then 50", exception.getMessage());
        }
    }

    @Nested
    class ValidateListOfObjects {

        @Test
        void validate_ShouldThrowException_WhenListOfStudentsIsNull() {
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate((List<Student>) null);
                    });
            Assertions.assertEquals("Input list of students can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenStudentInListIsNull() {
            List<Student> students = new ArrayList<>();
            students.add(null);
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate(students);
                    });
            Assertions.assertEquals("Student can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenStudentFirstNameInListIsNull() {
            List<Student> students = new ArrayList<>();
            Student student = new Student(1);
            student.setLastName("sdf");
            students.add(student);
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate(students);
                    });
            Assertions.assertEquals("First name can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenStudentLastNameInListIsNull() {
            List<Student> students = new ArrayList<>();
            Student student = new Student(1);
            student.setFirstName("sdf");
            students.add(student);
            IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> {
                        studentValidator.validate(students);
                    });
            Assertions.assertEquals("Last name can't be null", exception.getMessage());
        }

    }

}