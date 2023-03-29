package consoleViev.appMenu;

import com.klymovych.school.consoleViev.appMenu.AddNewStudent;
import com.klymovych.school.consoleViev.appMenu.AddStudentToCourseFromList;
import com.klymovych.school.consoleViev.appMenu.DeleteStudentById;
import com.klymovych.school.consoleViev.appMenu.FindAllGroupsWithLessOrEqualStudentsNumber;
import com.klymovych.school.consoleViev.appMenu.GetStudentByCourseName;
import com.klymovych.school.consoleViev.appMenu.RemoveStudentFromOneOfTheirCourses;
import com.klymovych.school.consoleViev.appMenu.VievMenu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VievMenuTest {
    PrintStream standardOut = System.out;
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    private AddNewStudent addNewStudent;
    @Mock
    private AddStudentToCourseFromList addStudentToCourseFromList;
    @Mock
    private DeleteStudentById deleteStudentById;
    @Mock
    private GetStudentByCourseName getStudentByCourseName;
    @Mock
    private RemoveStudentFromOneOfTheirCourses removeStudentFromOneOfTheirCourses;

    @Mock
    private FindAllGroupsWithLessOrEqualStudentsNumber findAllGroupsWithLessOrEqualStudentsNumber;

    private VievMenu vievMenu;

    @BeforeEach
    void setUp() {
        vievMenu = new VievMenu(Arrays.asList(addNewStudent,
                addStudentToCourseFromList,
                deleteStudentById,
                findAllGroupsWithLessOrEqualStudentsNumber,
                 getStudentByCourseName,
                 removeStudentFromOneOfTheirCourses)
                );
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void run_ShouldPrintListOfCommands_WhenListNotEmpty() throws Exception {
        when(addNewStudent.getName()).thenReturn("Add New Student");
        withTextFromSystemIn("999").execute(() -> vievMenu.run());
        String expectedOutput1 ="Enter number one of the menu or if you want finish enter '999'";
        String expectedOutput2 = "1.Add New Student";
        String actualOutput = outputStreamCaptor.toString();

        Assertions.assertTrue(actualOutput.contains(expectedOutput1));
        Assertions.assertTrue(actualOutput.contains(expectedOutput2));
    }

    @Test
    void run_ShouldRunCommandFromList_WhenLIstNotEmpty() throws Exception {
        withTextFromSystemIn("1", "999").execute(() -> vievMenu.run());
        verify(addNewStudent).run();
    }

    @Test
    void run_ShouldPrintToConsole_WhenUserWriteCommandToExitFromApp() throws Exception {
        when(addNewStudent.getName()).thenReturn("Add New Student");
        withTextFromSystemIn("999").execute(() -> vievMenu.run());

        String expectedOutput= "App finish";
        String actualOutput = outputStreamCaptor.toString();
        Assertions.assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    void run_ShouldWriteMassageToUser_WhenNoCommandWithInputNumber() throws Exception {
        when(addNewStudent.getName()).thenReturn("Add New Student");
        withTextFromSystemIn("10", "999").execute(() -> vievMenu.run());

        String expectedOutput= "Wrong command number, repeat please";
        String actualOutput = outputStreamCaptor.toString();
        Assertions.assertTrue(actualOutput.contains(expectedOutput));
    }

    @Test
    void run_ShouldThrowException_WhenInputIsNotInteger (){
        Assertions.assertThrows(InputMismatchException.class,
                () -> withTextFromSystemIn("abc").execute(() -> vievMenu.run()));
    }

}