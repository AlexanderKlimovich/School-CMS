package com.klymovych.school.consoleViev.dao;

import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;

import java.util.List;

public interface StudentDAO extends DAO<Student > {
    List<Student> findByCoursesName(String course_name) throws DAOException;

    List<Student> findByCourses(Course course) throws DAOException;

    void addToGroup(Student student, Group group) throws DAOException;

    List<Student> getStudentByCourseName(String course_name) throws DAOException;
    void updateAll(List<Student> students) throws DAOException;

}

