package com.klymovych.school.consoleViev.service.implementsAllinterfases;

import com.klymovych.school.consoleViev.dao.CourseDAO;
import com.klymovych.school.consoleViev.dao.StudentDAO;
import com.klymovych.school.consoleViev.dao.implementsAll.DAOException;
import com.klymovych.school.consoleViev.model.Course;
import com.klymovych.school.consoleViev.model.Group;
import com.klymovych.school.consoleViev.model.Student;
import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.StudentService;
import com.klymovych.school.consoleViev.validators.GroupValidator;
import com.klymovych.school.consoleViev.validators.StudentValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final StudentValidator studentValidator;
    private final GroupValidator groupValidator;

    private static final int MAX_OF_COURSES = 4;

    @Autowired
    public StudentServiceImpl(StudentDAO StudentDAO, CourseDAO courseDAO,
                              StudentValidator studentValidator, GroupValidator groupValidator) {
        this.studentDAO = StudentDAO;
        this.courseDAO = courseDAO;
        this.studentValidator = studentValidator;
        this.groupValidator = groupValidator;
    }

    @Override
    public Optional<Student> getById(int id) throws ServiceException {
        try {
            return studentDAO.getById(id);
        } catch (DAOException e) {
            throw new ServiceException("This student is absent in database", e);
        }
    }

    @Override
    public void save(Student student) throws ServiceException {
        studentValidator.validate(student);
        try {
            studentDAO.save(student);
        } catch (DAOException e) {
            throw new ServiceException("Cannot save this student", e);
        }
    }

    @Override
    public void update(Student student) throws ServiceException {
        studentValidator.validate(student);
        try {
            studentDAO.update(student);
        } catch (DAOException e) {
            throw new ServiceException("Cannot update this student", e);
        }
    }

    @Override
    public void deleteById(int id) throws ServiceException {
        try {
            studentDAO.deleteById(id);
        } catch (DAOException e) {
            throw new ServiceException("Cannot delete this student",e);
        }
    }

    @Override
    public List<Student> getStudentByCourseName(String courseName) throws ServiceException {
        try {
            return  studentDAO.getStudentByCourseName(courseName);
        } catch (DAOException e) {
            throw new ServiceException("Cannot get student by this course name",e);
        }
    }

    @Override
    public List<Course> getCoursesOfStudent(Student student) throws ServiceException {
        studentValidator.validate(student);
        try {
            return getById(student.getId()).get().getCourses();
        } catch (DAOException e) {
            throw new ServiceException("Cannot get courses of this student",e);
        }
    }

    @Override
    public void saveAll( List<Student> students) throws ServiceException {
        studentValidator.validate(students);
        try {
            studentDAO.saveAll(students);
        } catch (DAOException e) {
            throw new ServiceException("Cannot save this list of students",e);
        }
    }

    @Override
    public List<Student> getAll() throws ServiceException {
        try {
            return studentDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Cannot get all students",e);
        }
    }

    @Override
    public void addCourse(int studentId, int courseId) throws ServiceException {
        Student student = getById(studentId).get();
        Course course = courseDAO.getById(courseId).get();
        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            save(student);
        } else {
            throw new ServiceException("Student with ID " + studentId + " have course with ID " + courseId);
        }

    }

    @Override
    public void deleteCourse(int studentId, int courseId) throws ServiceException {
        Student student = getById(studentId).get();
        Course course = courseDAO.getById(courseId).get();
        if (student.getCourses().contains(course)) {
            student.getCourses().remove(course);
            studentDAO.save(student);
        } else {
            throw new ServiceException("Cannot delete this course of this student");
        }
    }
    @Override
    public void addStudentToCourseFromList(int courseId) throws ServiceException {
        List<Student> students = getAll();
        Course course = courseDAO.getById(courseId).get();
        List <Student> studentsWithCourse = studentDAO.findByCourses(course);
        studentsWithCourse.forEach(students::remove);
        students.stream().filter(s -> s.getCourses().size() < MAX_OF_COURSES - 1).collect(Collectors.toList());
        try {
            studentDAO.saveAll(students);
        } catch (DAOException e) {
            throw new ServiceException("Cannot add students to this course",e);
        }
    }

    @Override
    public void addStudentToGroup(Student student, Group group) throws ServiceException{
        groupValidator.validate(group);
        try {
            student.setGroup(group);
            save(student);
        } catch (DAOException e) {
            throw new ServiceException("Cannot add students to this group",e);
        }
    }

    @Override
    public void addCoursesForAllStudents(List<Student> students) throws ServiceException {
        studentValidator.validate(students);
        try {
            studentDAO.saveAll(students);
        } catch (DAOException e) {
            throw new ServiceException("Cannot add courses for students",e);
        }
    }

    @Override
    public void updateAll(List<Student> students) throws ServiceException{
        studentValidator.validate(students);
        try {
            studentDAO.saveAll(students);
        } catch (DAOException e) {
            throw new ServiceException("Cannot update package of data",e);
        }
    }
}
