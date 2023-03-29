package service.implementsAllinterfases;

import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.dao.implementsAll.JPAGroupDAO;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.GroupServiceImpl;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {

    @MockBean
    private JPAGroupDAO JPAGroupDAO;

    @MockBean
    private GroupValidator groupValidator;

    @Autowired
    private GroupServiceImpl groupServiceImpl;

    @Nested
    class GetByIdTest {
        @Test
        void getById_ShouldReturnsGroup_WhenGroupInDb() {
            Group expectedGroup = new Group("er-45");
            when(JPAGroupDAO.getById(1)).thenReturn(Optional.of(expectedGroup));
            Group actualGroup = groupServiceImpl.getById(1).get();

            Assertions.assertEquals(expectedGroup, actualGroup);
        }

        @Test
        void getById_ShouldThrowsServiceException_WhenNoGroupInDb() {
            int id = 2;
            when(JPAGroupDAO.getById(id)).thenThrow(new DAOException());
            ServiceException exception =
                    Assertions.assertThrows(ServiceException.class, () -> groupServiceImpl.getById(id));
            Assertions.assertEquals("Cannot get group by this id", exception.getMessage());
        }
    }

    @Nested
    class SaveTest {
        @Test
        void save_ShouldSaveGroupToDb_WhenGroupHaveName() {
            Group group = new Group("er-45");
            doNothing().when(JPAGroupDAO).save(any(Group.class));
            groupServiceImpl.save(group);
            verify(JPAGroupDAO, times(1)).save(group);
        }

        @Test
        void save_ShouldThrowException_WhenGroupNoSaveToDb() {
            Group group = new Group("er-45");
            doThrow(new DAOException()).when(JPAGroupDAO).save(any(Group.class));
            ServiceException thrown = Assertions.assertThrows(ServiceException.class, () -> {
                groupServiceImpl.save(group);});
            Assertions.assertEquals("Cannot save data about this group", thrown.getMessage());
        }

        @Test
        void save_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Group group = new Group("er-45");
            doNothing().when(JPAGroupDAO).save(any(Group.class));
            groupServiceImpl.save(group);

            verify(groupValidator).validate(group);
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void update_ShouldCallMethodValidate_WhenSaveDataToDb() {
            Group updatedGroup = new Group(1,"fd-56");
            doNothing().when(JPAGroupDAO).update(any(Group.class));
            groupServiceImpl.update(updatedGroup);

            verify(groupValidator).validate(updatedGroup);
        }

        @Test
        void update_ShouldUpdateGroupInDb_WhenGroupHaveName() {
            Group updatedGroup = new Group(1,"fd-56");
            doNothing().when(JPAGroupDAO).update(any(Group.class));
            groupServiceImpl.update(updatedGroup);
            verify(JPAGroupDAO, times(1)).update(updatedGroup);
        }

        @Test
        void update_ShouldThrowException_WhenGroupNoSaveToDb() throws ServiceException {
            Group updatedGroup = new Group(1,"fu-56");
            doThrow(new DAOException()).when(JPAGroupDAO).update(updatedGroup);
            ServiceException thrown = Assertions.assertThrows(ServiceException.class,
                    () -> groupServiceImpl.update(updatedGroup));
            Assertions.assertEquals("Cannot update data to this group", thrown.getMessage());
        }
    }

    @Nested
    class DeleteByIdTest {
        @Test
        void deleteById_ShouldCallDeleteByIdOnGroupDao_WhenGroupIsInDb() throws ServiceException {
            int id = 1;
            groupServiceImpl.deleteById(id);
            verify(JPAGroupDAO).deleteById(id);
        }

        @Test
        void deleteById_ShouldThrowServiceException_WhenNoGroupInDb() throws ServiceException {
            int id = 1;
            doThrow(new DAOException()).when(JPAGroupDAO).deleteById(anyInt());
            ServiceException exception = Assertions.assertThrows(ServiceException.class,
                    () -> groupServiceImpl.deleteById(id));
            Assertions.assertEquals("Cannot delete data about this group", exception.getMessage());
        }
    }

    @Nested
    class GetAllTest {
        @Test
        void getAll_ShouldReturnListOfGroups_WhenDbNotEmpty() throws ServiceException {
            List<Group> expectedGroups = Arrays.asList(
                    new Group("eg-43"),
                    new Group("hj-67"));
            when(JPAGroupDAO.getAll()).thenReturn(expectedGroups);
            List<Group> actualStudents = groupServiceImpl.getAll();
            Assertions.assertEquals(expectedGroups, actualStudents);
        }

        @Test
        void getAll_ShouldThrowServiceException_WhenGroupDAOThrowsException() {
            when(JPAGroupDAO.getAll()).thenThrow(new DAOException());
            Exception exception = Assertions.assertThrows(ServiceException.class, () -> {
                groupServiceImpl.getAll();
            });
            Assertions.assertEquals("Cannot take data about this groups", exception.getMessage());
        }
    }

    @Nested
    class SaveAllTest {
        @Test
        void saveAll_ShouldSaveListOfStudents_WhenListNotEmpty() throws ServiceException {
            List<Group> groups = Arrays.asList(
                    new Group("eg-73"),
                    new Group("rj-67"));
            groupServiceImpl.saveAll(groups);
            verify(JPAGroupDAO).saveAll(groups);
        }

        @Test
        void saveAll_ShouldThrowServiceException_WhenGroupDAOThrowException() throws ServiceException {
            List<Group> groups = Arrays.asList(
                    new Group("eg-73"),
                    new Group("rj-67"));
            doThrow(DAOException.class).when(JPAGroupDAO).saveAll(any());
            Exception exception = Assertions.assertThrows(ServiceException.class, () -> {
                groupServiceImpl.saveAll(groups);});
            Assertions.assertEquals("Cannot save data about this groups", exception.getMessage());
        }

        @Test
        void saveAll_ShouldCallMethodValidate_WhenSaveDataToDb() {
            List<Group> groups = Arrays.asList(
                    new Group("eg-73"),
                    new Group("rj-67"));
            groupServiceImpl.saveAll(groups);

            verify(groupValidator).validate(groups);
        }
    }

    @Nested
    class GetGroupsWithSameOrFewerAmountOfStudentsTest {

        @Test
        void getGroupsWithSameOrFewerAmountOfStudents_ShouldReturnNotEmptyList_WhenGroupsHaveStudents(){
            List<Group> expectedGroups = Arrays.asList(
                    new Group("eg-73"),
                    new Group("rj-67"));
            when(JPAGroupDAO.getGroupsWithSameOrFewerAmountOfStudents(10)).thenReturn(expectedGroups);
            List<Group> actualGroups = groupServiceImpl.getGroupsWithSameOrFewerAmountOfStudents(10);
            verify(JPAGroupDAO).getGroupsWithSameOrFewerAmountOfStudents(10);
            Assertions.assertEquals(expectedGroups, actualGroups);
        }

        @Test
        void getGroupsWithSameOrFewerAmountOfStudents_ShouldReturnEmptyList_WhenNoGroupsWithThisAmountOfStudents(){
            List<Group> expectedGroups = new ArrayList<>();
            when(JPAGroupDAO.getGroupsWithSameOrFewerAmountOfStudents(10)).thenReturn(expectedGroups);
            List<Group> actualGroups = groupServiceImpl.getGroupsWithSameOrFewerAmountOfStudents(10);
            verify(JPAGroupDAO).getGroupsWithSameOrFewerAmountOfStudents(10);
            Assertions.assertTrue(actualGroups.isEmpty());
        }

        @Test
        void getGroupsWithSameOrFewerAmountOfStudents_ShouldThrowException_WhenGroupDAOThrowException(){
            doThrow(DAOException.class).when(JPAGroupDAO).getGroupsWithSameOrFewerAmountOfStudents(anyInt());
            Exception exception = Assertions.assertThrows(ServiceException.class, () -> {
                groupServiceImpl.getGroupsWithSameOrFewerAmountOfStudents(1);});
            Assertions.assertEquals("Cannot take data about this groups wit same or fewer amount of students",
                    exception.getMessage());
        }
    }
}