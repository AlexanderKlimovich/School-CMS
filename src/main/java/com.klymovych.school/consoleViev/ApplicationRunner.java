package com.klymovych.school.consoleViev;

import com.klymovych.school.consoleViev.appMenu.VievMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    private final DbLoader dbLoader;

    private final VievMenu vievMenu;

    @Autowired
    public ApplicationRunner(DbLoader dbLoader, VievMenu vievMenu) {
        this.dbLoader = dbLoader;
        this.vievMenu = vievMenu;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dbLoader.insertDataToDatabase();
        vievMenu.run();
    }
}
