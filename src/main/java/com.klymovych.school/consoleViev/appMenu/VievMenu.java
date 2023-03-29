package com.klymovych.school.consoleViev.appMenu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class VievMenu implements Menu {
    private final List<Menu> menu;

    public VievMenu(List<Menu> menuItems) {
        this.menu = menuItems;
    }

    @Override
    public void run (){
        int commandNumber;
        String commands = "";
        Scanner scanner = new Scanner(System.in);
        if(!this.menu.isEmpty()) {
            for (Menu command : this.menu) {
                commands += String.format("%d.%s\n", this.menu.indexOf(command) + 1, command.getName());
            }
        } else {
            throw new NoSuchElementException("No commands found");
        }
        do {
            System.out.println("Enter number one of the menu or if you want finish enter '999'\n" + commands);
            commandNumber = scanner.nextInt();
            if (commandNumber - 1 < this.menu.size()) {
                this.menu.get(commandNumber - 1).run();
            } else if (commandNumber == 999){
                System.out.println("App finish");
            } else  if (commandNumber < 0 || commandNumber > this.menu.size()){
                System.out.println("Wrong command number, repeat please");
            }
        } while (commandNumber != 999);
    }

    @Override
    public String getName() {
        return null;
    }

}
