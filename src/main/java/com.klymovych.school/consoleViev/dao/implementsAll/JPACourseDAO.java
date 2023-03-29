package com.klymovych.school.consoleViev.dao.implementsAll;

import com.klymovych.school.consoleViev.dao.CourseDAO;
import com.klymovych.school.consoleViev.dao.DaoJpa.CourseDaoJpa;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JPACourseDAO implements CourseDAO {

    private final CourseDaoJpa dao;
    private CourseValidator courseValidator;

    @Autowired
    public JPACourseDAO(CourseDaoJpa dao, CourseValidator courseValidator) {
        this.dao = dao;
        this.courseValidator = courseValidator;
    }

    @Override
    public Optional<Course> getById(int id) throws DAOException {
        return dao.findById(id);
    }

    @Override
    public void save(Course course)  {
        courseValidator.validate(course);
        dao.save(course);
    }

    @Override
    public void saveAll(List<Course> courses) throws DAOException {
        courseValidator.validate(courses);
        dao.saveAll(courses);
    }

    @Override
    public void update(Course updatedCourse) throws DAOException {
        courseValidator.validate(updatedCourse);
        if (getById(updatedCourse.getId()).isPresent()) {
            dao.save(updatedCourse);
        } else {
            throw new DAOException("No course in database with this id " + updatedCourse.getId());
        }
    }

    @Override
    public void deleteById(int id) throws DAOException {
        if(getById(id).isPresent()) {
            dao.deleteById(id);
        } else {
            throw new DAOException("No course in database with this id " + id);
        }
    }

    @Override
    public List<Course> getAll() throws DAOException {
            return dao.findAll();
    }
}
