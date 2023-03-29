package service.implementsAllinterfases;

import com.klymovych.school.consoleViev.dao.implementsAll.JPACourseDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.CourseServiceImpl;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {
    @MockBean
    private JPACourseDAO JPACourseDAO;
    @MockBean
    private CourseValidator courseValidator;

    @Autowired
    private CourseServiceImpl courseServiceImpl;

    @Nested
    class GetByIdTest {
        @Test
        void getById_ShouldReturnsCourse_WhenCourseInDb() {
            Course expectedCourse = new Course("Music", "Description");
            when(JPACourseDAO.getById(anyInt())).thenReturn(Optional.of(expectedCourse));
            Course actualCourse = courseServiceImpl.getById(1).get();
            Assertions.assertEquals(expectedCourse, actualCourse);
            verify(JPACourseDAO, times(1)).getById(1);
        }

        @Test
        void getById_ShouldReturnEmptyOptional_WhenNoCourseInDb() {
            Optional<Course> course = courseServiceImpl.getById(1);
            Assertions.assertTrue(course.isEmpty());
        }
    }

    @Nested
    class SaveTest {
        @Test
        void save_ShouldSaveCourseToDb_WhenCourseHaveNameAndDescription() {
            Course course = new Course("Music", "Description");
            doNothing().when(JPACourseDAO).save(any(Course.class));
            courseServiceImpl.save(course);
            verify(JPACourseDAO, times(1)).save(course);
        }

        @Test
        void save_ShouldThrowException_WhenCourseNoSaveToDb() {
            Course course = new Course("Music", "Description");
            doThrow(new DAOException()).when(JPACourseDAO).save(any(Course.class));
            ServiceException thrown = Assertions.assertThrows(ServiceException.class, () -> {
                courseServiceImpl.save(course);});
            Assertions.assertEquals("Cannot save data to this course", thrown.getMessage());
        }

        @Test
        void save_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Course course = new Course("Music", "Description");
            doNothing().when(JPACourseDAO).save(any(Course.class));
            courseServiceImpl.save(course);

            verify(courseValidator).validate(course);
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void update_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Course course = new Course(1,"Music", "Description");
            doNothing().when(JPACourseDAO).update(any(Course.class));
            courseServiceImpl.update(course);

            verify(courseValidator).validate(course);
        }

        @Test
        void update_ShouldUpdateCourseInDb_WhenCourseHaveNameAndDescription() {
            Course course = new Course(1,"Music", "Description");
            doNothing().when(JPACourseDAO).update(any(Course.class));
            courseServiceImpl.update(course);
            verify(JPACourseDAO, times(1)).update(course);
        }

        @Test
        void update_ShouldThrowException_WhenCourseNoSaveToDb() throws ServiceException {
            Course course = new Course(1,"Music", "Description");
            doThrow(new DAOException()).when(JPACourseDAO).update(course);
            ServiceException thrown = Assertions.assertThrows(ServiceException.class,
                    () -> courseServiceImpl.update(course));
            Assertions.assertEquals("Cannot update data to this course", thrown.getMessage());
        }
    }

    @Nested
    class DeleteByIdTest {
        @Test
        void deleteById_ShouldCallDeleteByIdOnCourseDao_WhenCourseIsInDb() throws ServiceException {
            int id = 1;
            courseServiceImpl.deleteById(id);
            verify(JPACourseDAO).deleteById(id);
        }

        @Test
        void deleteById_ShouldThrowServiceException_WhenCourseDaoThrowsException() throws ServiceException {
            doThrow(new DAOException()).when(JPACourseDAO).deleteById(anyInt());
            ServiceException exception = Assertions.assertThrows(ServiceException.class,
                    () -> courseServiceImpl.deleteById(1));
            Assertions.assertEquals("Cannot delete data about this course", exception.getMessage());
        }
    }

    @Nested
    class SaveAllTest {
        @Test
        void saveAll_ShouldSaveListOfCourses_WhenListNotEmpty() throws ServiceException {
            List<Course> courses = Arrays.asList(
                    new Course("Music", "description"));
            courseServiceImpl.saveAll(courses);
            verify(JPACourseDAO).saveAll(courses);
        }

        @Test
        void saveAll_ShouldThrowServiceException_WhenListOfCoursesNoSaveToDb() throws ServiceException {
            List<Course> courses = Arrays.asList(
                    new Course("Music", "description"));
            doThrow(DAOException.class).when(JPACourseDAO).saveAll(any());
            Assertions.assertThrows(ServiceException.class, () -> {
                courseServiceImpl.saveAll(courses);
            });
        }

        @Test
        void saveAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            List<Course> courses = Arrays.asList(
                    new Course("Music", "description"));
            courseServiceImpl.saveAll(courses);

            verify(courseValidator).validate(courses);
        }
    }

    @Nested
    class GetAllTest {
        @Test
        void getAll_ShouldReturnListOfCourses_WhenDbNotEmpty() throws ServiceException {
            List<Course> courses = Arrays.asList(
                    new Course("Music", "description"));
            when(JPACourseDAO.getAll()).thenReturn(courses);
            List<Course> actualCourses = courseServiceImpl.getAll();
            Assertions.assertEquals(courses, actualCourses);
        }

        @Test
        void getAll_ShouldThrowServiceException_WhenCourseDAOThrowsException() {
            when(JPACourseDAO.getAll()).thenThrow(new DAOException());
            Exception exception = Assertions.assertThrows(ServiceException.class, () -> {
                courseServiceImpl.getAll();
            });
            Assertions.assertEquals("Cannot take data about this courses", exception.getMessage());
        }
    }

}
