package com.klymovych.school.consoleViev.validators;

import com.klymovych.school.consoleViev.model.Group;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class GroupValidator {

    public void validate(List<Group> groups) {
        validateNonNull(groups, "Input list of groups");
        groups.forEach(this::validate);
    }

    public void validate(Group group) {
        validateNonNull(group, "Group");
        validateNonNull(group.getName(), "Group name");
        validateFieldLength(group.getName(), "Group name",10);
    }

    private void validateNonNull(Object object, String name){
        Optional.ofNullable(object).orElseThrow(() -> new IllegalArgumentException(
                name +" can't be null"));
    }

    private void validateFieldLength(String object, String name, int length) {
        if(object.length() > length){
            throw new IllegalArgumentException(name +" can't be more then " + length);
        }
    }

}
