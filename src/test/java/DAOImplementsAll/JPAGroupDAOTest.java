package DAOImplementsAll;

import com.klymovych.school.consoleViev.dao.CourseDAO;
import com.klymovych.school.consoleViev.dao.DaoJpa.CourseDaoJpa;
import com.klymovych.school.consoleViev.dao.DaoJpa.GroupDaoJpa;
import com.klymovych.school.consoleViev.dao.DaoJpa.StudentDaoJpa;
import com.klymovych.school.consoleViev.dao.GroupDAO;
import com.klymovych.school.consoleViev.dao.StudentDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.dao.implementsAll.JPACourseDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.JPAGroupDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.JPAStudentDAO;
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
import org.springframework.boot.test.mock.mockito.MockBean;;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = RepositoryConfig.class)
@Sql(scripts = {"/sql/schemaTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JPAGroupDAOTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private StudentDaoJpa studentDao;
    @Autowired
    private GroupDaoJpa groupDao;
    @Autowired
    private CourseDaoJpa courseDao;

    private GroupDAO groupDAOImpl;
    private StudentDAO studentDAOImpl;
    private CourseDAO courseDAOImpl;

    @MockBean
    private GroupValidator groupValidator;
    @MockBean
    private StudentValidator studentValidator;
    @MockBean
    private CourseValidator courseValidator;


    @BeforeEach
    void setUp(){
        groupDAOImpl = new JPAGroupDAO(groupValidator,  groupDao);
        courseDAOImpl = new JPACourseDAO(courseDao, courseValidator);
        studentDAOImpl = new JPAStudentDAO( studentDao, studentValidator, groupValidator,
                 (JPAGroupDAO) groupDAOImpl, courseDAOImpl);
    }

    @Nested
    class GetByIdTest {

        @Test
        void getById_ShouldReturnGroup_whenExistingGroupInDb() {
            entityManager.persist(new Group("rt-23"));
            Group group = groupDAOImpl.getById(1).get();
            Assertions.assertEquals(1, group.getId());
            Assertions.assertEquals("rt-23", group.getName());
        }

        @Test
        void getById_ShouldReturnEmptyOptional_whenNonExistingGroupInDb() {
            Optional<Group> group = groupDAOImpl.getById(23);
            Assertions.assertTrue(group.isEmpty());
        }

    }
    @Nested
    class SaveTest {

        @Test
        void save_ShouldSaveSuccessfully_WhenGroupHasName() {
            entityManager.persist(new Group("group-1"));
            Group savedGroup = groupDAOImpl.getById(1).get();
            Assertions.assertNotNull(savedGroup);
            Assertions.assertEquals("group-1", savedGroup.getName());
        }

        @Test
        void save_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Group group = new Group("group-1");
            groupDAOImpl.save(group);

            verify(groupValidator).validate(group);
        }

    }
    @Nested
    class SaveAllTest {

        @Test
        public void saveAll_ShouldSaveSuccessfully_WhenGroupsHaveNames() throws DAOException {
            List<Group> groups = new ArrayList<>();
            groups.add(new Group("group1"));
            groups.add(new Group("group2"));
            groups.add(new Group("group3"));
            groups.forEach(entityManager::persist);
            List<Group> savedGroups = groupDAOImpl.getAll();
            Assertions.assertEquals(groups.size(), savedGroups.size() );
            for (int i = 0; i < groups.size(); i++) {
                Assertions.assertEquals(groups.get(i).getName(), savedGroups.get(i).getName());
            }
        }

        @Test
        void saveAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            List<Group> groups = new ArrayList<>();
            groups.add(new Group("group1"));
            groupDAOImpl.saveAll(groups);

            verify(groupValidator).validate(groups);
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void update_ShouldUpdateDataInDb_WhenGroupInDbAndUpdatedGroupHasName() {
            entityManager.persist(new Group("we-35"));
            Group updatedGroup = new Group(1, "rt-23");
            groupDAOImpl.update(updatedGroup);
            Group updated = groupDAOImpl.getById(updatedGroup.getId()).get();
            Assertions.assertEquals(updatedGroup.getName(), updated.getName());
        }

        @Test
        void update_ShouldThrowException_WhenNoGroupInDbWithInputId() {
            Exception exception = Assertions.assertThrows(DAOException.class,
                    () -> groupDAOImpl.update(new Group(52, "dtr")));
            Assertions.assertEquals("No group in database with this id 52", exception.getMessage());
        }

        @Test
        void update_ShouldCallMethodValidate_WhenSaveDataToDb() {
            entityManager.persist(new Group("we-35"));
            Group updatedGroup = new Group(1, "rt-23");
            groupDAOImpl.update(updatedGroup);

            verify(groupValidator).validate(updatedGroup);
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void delete_ShouldDeleteGroupFromDb_WhenGroupExistInDb() {
            entityManager.persist(new Group("re-43"));
            Group group = groupDAOImpl.getById(1).get();
            groupDAOImpl.deleteById(1);
            DAOException exception = Assertions.assertThrows(DAOException.class, () -> groupDAOImpl.deleteById(1));
            Assertions.assertEquals("No group in database with this id 1", exception.getMessage());
        }

        @Test
        void delete_ShouldThrowException_WhenNoGroupInDb() {
            DAOException exception = Assertions.assertThrows(DAOException.class, () -> groupDAOImpl.deleteById(10));
            Assertions.assertEquals("No group in database with this id 10", exception.getMessage());
        }
    }

    @Nested
    class GetAllTest {

        @Test
        void getAll_ShouldReturnNotEmptyList_WhenGroupsAreInDb() {
            entityManager.persist(new Group("cf-21"));
            List<Group> groups = groupDAOImpl.getAll();
            Assertions.assertFalse(groups.isEmpty());
            Assertions.assertEquals(groups.get(0).getClass(), Group.class);
        }

    }

    @Nested
    class GetGroupWithSameOrFewerAmountOfStudentsTest {

        @Test
        void getGroupsWithSameOrFewerAmountOfStudents_ShouldReturnListOfGroups_WhenHaveDataInDb() {
            entityManager.persist(new Group("cr-21"));
            entityManager.persist(new Group("df-12"));
            entityManager.persist(new Student("st1", "st1"));
            entityManager.persist(new Student("st3", "st3"));
            entityManager.persist(new Student("st4", "st4"));
            studentDAOImpl.update(new Student(1, new Group(1), "st1", "st1"));
            studentDAOImpl.update(new Student(2, new Group(1), "st3", "st3"));
            studentDAOImpl.update(new Student(3, new Group(2), "st4", "st4"));
            List<Group> groups = groupDAOImpl.getGroupsWithSameOrFewerAmountOfStudents(2);

            Assertions.assertEquals("df-12", groups.get(0).getName());
            Assertions.assertEquals("cr-21", groups.get(1).getName());

        }

        @Test
        void getGroupsWithSameOrFewerAmountOfStudents_ShouldReturnListOfGroups_WhenNoStudentsInGroup(){
            entityManager.persist(new Group("ty-89"));
            List<Group> groups = groupDAOImpl.getGroupsWithSameOrFewerAmountOfStudents(5);
            Assertions.assertEquals("ty-89", groups.get(0).getName());
        }

        @Test
        void getGroupsWithSameOrFewerAmountOfStudents_ShouldReturnEmptyList_WhenNoGroupsWithThisAmountOfStudents(){
            entityManager.persist(new Group("cr-21"));
            entityManager.persist(new Student( new Group(1),"st1", "st1"));
            entityManager.persist(new Student(new Group(1),"st3", "st3"));
            entityManager.persist(new Student(new Group(1),"st4", "st4"));
            List<Group> groups = groupDAOImpl.getGroupsWithSameOrFewerAmountOfStudents(1);
            Assertions.assertTrue(groups.isEmpty());
        }

        @Test
        void getGroupsWithSameOrFewerAmountOfStudents_ShouldReturnEmptyListOf_WhenNoDataInDb() {
            List<Group> groups = groupDAOImpl.getGroupsWithSameOrFewerAmountOfStudents(1);
            Assertions.assertTrue(groups.isEmpty());
        }

    }

    @Nested
    class GetStudentsFromGroupTest {

        @Test
        void getStudentsFromGroup_ShouldReturnListOfStudents_WhenStudentsHaveSameGroup() {
            entityManager.persist(new Group("cr-21"));
            entityManager.persist(new Student("st1", "st1"));
            Group group = groupDAOImpl.getById(1).get();
            Student student = studentDAOImpl.getById(1).get();
            student.setGroup(group);
            studentDAOImpl.update(student);
            List<Student> students = groupDAOImpl.getStudentsFromGroup(group.getId());
            Assertions.assertFalse(students.isEmpty());
        }

        @Test
        void getStudentsFromGroup_ShouldReturnEmptyList_WhenNoStudentsInGroup() {
            entityManager.persist(new Group("df-56"));
            List<Student> students = groupDAOImpl.getStudentsFromGroup(1);
            Assertions.assertTrue(students.isEmpty());
        }

    }
}