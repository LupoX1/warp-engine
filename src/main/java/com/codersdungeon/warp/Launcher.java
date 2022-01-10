package com.codersdungeon.warp;

import com.codersdungeon.warp.engine.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Launcher {
    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    public static void main(String... args){
        LOG.info("Start");
        try (InputStream inputStream = Launcher.class.getClassLoader().getResourceAsStream("application.properties")){
            Properties properties = new Properties();
            properties.load(inputStream);
            String className = properties.getProperty("application.class");
            Class<Application> applicationClass = (Class<Application>) Class.forName(className);
            Application application = applicationClass.getDeclaredConstructor().newInstance();
            application.run(properties);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("End");
    }
}
