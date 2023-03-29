package validators;

import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {GroupValidator.class})
class GroupValidatorTest {

    @Autowired
    private GroupValidator groupValidator;

    @Nested
    class ValidateOneObject {
        @Test
        void validate_ShouldThrowException_WhenGroupIsNull() {
            Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> groupValidator.validate((Group) null));
            Assertions.assertEquals("Group can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenGroupNameIsNull() {
            Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> groupValidator.validate(new Group(1)));
            Assertions.assertEquals("Group name can't be null", exception.getMessage());
        }

        @Test
        void validate_ShouldThrowException_WhenGroupNameIsLongerThan10Chars() {
            Group group = new Group("gggggggggggggggggggg");
            Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                    () -> groupValidator.validate(group));
            Assertions.assertEquals("Group name can't be more then 10", exception.getMessage());
        }
    }

    @Nested
    class ValidateList {

        @Test
        void validate_ShouldThrowException_WhenListOfGroupsIsNull() {
            IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                groupValidator.validate((List<Group>) null);
            });
            Assertions.assertEquals("Input list of groups can't be null", thrown.getMessage());
        }

        @Test
        void addStudentToGroup_ShouldThrowException_WhenGroupInListIsNull() {
            List<Group> groups = new ArrayList<>();
            groups.add(null);
            IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                groupValidator.validate(groups);
            });
            Assertions.assertEquals("Group can't be null", thrown.getMessage());
        }

        @Test
        void addStudentToGroup_ShouldThrowException_WhenGroupNameInListIsNull() {
            List<Group> groups = new ArrayList<>();
            groups.add(new Group(1));
            IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                groupValidator.validate(groups);
            });
            Assertions.assertEquals("Group name can't be null", thrown.getMessage());
        }
    }
}