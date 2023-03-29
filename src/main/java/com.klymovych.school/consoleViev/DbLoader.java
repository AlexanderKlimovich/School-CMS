package com.klymovych.school.consoleViev;

import com.klymovych.school.consoleViev.service.implementsAllinterfases.CourseServiceImpl;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.GroupServiceImpl;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import com.klymovych.school.consoleViev.model.Student;

@Component
public class DbLoader  {

    private final GroupServiceImpl groupServiceImpl;

    private final CourseServiceImpl courseServiceImpl;

    private final StudentServiceImpl studentServiceImpl;

    private final MakerDataToDataBase makerDataToDataBase;

    @Autowired
    public DbLoader(GroupServiceImpl groupServiceImpl, CourseServiceImpl courseServiceImpl,
                    StudentServiceImpl studentServiceImpl, MakerDataToDataBase makerDataToDataBase) {
        this.groupServiceImpl = groupServiceImpl;
        this.courseServiceImpl = courseServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
        this.makerDataToDataBase = makerDataToDataBase;
    }

    public void insertDataToDatabase() throws IOException {
        groupServiceImpl.saveAll(makerDataToDataBase.makeGroups());
        courseServiceImpl.saveAll(makerDataToDataBase.makeCourses("coursesNames.csv"));
        studentServiceImpl.saveAll(makerDataToDataBase.makeStudents());
        List<Student> students = studentServiceImpl.getAll();
        studentServiceImpl.updateAll(makerDataToDataBase.addStudentsToGroup(students, groupServiceImpl.getAll()));
        studentServiceImpl.addCoursesForAllStudents(makerDataToDataBase.addCourseToStudent(students,
                courseServiceImpl.getAll()));
    }

}

