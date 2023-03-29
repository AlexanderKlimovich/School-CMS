package com.klymovych.school.consoleViev.service;

import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;

import java.util.List;

public interface StudentService extends Service<Student> {
    List<Student> getStudentByCourseName(String courseName) throws ServiceException;

    List<Course> getCoursesOfStudent(Student student) throws ServiceException;

    void addCourse(int studentId, int courseId) throws ServiceException;

    void deleteCourse(int studentId, int courseId) throws ServiceException;

    void addStudentToCourseFromList(int courseId) throws ServiceException;

    void addStudentToGroup(Student student, Group group) throws ServiceException;

    void addCoursesForAllStudents(List<Student> students) throws DAOException;

    void updateAll(List<Student> students) throws ServiceException;
}

