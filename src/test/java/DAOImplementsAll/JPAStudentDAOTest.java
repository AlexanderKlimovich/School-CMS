package DAOImplementsAll;

import com.klymovych.school.consoleViev.dao.CourseDAO;
import com.klymovych.school.consoleViev.dao.DaoJpa.CourseDaoJpa;
import com.klymovych.school.consoleViev.dao.DaoJpa.GroupDaoJpa;
import com.klymovych.school.consoleViev.dao.DaoJpa.StudentDaoJpa;
import com.klymovych.school.consoleViev.dao.GroupDAO;
import com.klymovych.school.consoleViev.dao.StudentDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.JPACourseDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.dao.implementsAll.JPAGroupDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.JPAStudentDAO;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import com.klymovych.school.consoleViev.validators.StudentValidator;
import config.RepositoryConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ContextConfiguration(classes = RepositoryConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql( scripts = {"/sql/schemaTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JPAStudentDAOTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private StudentDaoJpa studentDaoJpa;
    @Autowired
    private CourseDaoJpa courseDaoJpa;
    @Autowired
    private GroupDaoJpa groupDaoJpa;

    private StudentDAO JPAStudentDAO;
    private GroupDAO JPAGroupDAO;
    private CourseDAO JPACourseDAO;

    @MockBean
    private CourseValidator courseValidator;
    @MockBean
    private GroupValidator groupValidator;
    @MockBean
    private StudentValidator studentValidator;

    @BeforeEach
    public void setUp() {
        JPACourseDAO = new JPACourseDAO(courseDaoJpa, courseValidator);
        JPAGroupDAO = new JPAGroupDAO(groupValidator, groupDaoJpa);
        JPAStudentDAO = new JPAStudentDAO( studentDaoJpa, studentValidator, groupValidator,
                (JPAGroupDAO) JPAGroupDAO, JPACourseDAO);
    }

    @Nested
    class GetByIdTest {

        @Test
        void getById_ShouldReturnStudent_WhenHaveDataInDatabase() {
            entityManager.persist(new Student("Jack", "Black"));
            Student student = JPAStudentDAO.getById(1).get();
            Assertions.assertEquals(1, student.getId());
            Assertions.assertEquals("Jack", student.getFirstName());
            Assertions.assertEquals("Black", student.getLastName());
        }

        @Test
        void getById_ShouldReturnEmptyOptional_WhenStudentNotInDatabase() {
            Optional<Student> student = JPAStudentDAO.getById(1);
            Assertions.assertTrue(student.isEmpty());
        }

    }

    @Nested
    class SaveTest {

        @Test
        void save_ShouldAddStudentToDatabase_WhenStudentHaveFirstNameAndLastName() {
            entityManager.persist(new Student("Bob", "li"));
            Student student = JPAStudentDAO.getById(1).get();
            Assertions.assertEquals("Bob", student.getFirstName());
            Assertions.assertEquals("li", student.getLastName());
        }

        @Test
        void save_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Student student = new Student("Bob", "li");
            JPAStudentDAO.save(student);
            verify(studentValidator).validate(student);
        }


    }

    @Nested
    class UpdateTest {

        @Test
        void update_ShouldUpdateDataInDatabase_WhenStudentInDatabase() {
            entityManager.persist(new Student("Jack", "Black"));
            entityManager.persist(new Group("cr-21"));
            Student student = new Student(1, new Group(), "Mike", "Der");
            student.setGroup(new Group(1));
            JPAStudentDAO.update(student);
            Student updated = JPAStudentDAO.getById(student.getId()).get();
            Assertions.assertEquals("Mike", updated.getFirstName());
            Assertions.assertEquals("Der", updated.getLastName());
            Assertions.assertEquals(1, updated.getGroup().getId());
        }

        @Test
        void update_ShouldThrowException_WhenNoStudentInDb() {
            DAOException exception = Assertions.assertThrows(DAOException.class,
                    () -> {
                        JPAStudentDAO.update(new Student(23, new Group(), "sdfv", "sfv"));
                    });
            Assertions.assertEquals("No course in database with this id 23", exception.getMessage());
        }

        @Test
        void update_ShouldCallMethodValidate_WhenSaveDataToDb() {
            entityManager.persist(new Student("Jack", "Black"));
            entityManager.persist(new Group("cr-21"));
            Student student = new Student(1, new Group(), "Mike", "Der");
            student.setGroup(new Group(1));
            JPAStudentDAO.update(student);

            verify(studentValidator).validate(student);
        }

    }

    @Nested
    class UpdateAllTest{

        @Test
        void updateAll_ShouldThrowException_WhenNoStudentInDb() {
            List<Student> students = new ArrayList<>();
            students.add(new Student(23, new Group(), "sdfv", "sfv"));
            DAOException exception = Assertions.assertThrows(DAOException.class,
                    () -> {
                        JPAStudentDAO.updateAll(students);
                    });
            Assertions.assertEquals("No student with Id 23", exception.getMessage());
        }

        @Test
        void updateAll_ShouldUpdateDataInDatabase_WhenStudentsInDatabase() {
            entityManager.persist(new Student("Jack", "Black"));
            entityManager.persist(new Group("cr-21"));
            List<Student> students = new ArrayList<>();
            Student student = new Student(1, new Group(), "Mike", "Der");
            student.setGroup(new Group(1));
            students.add(student);
            Student studentInDb = JPAStudentDAO.getById(1).get();
            Assertions.assertEquals("Jack", studentInDb.getFirstName());
            Assertions.assertEquals("Black", studentInDb.getLastName());

            JPAStudentDAO.updateAll(students);

            Student updated = JPAStudentDAO.getById(student.getId()).get();
            Assertions.assertEquals("Mike", updated.getFirstName());
            Assertions.assertEquals("Der", updated.getLastName());
            Assertions.assertEquals(1, updated.getGroup().getId());;
        }

        @Test
        void updateAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            entityManager.persist(new Student("Jack", "Black"));
            entityManager.persist(new Group("cr-21"));
            List<Student> students = new ArrayList<>();
            Student student = new Student(1, new Group(), "Mike", "Der");
            student.setGroup(new Group(1));
            students.add(student);
            JPAStudentDAO.updateAll(students);

            verify(studentValidator).validate(students);
        }

    }

    @Nested
    class SaveAllTest {

        @Test
        void saveAll_ShouldAddStudentsFromList_WhenListNotEmpty() {
            List<Student> students = new ArrayList<>();
            students.add(new Student("Mary", "Wort"));
            students.forEach(entityManager::persist);
            Student student = JPAStudentDAO.getById(1).get();
            Assertions.assertEquals("Mary", student.getFirstName());
            Assertions.assertEquals("Wort", student.getLastName());
        }

        @Test
        void saveAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            List<Student> students = new ArrayList<>();
            students.add(new Student("Mary", "Wort"));
            JPAStudentDAO.saveAll(students);

            verify(studentValidator).validate(students);
        }

    }

    @Nested
    class DeleteByIdTest {

        @Test
        void deleteById_ShouldDeleteDataFromDb_WhenDataPresentInDb() {
            entityManager.persist(new Student("foo", "foo"));
            Student student = JPAStudentDAO.getById(1).get();
            Assertions.assertEquals("foo", student.getFirstName());
            Assertions.assertEquals("foo", student.getLastName());
            JPAStudentDAO.deleteById(1);
            DAOException exception = Assertions.assertThrows(DAOException.class, () -> {
                JPAStudentDAO.deleteById(1);
            });
            Assertions.assertEquals("No student in database with this id 1", exception.getMessage());
        }

        @Test
        void deleteById_ShouldThrowException_WhenNoStudentInDb() {
            DAOException exception = Assertions.assertThrows(DAOException.class,
                    () -> {
                        JPAStudentDAO.deleteById(100);
                    });
            Assertions.assertEquals("No student in database with this id 100", exception.getMessage());
        }

    }

    @Nested
    class AddToGroupTest {

        @Test
        void addToGroup_ShouldAddStudentToGroup_WhenStudentAndGroupAreInDb() {
            entityManager.persist(new Student("Jack", "Black"));
            entityManager.persist(new Group("cr-21"));
            Student student = new Student(1);
            Group group = new Group(1);
            JPAStudentDAO.addToGroup(student, group);
            Assertions.assertEquals(1, JPAStudentDAO.getById(1).get().getGroup().getId());
        }

        @Test
        void addToGroup_ShouldCallMethodValidate_WhenSaveDataToDb() {
            entityManager.persist(new Student("Jack", "Black"));
            entityManager.persist(new Group("cr-21"));
            Group group = new Group(1, "cr-21");
            Student student = new Student(1,group, "Jack", "Black");
            JPAStudentDAO.addToGroup(student, group);

            verify(studentValidator).validate(student);
            verify(groupValidator).validate(group);
        }

        @Test
        void addToGroup_ThrowException_WhenNoStudentInDb() {
            Student student = new Student(100);
            Group group = new Group(0);
            DAOException exception = Assertions.assertThrows(DAOException.class,
                    () -> {
                        JPAStudentDAO.addToGroup(student, group);
                    });
            Assertions.assertEquals("No student or group in database ", exception.getMessage());
        }

        @Test
        void addToGroup_ThrowException_WhenNoGroupInDb() {
            entityManager.persist(new Student("Jack", "Black"));
            Student student = new Student(1);
            Group group = new Group(100);
            DAOException exception = Assertions.assertThrows(DAOException.class,
                    () -> {
                        JPAStudentDAO.addToGroup(student, group);
                    });
            Assertions.assertEquals("No student or group in database ", exception.getMessage());
        }

    }

    @Nested
    class GetStudentByCourseNameTest {

        @Test
        void getStudentByCourseName_ShouldReturnListOfStudents_WhenStudentsHaveThisCourse() {
            entityManager.persist(new Student("Jim", "Beam"));
            entityManager.persist(new Course("Music", "description"));
            entityManager.persist(new Group("sf-45"));
            Course course = new Course(1,"Music", "description");
            Student student = new Student(1, new Group(1),"Jim", "Beam");
            student.setCourse(List.of(course));
            JPAStudentDAO.update(student);
            List<Student> students = JPAStudentDAO.getStudentByCourseName("Music");
            Assertions.assertFalse(students.isEmpty());
            List<Course> courses = JPAStudentDAO.getById(students.get(0).getId()).get().getCourses();
            Course expectedcourse = JPACourseDAO.getById(courses.get(0).getId()).get();
            Assertions.assertEquals("Music", expectedcourse.getName());
        }

        @Test
        void getStudentByCourseName_ShouldReturnEmptyList_WhenNoCourseInDb() {
            Assertions.assertTrue(JPAStudentDAO.getStudentByCourseName("Music").isEmpty());
        }
    }

    @Nested
    class GetAllTest {

        @Test
        void getAll_ShouldReturnListOfStudents_WhenHaveDataInDb() {
            entityManager.persist(new Student("Jim", "Beam"));
            List<Student> students = JPAStudentDAO.getAll();
            Assertions.assertFalse(students.isEmpty());
            students.forEach(student -> Assertions.assertEquals(student.getClass(), Student.class));
        }

        @Test
        void getAll_ShouldReturnEmptyList_WhenNoDataInDb() {
            Assertions.assertTrue(JPAStudentDAO.getAll().isEmpty());
        }
    }

    @Nested
    class FindByCoursesTest {

        @Test
        void findByCourses_ShouldReturnListOfStudents_WhenStudentsHaveThisCourse() {
            Student student = new Student("Jim", "Beam");
            Course course = new Course("Music", "description");
            student.setCourse(List.of(course));
            entityManager.persist(course);
            entityManager.persist(student);
            course.setId(1);
            List<Student> students = JPAStudentDAO.findByCourses(course);
            Assertions.assertFalse(students.isEmpty());
            Assertions.assertEquals(students.get(0).getClass(), Student.class);
        }

        @Test
        void findByCourses_ShouldReturnEmptyList_WhenStudentWithoutCourses() {
            entityManager.persist(new Student("Jim", "Beam"));
            entityManager.persist(new Course("Music", "description"));
            Assertions.assertTrue(JPAStudentDAO.findByCourses(new Course(1,"Music", "description")).isEmpty());
        }

        @Test
        void findByCourses_ShouldThrowException_WhenNoStudentOrNoCourse() {
            DAOException exception = Assertions.assertThrows(DAOException.class,
                    () -> {JPAStudentDAO.findByCourses(new Course(25));});
            Assertions.assertEquals("No course in database with this id 25", exception.getMessage());
        }

    }

}
