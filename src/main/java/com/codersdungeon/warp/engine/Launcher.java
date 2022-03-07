package com.codersdungeon.warp.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    public static void main(String... args){
        LOG.info("Start");

        Application application = new DummyApplication();
        application.run();

        LOG.info("End");
    }
}
