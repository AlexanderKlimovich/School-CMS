package com.klymovych.school.consoleViev.dao;

import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;


import java.util.List;


public interface GroupDAO extends DAO<Group> {
    List<Group> getGroupsWithSameOrFewerAmountOfStudents(int maxStudents) throws DAOException;
    List<Student> getStudentsFromGroup(int id) throws DAOException;
}
