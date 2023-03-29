package com.klymovych.school.consoleViev.dao.implementsAll;

import com.klymovych.school.consoleViev.dao.CourseDAO;
import com.klymovych.school.consoleViev.dao.DaoJpa.StudentDaoJpa;
import com.klymovych.school.consoleViev.dao.GroupDAO;
import com.klymovych.school.consoleViev.dao.StudentDAO;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import com.klymovych.school.consoleViev.validators.StudentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JPAStudentDAO implements StudentDAO {

    private final StudentDaoJpa dao;

    private final GroupDAO groupDAO;

    private final CourseDAO courseDAO;

    private final StudentValidator studentValidator;

    private final GroupValidator groupValidator;

    @Autowired
    public JPAStudentDAO(StudentDaoJpa dao, StudentValidator studentValidator,
                         GroupValidator groupValidator, JPAGroupDAO groupDAO, CourseDAO courseDAO) {
        this.dao = dao;
        this.studentValidator = studentValidator;
        this.groupValidator = groupValidator;
        this.groupDAO = groupDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public Optional<Student> getById(int id) throws DAOException {
        return dao.findById(id);
    }

    @Override
    public void save(Student student) throws DAOException {
        studentValidator.validate(student);
        dao.save(student);
    }

    @Override
    public void update(Student updatedStudent) throws DAOException {
        studentValidator.validate(updatedStudent);
        if (getById(updatedStudent.getId()).isPresent()) {
            dao.save(updatedStudent);
        } else {
            throw new DAOException("No course in database with this id " + updatedStudent.getId());
        }
    }
    @Override
    public void updateAll(List<Student> students) throws DAOException {
        studentValidator.validate(students);
        students.forEach(student -> isObjectPresentInDb(student.getId()));
        dao.saveAll(students);
    }

    @Override
    public void saveAll(List<Student> students) throws DAOException {
        studentValidator.validate(students);
       dao.saveAll(students);
    }

    @Override
    public void deleteById(int id) throws DAOException {
       if (getById(id).isPresent()) {
           dao.deleteById(id);
       } else {
           throw new DAOException("No student in database with this id " + id);
       }
    }

    @Override
    public List<Student> findByCoursesName(String course_name) throws DAOException {
        return dao.findByCoursesName(course_name);
    }

    @Override
    public List<Student> findByCourses(Course course) throws DAOException {
        if(courseDAO.getById(course.getId()).isPresent()) {
            return dao.findByCourses(course);
        } else {
            throw new DAOException("No course in database with this id " + course.getId());
        }
    }

    @Override
    public void addToGroup(Student student, Group group) throws DAOException {
        studentValidator.validate(student);
        groupValidator.validate(group);
        if (getById(student.getId()).isPresent() & groupDAO.getById(group.getId()).isPresent()) {
            student.setGroup(group);
            dao.save(student);
        } else {
            throw new DAOException("No student or group in database ");
        }
    }

    @Override
    public List<Student> getStudentByCourseName(String course_name) throws DAOException {
        return dao.findByCoursesName(course_name);
    }

    @Override
    public List<Student> getAll() throws DAOException {
        return dao.findAll();
    }

    private void isObjectPresentInDb(int id){
        if(!getById(id).isPresent()){
            throw new DAOException("No student with Id " + id);
        }
    }

}

