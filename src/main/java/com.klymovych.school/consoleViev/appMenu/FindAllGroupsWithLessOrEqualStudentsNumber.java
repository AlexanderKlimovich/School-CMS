package com.klymovych.school.consoleViev.appMenu;

import com.klymovych.school.consoleViev.service.ServiceException;
import com.klymovych.school.consoleViev.service.implementsAllinterfases.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class FindAllGroupsWithLessOrEqualStudentsNumber implements Menu{
    private GroupServiceImpl groupService;

    private final String name = "Find all groups with less or equal students number";

    @Autowired
    public FindAllGroupsWithLessOrEqualStudentsNumber(GroupServiceImpl groupServiceImpl) {
        this.groupService = groupServiceImpl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        System.out.println("Enter amount of students in group");
        Scanner scanner = new Scanner(System.in);
        try {
            groupService.getGroupsWithSameOrFewerAmountOfStudents(scanner.nextInt())
                    .forEach(group ->{System.out.println(group.getName());});
        } catch (ServiceException e) {
            throw new IllegalArgumentException(e);
        }
    }
}