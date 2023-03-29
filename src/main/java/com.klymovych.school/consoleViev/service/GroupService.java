package com.klymovych.school.consoleViev.service;

import com.klymovych.school.consoleViev.model.Group;
import java.util.List;

public interface GroupService extends Service<Group> {
    List<Group> getGroupsWithSameOrFewerAmountOfStudents(int amountOfStudents) throws ServiceException;
}

