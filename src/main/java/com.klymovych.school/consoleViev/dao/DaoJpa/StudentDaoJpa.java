package com.klymovych.school.consoleViev.dao.DaoJpa;

import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentDaoJpa extends JpaRepository<Student, Integer> {
    List<Student> findByCoursesName(String course_name) throws DAOException;

    List<Student> findByCourses(Course course) throws DAOException;
}
