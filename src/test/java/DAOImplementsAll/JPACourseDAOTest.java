package DAOImplementsAll;

import com.klymovych.school.consoleViev.dao.CourseDAO;
import com.klymovych.school.consoleViev.dao.DaoJpa.CourseDaoJpa;
import com.klymovych.school.consoleViev.dao.implementsAll.JPACourseDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import config.RepositoryConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
@Sql(scripts = {"/sql/schemaTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JPACourseDAOTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CourseDaoJpa dao;

    @MockBean
    private CourseValidator courseValidator;

    private CourseDAO courseDAOImpl;

    @BeforeEach
    void setUp(){
        courseDAOImpl = new JPACourseDAO(dao, courseValidator);
    }

    @Nested
    class GetByIdTest {

        @Test
        void getById_ShouldReturnCourse_whenExistingCourseInDb() {
            Course expected = new Course("Music", "description");
            entityManager.persist(expected);
            Course actual = courseDAOImpl.getById(expected.getId()).get();
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void getById_ShouldReturnEmptyOptional_whenNonExistingCourseInDb() {
            Optional<Course> course= courseDAOImpl.getById(2);;
            Assertions.assertTrue(course.isEmpty());
        }

    }

    @Nested
    class SaveTest {

        @Test
        void save_ShouldSaveSuccessfully_WhenCourseHaveNameAndDescription() {
            entityManager.persist(new Course("History", "description2"));
            Course savedCourse = courseDAOImpl.getById(1).get();
            Assertions.assertNotNull(savedCourse);
            Assertions.assertEquals("History", savedCourse.getName());
            Assertions.assertEquals("description2", savedCourse.getDescription());
        }

        @Test
        void save_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Course course = new Course("History", "description2");
            courseDAOImpl.save(course);

            verify(courseValidator).validate(course);
        }

    }

    @Nested
    class SaveAllTest {

        @Test
        void saveAll_ShouldSaveSuccessfully_WhenCoursesHAveDescriptionsAndNames() {
            List<Course> courses = new ArrayList<>();
            courses.add(new Course("Economics", "Description"));
            entityManager.persist(courses.get(0));
            List<Course> savedCourses = courseDAOImpl.getAll();
            Assertions.assertEquals(courses.size(), savedCourses.size());
            Assertions.assertEquals(savedCourses.get(0).getName(), courses.get(0).getName());
            Assertions.assertEquals(savedCourses.get(0).getDescription(), courses.get(0).getDescription());
        }

        @Test
        void saveAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            List<Course> courses = new ArrayList<>();
            courses.add(new Course("Economics", "Description"));
            courseDAOImpl.saveAll(courses);

            verify(courseValidator).validate(courses);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        void update_ShouldUpdateDataInDb_WhenUpdatedCourseHaveNameAndDescription() {
            entityManager.persist(new Course("Music", "description"));
            Course updatedCourse = new Course(1,"Philosophy", "description 6");
            courseDAOImpl.update(updatedCourse);
            Course updated = courseDAOImpl.getById(1).get();
            Assertions.assertEquals(updatedCourse.getName(), updated.getName());
            Assertions.assertEquals(updatedCourse.getDescription(), updated.getDescription());
        }

        @Test
        void update_ShouldThrowException_WhenNoCourseInDbWithInputId() {
            Exception exception = Assertions.assertThrows(DAOException.class,
                    () -> courseDAOImpl.update(new Course(5, "sdf", "sfd")));
            Assertions.assertEquals("No course in database with this id 5", exception.getMessage());
        }

        @Test
        void update_ShouldCallMethodValidate_WhenSaveDataToDb() {
            entityManager.persist(new Course("Music", "description"));
            Course updatedCourse = new Course(1,"Philosophy", "description 6");
            courseDAOImpl.update(updatedCourse);
            verify(courseValidator).validate(updatedCourse);
        }

    }

    @Nested
    class DeleteById {

        @Test
        void deleteById_ShouldDeleteCourseFromDb_WhenCourseExistInDb() {
            Course course = new Course("Philosophy", "Description");
            entityManager.persist(course);
            Course savedCourse = courseDAOImpl.getById(1).get();
            Assertions.assertEquals(course.getName(), savedCourse.getName());
            Assertions.assertEquals(course.getDescription(), savedCourse.getDescription());
            Assertions.assertEquals(1, savedCourse.getId());
            courseDAOImpl.deleteById(1);
            Exception exception = Assertions.assertThrows(DAOException.class,
                    () -> courseDAOImpl.deleteById(1));
            Assertions.assertEquals("No course in database with this id 1", exception.getMessage());
        }

        @Test
        void deleteById_ShouldThrowException_WhenNoCourseInDb() {
            Exception exception = Assertions.assertThrows(DAOException.class,
                    () -> courseDAOImpl.deleteById(3));
            Assertions.assertEquals("No course in database with this id 3", exception.getMessage());
        }

    }

    @Nested
    class GetAllTest {
        @Test
        void getAll_ShouldReturnEmptyOption_WhenNoCoursesInDb() {
            List<Course> courses = courseDAOImpl.getAll();
            Assertions.assertTrue(courses.isEmpty());
        }
        @Test
        void getAll_ShouldReturnNotEmptyList_WhenCoursesAreInDb() {
            entityManager.persist(new Course("Music", "Description"));
            List<Course> courses = courseDAOImpl.getAll();
            Assertions.assertFalse(courses.isEmpty());
            Assertions.assertEquals(courses.get(0).getClass(), Course.class);
        }
    }
}