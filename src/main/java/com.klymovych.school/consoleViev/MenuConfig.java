package com.klymovych.school.consoleViev;

import com.klymovych.school.consoleViev.appMenu.AddNewStudent;
import com.klymovych.school.consoleViev.appMenu.AddStudentToCourseFromList;
import com.klymovych.school.consoleViev.appMenu.DeleteStudentById;
import com.klymovych.school.consoleViev.appMenu.VievMenu;
import com.klymovych.school.consoleViev.appMenu.FindAllGroupsWithLessOrEqualStudentsNumber;
import com.klymovych.school.consoleViev.appMenu.GetStudentByCourseName;
import com.klymovych.school.consoleViev.appMenu.RemoveStudentFromOneOfTheirCourses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class MenuConfig {

    @Bean
    public VievMenu VievMenu(AddNewStudent addNewStudent,
                             AddStudentToCourseFromList addStudentToCourseFromList,
                             DeleteStudentById deleteStudentById,
                             FindAllGroupsWithLessOrEqualStudentsNumber findAllGroupsWithLessOrEqualStudentsNumber,
                             GetStudentByCourseName getStudentByCourseName,
                             RemoveStudentFromOneOfTheirCourses RemoveStudentFromOneOfTheirCourses){
        return new VievMenu(List.of(addNewStudent, addStudentToCourseFromList, deleteStudentById,
                findAllGroupsWithLessOrEqualStudentsNumber, getStudentByCourseName,RemoveStudentFromOneOfTheirCourses ));
    }

}
