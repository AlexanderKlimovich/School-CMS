import com.klymovych.school.consoleViev.DbLoader;
import com.klymovych.school.consoleViev.MakerDataToDataBase;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.CourseServiceImpl;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.GroupServiceImpl;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
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

@SpringBootTest(classes = {DbLoader.class})
class DbLoaderTest {
    @MockBean
    private GroupServiceImpl groupServiceImpl;

    @MockBean
    private CourseServiceImpl courseServiceImpl;

    @MockBean
    private StudentServiceImpl studentServiceImpl;

    @MockBean
    private MakerDataToDataBase makerDataToDataBase;

    @Autowired
    private DbLoader dbLoader;

    @Nested
    class  InsertDataToDatabaseTest{


        @Test
        void insertDataToDatabase_ShouldInsertDataToDb_WhenAllServicesWork() throws IOException {
            AtomicInteger index = new AtomicInteger(1);

            List<Student> students = makerDataToDataBase.makeStudents();
            List<Student> studentsWithID = students;
            studentsWithID.forEach(student -> student.setId(index.getAndIncrement()));

            List<Course> courses = makerDataToDataBase.makeCourses("coursesNames.csv");
            List<Course> coursesWithId = courses;
            index.set(1);
            coursesWithId.forEach(course -> course.setId(index.getAndIncrement()));

            List<Group> groups = makerDataToDataBase.makeGroups();
            List<Group> groupsWithId = groups;
            index.set(1);
            groupsWithId.forEach(group -> group.setId(index.getAndIncrement()));

            List<Student> studentsWithGroup = makerDataToDataBase.addStudentsToGroup(studentsWithID, groupsWithId);
            List<Student> studentsWithCourse = makerDataToDataBase.addCourseToStudent(studentsWithID, coursesWithId);


            doNothing().when(groupServiceImpl).saveAll(anyList());
            doNothing().when(courseServiceImpl).saveAll(anyList());
            doNothing().when(studentServiceImpl).saveAll(anyList());

            when(studentServiceImpl.getAll()).thenReturn(studentsWithID);
            when(courseServiceImpl.getAll()).thenReturn(coursesWithId);
            when(groupServiceImpl.getAll()).thenReturn(groupsWithId);

            when(makerDataToDataBase.makeStudents()).thenReturn(students);
            when(makerDataToDataBase.makeGroups()).thenReturn(groups);
            when(makerDataToDataBase.makeCourses(anyString())).thenReturn(courses);

            when(makerDataToDataBase.addStudentsToGroup(anyList(), anyList())).thenReturn(studentsWithGroup);
            when(makerDataToDataBase.addCourseToStudent(anyList(),anyList())).thenReturn(studentsWithCourse);

            doNothing().when(studentServiceImpl).updateAll(anyList());
            doNothing().when(studentServiceImpl).addCoursesForAllStudents(anyList());

            dbLoader.insertDataToDatabase();

            verify(groupServiceImpl).saveAll(groups);
            verify(courseServiceImpl).saveAll(courses);
            verify(studentServiceImpl).saveAll(students);
            verify(studentServiceImpl).updateAll(studentsWithGroup);
            verify(studentServiceImpl).addCoursesForAllStudents(studentsWithCourse);
        }

        @Test
        void insertDataToDatabase_ShouldThrowException_WhenServiceThrowException(){
            when(studentServiceImpl.getAll()).thenThrow(new ServiceException());
            Assertions.assertThrows(ServiceException.class, () -> dbLoader.insertDataToDatabase());
        }

    }

}