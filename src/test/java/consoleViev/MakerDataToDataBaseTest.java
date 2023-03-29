package consoleViev;

import com.klymovych.school.consoleViev.MakerDataToDataBase;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import com.klymovych.school.consoleViev.validators.StudentValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {MakerDataToDataBase.class})
class MakerDataToDataBaseTest {

    @MockBean
    private StudentValidator studentValidator;

    @MockBean
    private GroupValidator groupValidator;

    @MockBean
    private CourseValidator courseValidator;

    @Autowired
    private MakerDataToDataBase makerDataToDataBase;

    @Nested
    class MakeStudentsTest{

        @Test
        void makeStudents_ShouldMakeNotEmptyListOfStudents_WhenFakerAddToProject(){
           Assertions.assertFalse(makerDataToDataBase.makeStudents().isEmpty());
        }

        @Test
        void makeStudents_ShouldMakeListOfStudents_WhenFakerAddToProject(){
            List<Student> students = makerDataToDataBase.makeStudents();
            students.stream().forEach(student ->Assertions.assertEquals(Student.class, student.getClass()));
        }

        @Test
        void makeStudents_ShouldMakeListOfStudentsWithFirstAndLAstNames_WhenFakerAddToProject(){
            List<Student> students = makerDataToDataBase.makeStudents();
            students.forEach(student ->Assertions.assertTrue(student.getFirstName().getClass().equals(String.class)));
            students.forEach(student ->Assertions.assertTrue(student.getLastName().getClass().equals(String.class)));
        }
    }


    @Nested
    class AddCourseToStudentTest {

        @Test
        void addCourseToStudent_ShouldReturnListOfStudentsWithCourses_WhenStudentAndCourseAreInDb() throws  IOException {
            AtomicInteger index = new AtomicInteger(1);
            List<Student> students = makerDataToDataBase.makeStudents();
            students.forEach(student -> student.setId(index.getAndIncrement()));

            List<Course> courses = makerDataToDataBase.makeCourses("coursesNames.csv");
            index.set(0);
            courses.forEach(course -> course.setId(index.getAndIncrement()));

            List<Student> studentsWithCourses = makerDataToDataBase.addCourseToStudent(students, courses);
            studentsWithCourses.forEach(student ->
                    Assertions.assertEquals(Course.class, student.getCourses().get(0).getClass()));

        }

        @Test
        void addCourseToStudent_ShouldUseMethodValidateWithCoursesAndValidateListOfCourses_WhenHaveInputData() throws IOException {
            AtomicInteger index = new AtomicInteger(1);
            List<Student> students = makerDataToDataBase.makeStudents();
            students.forEach(student -> student.setId(index.getAndIncrement()));

            List<Course> courses = makerDataToDataBase.makeCourses("coursesNames.csv");
            index.set(0);
            courses.forEach(course -> course.setId(index.getAndIncrement()));

            makerDataToDataBase.addCourseToStudent(students, courses);

            verify(studentValidator).validate(students);
            verify(courseValidator).validate(courses);
        }

    }
    @Nested
    class AddStudentsToGroupTest {

        @Test
        void addStudentsToGroup_ShouldReturnListOfStudentsWithGroup(){
            List<Student> students = makerDataToDataBase.makeStudents();
            AtomicInteger index = new AtomicInteger(1);
            students.forEach(student -> student.setId(index.getAndIncrement()));
            List<Group> groups = makerDataToDataBase.makeGroups();
            index.set(0);
            groups.forEach(group -> group.setId(index.getAndIncrement()));

            List<Student> equalsStudents = makerDataToDataBase.addStudentsToGroup(students, groups);
            equalsStudents.forEach(student -> Assertions.assertTrue(student.getGroup().getId()>=0));
        }

        @Test
        void addStudentToGroup_ShouldCallMethodsValidate_WhenSaveDataToDb(){
            List<Student> students = makerDataToDataBase.makeStudents();
            AtomicInteger index = new AtomicInteger(1);
            students.forEach(student -> student.setId(index.getAndIncrement()));
            List<Group> groups = makerDataToDataBase.makeGroups();
            index.set(0);
            groups.forEach(group -> group.setId(index.getAndIncrement()));

            makerDataToDataBase.addStudentsToGroup(students, groups);
            verify(studentValidator).validate(students);
            verify(groupValidator).validate(groups);
        }


    }

        @Nested
        class MakeGroupsTest {
            @Test
            void makeGroups_ShouldMakeNotEmptyListOfGroups_WhenMethodWork() {
                Assertions.assertFalse(makerDataToDataBase.makeGroups().isEmpty());
            }

            @Test
            void makeGroups_ShouldMakeListOfGroups_WhenMethodWork() {
                List<Group> groups = makerDataToDataBase.makeGroups();
                groups.forEach(group -> Assertions.assertEquals(Group.class, group.getClass()));
            }

            @Test
            void makeStudents_ShouldMakeListOfStudentsWithFirstAndLAstNames_WhenFakerAddToProject() {
                List<Group> groups = makerDataToDataBase.makeGroups();
                groups.forEach(group -> Assertions.assertTrue(group.getName().getClass().equals(String.class)));
            }
        }

        @Nested
        class MakeCoursesTest {

            @Test
            void MakeCourses_ShouldMakeNotEmptyListOfCourses_WhenFileNameNotNull() throws IOException {
                Assertions.assertFalse(makerDataToDataBase.makeCourses("coursesNames.csv").isEmpty());
            }

            @Test
            void MakeCourses_ShouldMakeListOfGroups_WhenMethodWork() {
                List<Group> groups = makerDataToDataBase.makeGroups();
                groups.forEach(group -> Assertions.assertEquals(Group.class, group.getClass()));
            }

            @Test
            void MakeCourses_ShouldMakeListOfStudentsWithFirstAndLAstNames_WhenFakerAddToProject() throws IOException {
                List<Course> courses = makerDataToDataBase.makeCourses("coursesNames.csv");
                courses.forEach(course -> Assertions.assertTrue(course.getName().getClass().equals(String.class)));
                courses.forEach(course -> Assertions.assertTrue(course.getDescription().getClass().equals(String.class)));
            }

            @Test
            void MakeCourses_Should_ThrowException_WhenNoFile() {
                Exception exception = Assertions.assertThrows(IOException.class, () -> {
                    makerDataToDataBase.makeCourses("noFile");
                });
                Assertions.assertEquals("File noFile not found", exception.getMessage());
            }
        }
    }

