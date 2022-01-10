package com.coders.dungeon;

public class ApplicationRunner {

    public static void run(Class<? extends Application> applicationClass){
        try{
            Application application = applicationClass.newInstance();
            application.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
