package service.implementsAllinterfases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.dao.implementsAll.JPACourseDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.JPAStudentDAO;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import com.klymovych.school.consoleViev.validators.StudentValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {
    @MockBean
    private JPAStudentDAO JPAStudentDAO;

    @MockBean
    private JPACourseDAO JPACourseDAO;

    @MockBean
    private StudentValidator studentValidator;

    @MockBean
    private GroupValidator groupValidator;

    @Autowired
    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @Nested
    class GetByIdTest {
        @Test
        void getById_ShouldReturnsStudent_WhenStudentInDb() {
            Student expectedStudent = new Student("John", "Doe");
            when(JPAStudentDAO.getById(1)).thenReturn(Optional.of(expectedStudent));
            Student actualStudent = studentServiceImpl.getById(1).get();
            Assertions.assertEquals(expectedStudent, actualStudent);
        }

        @Test
        void getById_ShouldThrowsServiceException_WhenNoStudentInDb() {
            int id = 2;
            when(JPAStudentDAO.getById(id)).thenThrow(new DAOException());
            ServiceException exception =
                    Assertions.assertThrows(ServiceException.class, () -> studentServiceImpl.getById(id));
            assertEquals("This student is absent in database", exception.getMessage());
        }
    }

    @Nested
    class SaveTest {
        @Test
        void save_ShouldSaveStudentToDb_WhenStudentHaveFirstAndLastName() {
            Student student = new Student("John", " Doe");
            doNothing().when(JPAStudentDAO).save(any(Student.class));
            studentServiceImpl.save(student);
            verify(JPAStudentDAO, times(1)).save(student);
        }

        @Test
        void save_ShouldThrowException_WhenStudentNoSaveToDb() {
            Student student = new Student("John", "Doe");
            doThrow(new DAOException()).when(JPAStudentDAO).save(any(Student.class));
            ServiceException thrown = Assertions.assertThrows(ServiceException.class, () -> {
                studentServiceImpl.save(student);
            });
            Assertions.assertEquals("Cannot save this student", thrown.getMessage());
        }

        @Test
        void save_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Student student = new Student("John", " Doe");
            doNothing().when(JPAStudentDAO).save(any(Student.class));
            studentServiceImpl.save(student);

            verify(studentValidator).validate(student);
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void update_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Student student = new Student("John", " Doe");
            student.setId(1);
            doNothing().when(JPAStudentDAO).update(any(Student.class));
            studentServiceImpl.update(student);

            verify(studentValidator).validate(student);
        }
        @Test
        void update_ShouldUpdateStudentInDb_WhenStudentHaveFirstAndLastName() {
            Student student = new Student("John", " Doe");
            student.setId(1);
            doNothing().when(JPAStudentDAO).update(any(Student.class));
            studentServiceImpl.update(student);
            verify(JPAStudentDAO, times(1)).update(student);
        }

        @Test
        void update_ShouldThrowException_WhenStudentNoSaveToDb() throws ServiceException {
            Student student = new Student("John", " Doe");
            student.setId(1);
            doThrow(new DAOException()).when(JPAStudentDAO).update(student);
            ServiceException thrown = Assertions.assertThrows(ServiceException.class,
                    () -> studentServiceImpl.update(student));
            Assertions.assertEquals("Cannot update this student", thrown.getMessage());
        }
    }

    @Nested
    class UpdateAllTest{

        @Test
        void updateAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Student student = new Student(1,new Group(1),"John", " Doe");
            List<Student> students = new ArrayList<>();
            students.add(student);
            doNothing().when(JPAStudentDAO).updateAll(anyList());
            studentServiceImpl.updateAll(students);

            verify(studentValidator).validate(students);
        }
        @Test
        void updateAll_ShouldUpdateStudentInDb_WhenStudentHaveFirstAndLastName() {
            Student student = new Student(1,new Group(1),"John", " Doe");
            List<Student> students = new ArrayList<>();
            students.add(student);
            doNothing().when(JPAStudentDAO).updateAll(anyList());
            studentServiceImpl.updateAll(students);
            verify(JPAStudentDAO, times(1)).saveAll(students);
        }

        @Test
        void updateAll_ShouldThrowException_WhenStudentNoSaveToDb() throws ServiceException {
            Student student = new Student(1,new Group(1),"John", " Doe");
            List<Student> students = new ArrayList<>();
            students.add(student);
            doThrow(new DAOException()).when(JPAStudentDAO).saveAll(anyList());
            ServiceException thrown = Assertions.assertThrows(ServiceException.class,
                    () -> studentServiceImpl.updateAll(students));
            Assertions.assertEquals("Cannot update package of data", thrown.getMessage());
        }
    }

    @Nested
    class DeleteByIdTest {
        @Test
        void deleteById_ShouldCallDeleteByIdOnStudentDao_WhenStudentIsInDb() throws ServiceException {
            int id = 1;
            studentServiceImpl.deleteById(id);
            verify(JPAStudentDAO).deleteById(id);
        }

        @Test
        void deleteById_ShouldThrowServiceException_WhenDaoThrowsException() throws ServiceException {
            int id = 1;
            doThrow(new DAOException()).when(JPAStudentDAO).deleteById(id);
            ServiceException exception = Assertions.assertThrows(ServiceException.class,
                    () -> studentServiceImpl.deleteById(id));
            Assertions.assertEquals("Cannot delete this student", exception.getMessage());
        }
    }

    @Nested
    class GetStudentByCourseNameTest {
        @Test
        void getStudentByCourseName_ShouldReturnsStudentsWithMatchingCourse_WhenStudentsHaveCourse() throws ServiceException {
            String courseName = "Computer Science";
            Student student1 = new Student(new Group(1), "John Doe", courseName);
            Student student2 = new Student(new Group(2), "Jane Doe", courseName);
            List<Student> expectedStudents = Arrays.asList(student1, student2);
            when(JPAStudentDAO.getStudentByCourseName(courseName)).thenReturn(expectedStudents);
            List<Student> actualStudents = studentServiceImpl.getStudentByCourseName(courseName);
            verify(JPAStudentDAO).getStudentByCourseName(courseName);
            Assertions.assertEquals(expectedStudents, actualStudents);
        }

        @Test
        void getStudentByCourseName_ShouldThrowsException_WhenErrorOccursInDaoClass() throws ServiceException {
            String courseName = "Computer Science";
            when(JPAStudentDAO.getStudentByCourseName(courseName)).thenThrow(new DAOException());
            ServiceException exception = Assertions.assertThrows(ServiceException.class,
                    () -> studentServiceImpl.getStudentByCourseName(courseName));
            verify(JPAStudentDAO).getStudentByCourseName(courseName);
            Assertions.assertEquals("Cannot get student by this course name", exception.getMessage());
        }
    }

    @Nested
    class GetCoursesOfStudentTest {
        @Test
        void getCoursesOfStudent_ShouldReturnListOfCourses_WhenStudentHaveCourse() throws ServiceException {
            Student student = new Student(1, new Group(1),"John", " Doe");
            List<Course> expectedCourses = new ArrayList<>();
            expectedCourses.add(new Course(1, "Math", "Description1"));
            expectedCourses.add(new Course(2, "Science", "Description2"));
            student.setCourse(expectedCourses);
            when(JPAStudentDAO.getById(student.getId())).thenReturn(Optional.of(student));
            List<Course> actualCourses = studentServiceImpl.getCoursesOfStudent(student);
            Assertions.assertEquals(expectedCourses, actualCourses);
        }

        @Test
        void getCoursesOfStudent_ShouldThrowException_WhenStudentNotFound() throws ServiceException {
            Student student = new Student(1);
            when(JPAStudentDAO.getById(student.getId())).thenThrow(new DAOException());
            ServiceException exception = Assertions.assertThrows(ServiceException.class,
                    () -> studentServiceImpl.getCoursesOfStudent(student));
            Assertions.assertEquals("This student is absent in database", exception.getMessage());
        }

    }

    @Nested
    class SaveAllTest {
        @Test
        void saveAll_ShouldSaveListOfStudents_WhenListNotEmpty() throws ServiceException {
            List<Student> students = new ArrayList<>();
            students.add(new Student("John", "Doe"));
            students.add(new Student("Jane", "Doe"));
            studentServiceImpl.saveAll(students);
            verify(JPAStudentDAO).saveAll(students);
        }

        @Test
        void saveAll_ShouldThrowServiceException_WhenStudentDAOThrowException() throws ServiceException {
            List<Student> students = new ArrayList<>();
            students.add(new Student("John", "Doe"));
            doThrow(DAOException.class).when(JPAStudentDAO).saveAll(any());
            Assertions.assertThrows(ServiceException.class, () -> {
                studentServiceImpl.saveAll(students);
            });
        }

        @Test
        void saveAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            List<Student> students = new ArrayList<>();
            students.add(new Student("John", "Doe"));
            students.add(new Student("Jane", "Doe"));
            studentServiceImpl.saveAll(students);

            verify(studentValidator).validate(students);
        }
    }

    @Nested
    class GetAllTest {
        @Test
        void getAll_ShouldReturnListOfStudents() throws ServiceException {
            List<Student> expectedStudents = Arrays.asList(
                    new Student("John", "Doe"),
                    new Student("Jane", "Doe"));
            when(JPAStudentDAO.getAll()).thenReturn(expectedStudents);
            List<Student> actualStudents = studentServiceImpl.getAll();
            Assertions.assertEquals(expectedStudents, actualStudents);
        }

        @Test
        void getAll_ShouldThrowServiceException_WhenRepositoryThrowsException() {
            when(JPAStudentDAO.getAll()).thenThrow(new DAOException());
            Exception exception = Assertions.assertThrows(ServiceException.class, () -> {
                studentServiceImpl.getAll();
            });
            Assertions.assertEquals("Cannot get all students", exception.getMessage());
        }
    }

    @Nested
    class AddCourseTest {
        @Test
        void addCourse_ShouldAddCourseToStudent_WhenStudentAndCourseInDb() {
            Student student = new Student(1, new Group(1), "Jim", "Beam");
            Course course = new Course(1, "Music", "description");
            when(JPAStudentDAO.getById(anyInt())).thenReturn(Optional.of(student));
            when(JPACourseDAO.getById(anyInt())).thenReturn(Optional.of(course));
            doNothing().when(JPAStudentDAO).save(any(Student.class));
            studentServiceImpl.addCourse(student.getId(), course.getId());
            verify(JPAStudentDAO).getById(student.getId());
            verify(JPACourseDAO).getById(course.getId());
            verify(JPAStudentDAO).save(student);
        }

        @Test
        void addCourse_ShouldThrowException_WhenDAOClassThrowException() {
            int courseId = 1;
            doThrow(new DAOException()).when(JPAStudentDAO).getById(anyInt());
            Exception exception = Assertions.assertThrows(ServiceException.class, () -> {
                studentServiceImpl.addCourse(1, courseId);
            });
            Assertions.assertEquals("This student is absent in database", exception.getMessage());
        }
    }

    @Nested
    class AddCoursesForAllStudentsTest{
        @Test
        void addCoursesForAllStudents_ShouldAddCoursesToStudents_WhenStudentsAndCoursesInDb(){
            Student student = new Student(1, new Group(1), "Jim", "Beam");
            student.setCourse(List.of(new Course(1, "Music", "description")));
            List<Student> students = new ArrayList<>();
            students.add(student);
            doNothing().when(JPAStudentDAO).saveAll(anyList());
            studentServiceImpl.addCoursesForAllStudents(students);
            verify(JPAStudentDAO).saveAll(students);
        }

        @Test
        void addCoursesForAllStudents_ShouldThrowException_WhenDAOClassThrowException() {
            Student student = new Student(1, new Group(1), "Jim", "Beam");
            student.setCourse(List.of(new Course(1, "Music", "description")));
            List<Student> students = new ArrayList<>();
            students.add(student);
            doThrow(new DAOException()).when(JPAStudentDAO).saveAll(anyList());
            Exception exception = Assertions.assertThrows(ServiceException.class, () -> {
                studentServiceImpl.addCoursesForAllStudents(students);
            });
            Assertions.assertEquals("Cannot add courses for students", exception.getMessage());
        }

        @Test
        void addCoursesForAllStudents_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Student student = new Student(1, new Group(1), "Jim", "Beam");
            student.setCourse(List.of(new Course(1, "Music", "description")));
            List<Student> students = new ArrayList<>();
            students.add(student);
            doNothing().when(JPAStudentDAO).saveAll(anyList());
            studentServiceImpl.addCoursesForAllStudents(students);

            verify(studentValidator).validate(students);
        }
    }

    @Nested
    class DeleteCourseTest {
        @Test
        void deleteCourse_whenStudentIdAndCourseIdAreValid_thenDeletesTheCourse() throws ServiceException {
            Student student = new Student(1, new Group(1), "Jim", "Beam");
            Course course = new Course(1, "Music", "description");
            List<Course> courses = new ArrayList<>();
            courses.add(course);
            student.setCourse(courses);
            when(JPAStudentDAO.getById(anyInt())).thenReturn(Optional.of(student));
            when(JPACourseDAO.getById(anyInt())).thenReturn(Optional.of(course));
            doNothing().when(JPAStudentDAO).save(any(Student.class));
            studentServiceImpl.deleteCourse(student.getId(), course.getId());
            verify(JPAStudentDAO).getById(student.getId());
            verify(JPACourseDAO).getById(course.getId());
            verify(JPAStudentDAO).save(student);
        }

        @Test
        void deleteCourse_whenStudentIdAndCourseIdAreInvalid_thenThrowsServiceException() throws ServiceException {
            int studentId = 1;
            int courseId = 2;
            doThrow(DAOException.class).when(JPAStudentDAO).getById(anyInt());
            Exception exception = Assertions.assertThrows(ServiceException.class,
                    () -> studentServiceImpl.deleteCourse(studentId, courseId));
            Assertions.assertEquals("This student is absent in database", exception.getMessage());
        }
    }

    @Nested
    class AddStudentToCourseFromListTest {
        @Test
        void addStudentToCourseFromList_ShouldAddStudents_WhenListNotEmpty() throws ServiceException {
            Course course = new Course(1, "Music", "description");
            Student student = new Student(1, new Group(1), "Jon", "Doe");
            student.setCourse(List.of(course));
            List<Student> students = Arrays.asList(student);
            when(JPAStudentDAO.getAll()).thenReturn(students);
            when(JPACourseDAO.getById(anyInt())).thenReturn(Optional.of(course));
            when(JPAStudentDAO.findByCourses(any(Course.class))).thenReturn(new ArrayList<>());
            doNothing().when(JPAStudentDAO).saveAll(anyList());
            studentServiceImpl.addStudentToCourseFromList(course.getId());
            verify(JPACourseDAO, times(1)).getById(course.getId());
            verify(JPAStudentDAO, times(1)).findByCourses(course);
            verify(JPAStudentDAO, times(1)).saveAll(students);
        }
    }

    @Nested
    class AddStudentToGroupTest{

        @Test
        void addStudentToGroup_ShouldAddStudentsToGroup_WhenStudentsAndGroupsAreInDb(){
            studentServiceImpl.addStudentToGroup(new Student(), new Group());
           verify(JPAStudentDAO).save(any(Student.class));
        }

        @Test
        void addStudentToGroup_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Student student = new Student(1, new Group(1), "Jim", "Beam");
            Group group = new Group(1, "df-56");
            studentServiceImpl.addStudentToGroup(student, group);
            verify(studentValidator).validate(student);
            verify(groupValidator).validate(group);
        }

        @Test
        void addStudentToGroup_ShouldThrowException_WhenStudentOrGroupNoInDb(){
            doThrow(new DAOException()).when(JPAStudentDAO).save(any(Student.class));
            Exception exception = Assertions.assertThrows(ServiceException.class,
                    () -> studentServiceImpl.addStudentToGroup(new Student(), new Group()));
            Assertions.assertEquals("Cannot save this student", exception.getMessage());
        }
    }
}
