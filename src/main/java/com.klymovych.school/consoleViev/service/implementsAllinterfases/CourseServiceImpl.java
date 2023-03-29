package com.klymovych.school.consoleViev.service.implementsAllinterfases;

import com.klymovych.school.consoleViev.dao.CourseDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.CourseService;
import com.klymovych.school.consoleViev.validators.CourseValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO;

    private final CourseValidator courseValidator;

    @Autowired
    public CourseServiceImpl(CourseDAO CourseDAO, CourseValidator courseValidator) {
        this.courseDAO = CourseDAO;
        this.courseValidator = courseValidator;
    }

    @Override
    public Optional<Course> getById(int id) {
        try {
            return courseDAO.getById(id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Course course)  {
        courseValidator.validate(course);
        try {
            courseDAO.save(course);
        } catch (DAOException e) {
            throw new ServiceException("Cannot save data to this course",e);
        }
    }

    @Override
    public void saveAll(List<Course> courses) throws ServiceException {
        courseValidator.validate(courses);
        try {
            courseDAO.saveAll(courses);
        } catch (DAOException e) {
            throw new ServiceException("Cannot get data about this courses",e);
        }
    }

    @Override
    public void update(Course course) throws ServiceException {
        courseValidator.validate(course);
        try {
            courseDAO.update(course);
        } catch (DAOException e) {
            throw new ServiceException("Cannot update data to this course",e);
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        try {
            courseDAO.deleteById(id);
        } catch (DAOException e) {
            throw new ServiceException("Cannot delete data about this course",e);
        }
    }

    @Override
    public List<Course> getAll() throws ServiceException {
        try {
            return courseDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Cannot take data about this courses",e);
        }
    }
}
